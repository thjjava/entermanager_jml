package com.sttri.action;

import java.io.PrintWriter;
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
import com.sttri.pojo.TblDev;
import com.sttri.pojo.TblUser;
import com.sttri.pojo.UserQuestion;
import com.sttri.service.ICompanyGroupService;
import com.sttri.service.IDevService;
import com.sttri.service.IUserQuestionService;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;


public class UserQuestionAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rows;            
	private String page;
	
	private UserQuestion userQuestion;
	
	@Autowired
	private IUserQuestionService userQuestionService;
	@Autowired
	private IDevService devService;
	@Autowired
	private ICompanyGroupService groupService;
	
	
	
	//导出
	public void exportExcel(){
		response.setCharacterEncoding("UTF-8");
		String startTime = Util.dealNull(request.getParameter("startTime"));
		String endTime = Util.dealNull(request.getParameter("endTime"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		TblUser u = WorkUtil.getCurrUser(request);
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		try {
			if (u != null) {
				StringBuffer jpql = new StringBuffer("1 =1 ");
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
				List<UserQuestion> list = this.userQuestionService.getResultList(jpql.toString(),orderBy);
				response.reset();//清除缓存
				String fileName = "晨会自评.xls";//文件名
				//设置下载文件名
				response.addHeader("Content-Disposition", "attachment;filename="+
				new String(fileName.getBytes("gb2312"),"iso8859-1"));
				Map<String, String> map=new LinkedHashMap<String, String>();
				map.put("higherGroupName", "大区");
				map.put("parentGroupName", "市级部");
				map.put("curGroupName", "当前组织");
				map.put("devName", "会议组织人");
				map.put("answer1", "唱营销之歌");
				map.put("answer6", "业绩汇总、收入分析");
//				map.put("answer2", "昨日追踪");
				map.put("answer3", "早会汇总表、日线路检讨");
				map.put("answer4", "经销商/小老板七大工法日检讨");
				map.put("answer7", "早展售直播");
				map.put("answer5", "参与沟通人数");
				map.put("timeLen", "会议时长");
				map.put("score", "得分");
				map.put("addTime", "时间");
				response.setContentType("application/x-download");
	        	com.sttri.util.ExcelUtil.ImportExcel(list, response.getOutputStream(), map, "晨会自评");
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
		String groupId = Util.dealNull(request.getParameter("groupId"));
		TblUser u = WorkUtil.getCurrUser(request);
		PageView<UserQuestion> pageView = new PageView<UserQuestion>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer("1 =1 ");
			jpql.append(" and o.comId =? ");
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
			QueryResult<UserQuestion> qr = this.userQuestionService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"answer1\":\"无记录数据\"}]}";
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

	public UserQuestion getUserQuestion() {
		return userQuestion;
	}

	public void setUserQuestion(UserQuestion userQuestion) {
		this.userQuestion = userQuestion;
	}

}
