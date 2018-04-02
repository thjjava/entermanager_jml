package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.CompanyGroup;
import com.sttri.pojo.DevLog;
import com.sttri.pojo.TblDev;
import com.sttri.pojo.TblUser;
import com.sttri.service.ICompanyGroupService;
import com.sttri.service.IDevLogService;
import com.sttri.service.IDevService;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;


public class DevLogAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rows;            
	private String page;
	
	private DevLog devLog;
	@Autowired
	private IDevLogService devLogService;
	@Autowired
	private IDevService devService;
	@Autowired
	private ICompanyGroupService groupService;
	
	
	//手机端登录统计，以logType=0,设备成功开启直播为准 ,type=1表示按月查，type=2表示按年查询
	public void devLoginTimeCount(){
		response.setCharacterEncoding("UTF-8");
		String queryTime = Util.dealNull(request.getParameter("queryTime"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		String type = Util.dealNull(request.getParameter("type"));
		try {
			JSONArray array = new JSONArray();
			JSONObject unLoginObj = new JSONObject();
			unLoginObj.put("name", "未登录");
			unLoginObj.put("type", "bar");
			JSONObject loginObj = new JSONObject();
			loginObj.put("name", "登录");
			loginObj.put("type", "bar");
			JSONArray loginArray = new JSONArray();
			JSONArray unLoginArray = new JSONArray();
			TblUser u = WorkUtil.getCurrUser(request);
			if (u != null) {
				StringBuffer jpql = new StringBuffer("1 =1 ");
				jpql.append(" and o.company.id =?");
				JSONArray devArray = new JSONArray();
				String jpqlStr = "";
				if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
					if (groupId == null || "".equals(groupId)) {
						groupId = u.getGroupId();
					}
					CompanyGroup group = this.groupService.getById(groupId);
					if (!group.getPid().equals("0")) {
						devArray = getArray(groupId,devArray);
						if (devArray.size() > 0) {
							jpqlStr = devArray.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
						}else {
							jpqlStr = "('')";
						}
						jpql.append(" and o.id in "+ jpqlStr);
					}
				}
				List<TblDev> devs = this.devService.getResultList(jpql.toString(), null,new Object[]{u.getCompany().getId()});
				int loginNum =0,unLoginNum = 0;
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				List<DevLog> devLogs = null;
				if("1".equals(type)){
					if ("".equals(queryTime)) {
						queryTime = Util.dateToStr(new Date()).substring(0,7);
					}
					for (int i = 1; i <= 31; i++) {
						if(!(queryTime.substring(0, 4).equals(year+"") && queryTime.substring(5,7).equals(month+"") && i > day)){
							String d = i<10?"0"+i:i+"";
							queryTime = queryTime+"-"+d;
							String hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=0 and o.addTime like '"+queryTime+"%' group by o.dev.id";
							if (!"".equals(jpqlStr)) {
								hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=0 and o.addTime like '"+queryTime+"%' and o.dev.id in "+ jpqlStr+" group by o.dev.id";
							}
							devLogs = this.devLogService.getResultList(hql);
							queryTime = queryTime.substring(0,7);
							loginNum = devLogs.size();
							unLoginNum = devs.size() - devLogs.size();
						}else {
							loginNum =0;
							unLoginNum = devs.size();
						}
						loginArray.add(loginNum);
						unLoginArray.add(unLoginNum);
					}
					
				}else {
					if ("".equals(queryTime)) {
						queryTime = Util.dateToStr(new Date()).substring(0,4);
					}
					for (int i = 1; i <= 12; i++) {
						if(!(queryTime.substring(0, 4).equals(year+"") && i > month)){
							String m = i<10?"0"+i:i+"";
							queryTime = queryTime+"-"+m;
							String hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=0 and o.addTime like '"+queryTime+"%' group by o.dev.id";
							if (!"".equals(jpqlStr)) {
								hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=0 and o.addTime like '"+queryTime+"%' and o.dev.id in "+ jpqlStr+" group by o.dev.id";
							}
							devLogs = this.devLogService.getResultList(hql);
							queryTime = queryTime.substring(0,4);
							loginNum = devLogs.size();
							unLoginNum = devs.size() - devLogs.size();
						}else {
							loginNum =0;
							unLoginNum = devs.size();
						}
						loginArray.add(loginNum);
						unLoginArray.add(unLoginNum);
					}
				}
				loginObj.put("data", loginArray);
				array.add(loginObj);
				unLoginObj.put("data", unLoginArray);
				array.add(unLoginObj);
			}
			System.out.println(array.toString());
			PrintWriter pw = response.getWriter();
			pw.print(array.toString());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/*//人员上线统计导出
	public void exportOnLineExcel(){
		response.setCharacterEncoding("UTF-8");
		String onLine = Util.dealNull(request.getParameter("onLine"));
		String startTime = Util.dealNull(request.getParameter("startTime"));
		String endTime = Util.dealNull(request.getParameter("endTime"));
		String devNo = Util.dealNull(request.getParameter("devNo"));
		TblUser u = WorkUtil.getCurrUser(request);
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		try {
			if (u != null) {
				StringBuffer jpql = new StringBuffer("1 =1 ");
				jpql.append(" and o.dev.company.id ='"+u.getCompany().getId()+"' and o.logType= 0");
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
				if(!"".equals(devNo)){
					jpql.append(" and o.dev.devNo='"+devNo+"'");
				}
				String groupId = u.getGroupId();
				JSONArray array = new JSONArray();
				if (groupId != null && !groupId.equals("")) {
					CompanyGroup group = this.groupService.getById(groupId);
					if (!group.getPid().equals("0")) {
						array = getArray(groupId,array);
						String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
						if (array != null && !array.equals("")) {
							jpql.append(" and o.dev.id in "+ jpqlStr);
						}
					}
				}
				orderBy.put("addTime", "asc");
				List<DevLog> list = this.devLogService.getResultList(jpql.toString(),orderBy);
				String fileName = "人员上线统计.xls";//文件名
				response.reset();//清除缓存
				//设置下载文件名
				response.addHeader("Content-Disposition", "attachment;filename="+
				new String(fileName.getBytes("gb2312"),"iso8859-1"));
				Map<String, String> map=new LinkedHashMap<String, String>();
				map.put("higherGroupName", "上上级组织");
				map.put("parentGroupName", "上级组织");
				map.put("curGroupName", "当前组织");
				map.put("devNo", "设备账号");
				map.put("devName", "设备名称");
				response.setContentType("application/x-download");
				if ("0".equals(onLine)) {
					map.put("addTime", "上线时间");
					com.sttri.util.ExcelUtil.ImportExcel(list, response.getOutputStream(), map, "人员上线统计");
				}else {
					for (DevLog devLog : list) {
						array.add(devLog.getDev().getId());
					}
					String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					List<TblDev> devs =  new ArrayList<TblDev>();
					if (array != null && !array.equals("")) {
						devs = this.devService.getResultList(" o.id not in "+jpqlStr,null);
					}
					com.sttri.util.ExcelUtil.ImportExcel(devs, response.getOutputStream(), map, "人员上线");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}*/
	
	//设备日志导出
	public void exportExcel(){
		response.setCharacterEncoding("UTF-8");
		String queryLogType = Util.dealNull(request.getParameter("queryLogType"));
		String startTime = Util.dealNull(request.getParameter("startTime"));
		String endTime = Util.dealNull(request.getParameter("endTime"));
		String devNo = Util.dealNull(request.getParameter("devNo"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		TblUser u = WorkUtil.getCurrUser(request);
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		try {
			if (u != null) {
				StringBuffer jpql = new StringBuffer("1 =1 ");
				jpql.append(" and o.dev.company.id ='"+u.getCompany().getId()+"'");
				if (!"".equals(queryLogType)) {
					jpql.append(" and o.logType="+Integer.parseInt(queryLogType));
				}
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
				if(!"".equals(devNo)){
					jpql.append(" and o.dev.devNo='"+devNo+"'");
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
				List<DevLog> list = this.devLogService.getResultList(jpql.toString(),orderBy);
				response.reset();//清除缓存
				String fileName = "设备日志.xls";//文件名
				//设置下载文件名
				response.addHeader("Content-Disposition", "attachment;filename="+
				new String(fileName.getBytes("gb2312"),"iso8859-1"));
				Map<String, String> map=new LinkedHashMap<String, String>();
				map.put("devName", "设备名称");
				map.put("logDesc", "日志内容");
				map.put("addTime", "时间");
				response.setContentType("application/x-download");
	        	com.sttri.util.ExcelUtil.ImportExcel(list, response.getOutputStream(), map, "设备日志");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//手机端登录统计，以logType=0,设备成功开启直播为准
	public void devLoginCount(){
		response.setCharacterEncoding("UTF-8");
		String queryTime = Util.dealNull(request.getParameter("queryTime"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		try {
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			TblUser u = WorkUtil.getCurrUser(request);
			if (u != null) {
				StringBuffer jpql = new StringBuffer("1 =1 ");
				jpql.append(" and o.company.id =?");
				JSONArray devArray = new JSONArray();
				String jpqlStr = "";
				if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
					if (groupId == null || "".equals(groupId)) {
						groupId = u.getGroupId();
					}
					CompanyGroup group = this.groupService.getById(groupId);
					if (!group.getPid().equals("0")) {
						devArray = getArray(groupId,devArray);
						if (devArray.size() > 0) {
							jpqlStr = devArray.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
						}else {
							jpqlStr = "('')";
						}
						jpql.append(" and o.id in "+ jpqlStr);
					}
				}
				List<TblDev> devs = this.devService.getResultList(jpql.toString(), null,new Object[]{u.getCompany().getId()});
				if ("".equals(queryTime)) {
					queryTime = Util.dateToStr(new Date()).substring(0,10);
				}
				String hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=0 and o.addTime like '"+queryTime+"%' group by o.dev.id";
				if (!"".equals(jpqlStr)) {
					hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=0 and o.addTime like '"+queryTime+"%' and o.dev.id in "+ jpqlStr+" group by o.dev.id";
				}
				List<DevLog> devLogs = this.devLogService.getResultList(hql);
				obj.put("value", devLogs.size());
				obj.put("name", "手机端已登录数("+(devLogs.size())+")");
				array.add(obj);
				obj.put("value", devs.size()-devLogs.size());
				obj.put("name", "手机端未登录数("+(devs.size()-devLogs.size())+")");
				array.add(obj);
			}
			System.out.println(array.toString());
			PrintWriter pw = response.getWriter();
			pw.print(array.toString());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//手机端登录统计，以logType=5成功登录为准
	public void remoteLoginCount(){
		response.setCharacterEncoding("UTF-8");
		String queryTime = Util.dealNull(request.getParameter("queryTime"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		try {
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			TblUser u = WorkUtil.getCurrUser(request);
			if (u != null) {
				StringBuffer jpql = new StringBuffer("1 =1 ");
				jpql.append(" and o.company.id =?");
				JSONArray devArray = new JSONArray();
				String jpqlStr = "";
				if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
					if (groupId == null || "".equals(groupId)) {
						groupId = u.getGroupId();
					}
					CompanyGroup group = this.groupService.getById(groupId);
					if (!group.getPid().equals("0")) {
						devArray = getArray(groupId,devArray);
						if (devArray.size() > 0) {
							jpqlStr = devArray.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
						}else {
							jpqlStr = "('')";
						}
						jpql.append(" and o.id in "+ jpqlStr);
					}
				}
				List<TblDev> devs = this.devService.getResultList(jpql.toString(), null,new Object[]{u.getCompany().getId()});
				if ("".equals(queryTime)) {
					queryTime = Util.dateToStr(new Date()).substring(0,10);
				}
				String hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=5 and o.addTime like '"+queryTime+"%' group by o.dev.id";
				if (!"".equals(jpqlStr)) {
					hql =" select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"' and o.logType=5 and o.addTime like '"+queryTime+"%' and o.dev.id in "+ jpqlStr+" group by o.dev.id";
				}
				List<DevLog> devLogs = this.devLogService.getResultList(hql);
				obj.put("value", devLogs.size());
				obj.put("name", "远程客户端已登录数("+(devLogs.size())+")");
				array.add(obj);
				obj.put("value", devs.size()-devLogs.size());
				obj.put("name", "远程客户端未登录数("+(devs.size()-devLogs.size())+")");
				array.add(obj);
			}
			System.out.println(array.toString());
			PrintWriter pw = response.getWriter();
			pw.print(array.toString());
			pw.flush();
			pw.close();
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
		String queryLogType = Util.dealNull(request.getParameter("queryLogType"));
		String queryIsp = Util.dealNull(request.getParameter("queryIsp"));
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		String queryDevNo = Util.dealNull(request.getParameter("queryDevNo"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		TblUser u = WorkUtil.getCurrUser(request);
		PageView<DevLog> pageView = new PageView<DevLog>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer("1 =1 ");
			jpql.append(" and o.dev.company.id =?");
			param.add(u.getCompany().getId());
			
			if (!"".equals(queryLogType)) {
				jpql.append(" and o.logType=?");
				param.add(Integer.parseInt(queryLogType));
			}
			if (!"".equals(queryIsp)) {
				String isp=null;
				switch (Integer.parseInt(queryIsp)) {
				case 0:
					isp = "电信";
					break;
				case 1:
					isp = "移动";
					break;
				case 2:
					isp = "联通";
					break;
				case 3:
					isp = "铁通";
					break;
				default:
					isp = null;
					break;
				}
				jpql.append(" and o.operatorName=?");
				param.add(isp);
			}
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
			if(!"".equals(queryDevNo)){
				jpql.append(" and o.dev.devNo=?");
				param.add(queryDevNo);
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
			QueryResult<DevLog> qr = this.devLogService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"logDesc\":\"无记录数据\"}]}";
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

	public DevLog getDevLog() {
		return devLog;
	}

	public void setDevLog(DevLog devLog) {
		this.devLog = devLog;
	}

}
