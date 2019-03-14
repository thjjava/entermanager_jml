package com.sttri.action;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.CompanyGroup;
import com.sttri.pojo.DevRecordTime;
import com.sttri.pojo.TblDev;
import com.sttri.pojo.TblUser;
import com.sttri.service.ICompanyGroupService;
import com.sttri.service.IDevRecordTimeService;
import com.sttri.service.IDevService;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;


public class DevRecordTimeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rows;            
	private String page;
	
	private DevRecordTime devRecordTime;
	@Autowired
	private IDevRecordTimeService devRecordTimeService;
	@Autowired
	private IDevService devService;
	@Autowired
	private ICompanyGroupService groupService;
	
	
	//设备日志导出
	public void exportExcel(){
		response.setCharacterEncoding("UTF-8");
		String startTime = Util.dealNull(request.getParameter("startTime"));
		String endTime = Util.dealNull(request.getParameter("endTime"));
		String devName = Util.dealNull(request.getParameter("devName"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		TblUser u = WorkUtil.getCurrUser(request);
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		try {
			if (u != null) {
				StringBuffer jpql = new StringBuffer("1 =1 and o.status =0");
				jpql.append(" and o.dev.company.id ='"+u.getCompany().getId()+"'");
				if(!startTime.equals("")){
					jpql.append(" and o.addTime >= '"+startTime+"'");
					if(!endTime.equals("")){
						jpql.append(" and o.addTime<='"+endTime+"'");
					}else{
						jpql.append(" and o.addTime<='"+Util.dateToStr(new Date())+"'");
					}
				}else {
					jpql.append(" and o.addTime like '"+Util.dateToStr(new Date()).substring(0,10)+"%'");
				}
				if(!"".equals(devName)){
					jpql.append(" and o.dev.devName like '%"+URLDecoder.decode(devName.trim(),"UTF-8")+"%'");
				}
				
				JSONArray array = new JSONArray();
				if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
					if (groupId == null || "".equals(groupId)) {
						groupId = u.getGroupId();
					}
					CompanyGroup group = this.groupService.getById(groupId);
					if (!group.getPid().equals("0")) {
						array = getArray(groupId,array);
						String jpqlStr = "";
						if (array.size() > 0) {
							jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
						}else {
							jpqlStr = "('')";
						}
						jpql.append(" and o.dev.id in "+ jpqlStr);
					}
				}
				orderBy.put("addTime", "asc");
				List<DevRecordTime> list = this.devRecordTimeService.getResultList(jpql.toString(),orderBy);
				response.reset();//清除缓存
				String fileName = "直播时长统计.xls";//文件名
				//设置下载文件名
				response.addHeader("Content-Disposition", "attachment;filename="+
				new String(fileName.getBytes("gb2312"),"iso8859-1"));
				Map<String, String> map=new LinkedHashMap<String, String>();
				map.put("devName", "设备名称");
				map.put("recordStartTime", "直播开始时间");
				map.put("recordEndTime", "直播结束时间");
				map.put("timeLen", "直播时长");
				response.setContentType("application/x-download");
	        	com.sttri.util.ExcelUtil.ImportExcel(list, response.getOutputStream(), map, "直播时长统计");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
		
	/**
	 * 根据用户的组织id，查询该组织所有子节点的设备id
	 */
	public JSONArray getArray(String id,JSONArray array){
		List<TblDev> devs = this.devService.getResultList("o.group.id=?", null, new Object[]{id});
		if (devs != null && devs.size() >0) {
			for (TblDev tblDev : devs) {
				array.add(tblDev.getId());
			}
		}
		
		//查询组织表中，该ID的根节点下的所有子节点
		List<CompanyGroup> gList = this.groupService.getResultList(" o.pid=?", null, new Object[]{id});
		if(gList != null && gList.size()>0){
			for (CompanyGroup companyGroup : gList) {
				String gid = companyGroup.getId();
				List<TblDev> dList = this.devService.getResultList(" o.group.id=?", null, new Object[]{gid});
				if (dList != null && dList.size() >0) {
					for (TblDev tblDev : dList) {
						array.add(tblDev.getId());
					}
				}
				getArray(gid,array);//递归查询gid该节点的子节点
			}
		}
		return array;
	}
	
	public void query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		String queryDevName = Util.dealNull(request.getParameter("queryDevName"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		TblUser u = WorkUtil.getCurrUser(request);
		PageView<DevRecordTime> pageView = new PageView<DevRecordTime>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer("1 =1 ");
			jpql.append(" and o.dev.company.id =?");
			param.add(u.getCompany().getId());
			
			if(!addTimeStart.equals("")){
				jpql.append(" and o.addTime >=?");
				param.add(addTimeStart);
				if(!addTimeEnd.equals("")){
					jpql.append(" and o.addTime<=?");
					param.add(addTimeEnd);
				}else{
					jpql.append(" and o.addTime<=?");
					param.add(Util.dateToStr(new Date()));
				}
			}
			if(!"".equals(queryDevName)){
				jpql.append(" and o.dev.devName like ?");
				param.add("%"+queryDevName.trim()+"%");
			}
			JSONArray array = new JSONArray();
			if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
				if (groupId == null || "".equals(groupId)) {
					groupId = u.getGroupId();
				}
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					array = getArray(groupId,array);
					String jpqlStr = "";
					if (array.size() > 0) {
						jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					}else {
						jpqlStr = "('')";
					}
					jpql.append(" and o.dev.id in "+ jpqlStr);
				}
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("addTime", "desc");
			QueryResult<DevRecordTime> qr = this.devRecordTimeService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"devName\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public DevRecordTime getDevRecordTime() {
		return devRecordTime;
	}

	public void setDevRecordTime(DevRecordTime devRecordTime) {
		this.devRecordTime = devRecordTime;
	}

}
