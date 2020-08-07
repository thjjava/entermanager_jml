package com.sttri.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.Company;
import com.sttri.pojo.CompanyGroup;
import com.sttri.pojo.DevLog;
import com.sttri.pojo.TblDev;
import com.sttri.pojo.TblUser;
import com.sttri.service.ICompanyGroupService;
import com.sttri.service.ICompanyService;
import com.sttri.service.IDevLogService;
import com.sttri.service.IDevService;
import com.sttri.util.CreateFile;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

public class DevAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private TblDev dev;
	private File upload;

	private String uploadFileName;
	
	@Autowired
	private IDevService devService;
	@Autowired
	private ICompanyService companyService;
	@Autowired
	private ICompanyGroupService groupService;
	@Autowired
	private IDevLogService devLogService;
	
	/**
	 * 根据用户的组织id，查询该组织所有子节点的id
	 */
	public JSONArray getArray(String id,JSONArray array){
		array.add(id);
		//查询组织表中，该ID的根节点下的所有子节点
		List<CompanyGroup> gList = this.groupService.getResultList(" o.pid=?", null, id);
		if(gList != null && gList.size()>0){
			for (CompanyGroup companyGroup : gList) {
				String gid = companyGroup.getId();
				array.add(gid);
				getArray(gid,array);//递归查询gid该节点的子节点
			}
		}
		return array;
	}
	
	public void query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryDevNo = Util.dealNull(request.getParameter("queryDevNo"));
		String queryDevName = Util.dealNull(request.getParameter("queryDevName"));
		String queryIsGroup = Util.dealNull(request.getParameter("queryIsGroup"));
		String queryIsAble = Util.dealNull(request.getParameter("queryIsAble"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		String queryIsOnLine = Util.dealNull(request.getParameter("queryIsOnLine"));
		TblUser u = WorkUtil.getCurrUser(request);
		PageView<TblDev> pageView = new PageView<TblDev>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" o.company.id=? ");
			param.add(u.getCompany().getId());
			if(queryIsGroup.equals("yes"))
				jpql.append("and o.group is not null");
			else if(queryIsGroup.equals("no"))
				jpql.append("and o.group is null");
			if(!"".equals(queryDevNo)){
				jpql.append("and o.devNo like '%"+queryDevNo+"%' ");
			}
			if(!"".equals(queryDevName)){
				jpql.append("and o.devName like '%"+queryDevName+"%' ");
			}
			if (!"".equals(queryIsAble)) {
				jpql.append(" and o.isAble = ?");
				param.add(Integer.parseInt(queryIsAble));
			}
			if (!"".equals(queryIsOnLine)) {
				jpql.append(" and o.onLines = ?");
				param.add(Integer.parseInt(queryIsOnLine));
			}
			JSONArray array = new JSONArray();
			if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
				if (groupId == null || "".equals(groupId)) {
					groupId = u.getGroupId();
				}
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					array = getArray(groupId,array);
					String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					if (array != null && !array.equals("")) {
						jpql.append(" and o.group.id in "+ jpqlStr);
					}
				}
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblDev> qr = devService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
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
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<TblDev> dlist = null;
		try {
			PrintWriter pw = response.getWriter();
			dlist = devService.getResultList("1=1 ", null);
			if(dlist==null || dlist.size()==0){
				dlist = new ArrayList<TblDev>();
			}
			pw.print(new JsonView(dlist));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public void save(){
		response.setCharacterEncoding("UTF-8");
		try {
			Company company = companyService.getById(dev.getCompany().getId());
			List<TblDev> dlist = devService.getResultList("o.company.id=?", null, new Object[]{dev.getCompany().getId()});
			
			int comdevs = company.getComDevNums();
			int ds = 0;
			if(dlist!=null && dlist.size()>0)
				ds = dlist.size();

			PrintWriter pw = response.getWriter();
			String rt = "fail";
			if(comdevs>ds){
				dev.setId(Util.getUUID(6));
				dev.setOnLines(1);
				dev.setIsAble(0);
				dev.setAddTime(Util.dateToStr(new Date()));
				dev.setDevKey(WorkUtil.pwdEncrypt(dev.getDevKey()));
				dev.setFullFlag(0);
				devService.save(dev);
				rt = "success";
			}else
				rt = "devnums";
			
			pw.print(rt);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			TblDev d = devService.getById(dev.getId());
			String devKey = dev.getDevKey();
			if(!devKey.equals(d.getDevKey())){
				dev.setDevKey(WorkUtil.pwdEncrypt(dev.getDevKey()));
			}
			String devNo = dev.getDevNo();
			if (!devNo.equals(d.getDevNo())) {
				List<TblDev> devs = this.devService.getResultList(" o.devNo=?", null, new Object[]{devNo});
				if (devs != null && devs.size() >0) {
					pw.print("devNo");
					pw.flush();
					pw.close();
					return;
				}
			}
			/*String devName = dev.getDevName();
			if (!devName.equals(d.getDevName())) {
				List<TblDev> devs = this.devService.getResultList(" o.devName=?", null, new Object[]{devName});
				if (devs != null && devs.size() >0) {
					pw.print("devName");
					pw.flush();
					pw.close();
					return;
				}
			}*/
			dev.setEditTime(Util.dateToStr(new Date()));
			dev.setCompany(d.getCompany());
			devService.update(dev);
			
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			TblDev d = null;
			if(!id.equals("")){
				d = devService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(d));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				devService.deletebyids(ids.split("_"));
				PrintWriter pw = response.getWriter();
				pw.print("success");
				pw.flush();
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String isAble(){
		response.setCharacterEncoding("UTF-8");
		JSONObject obj = new JSONObject();
		obj.put("key", "fail");
		String id = Util.dealNull(request.getParameter("id"));
		int isAble = Integer.parseInt(Util.dealNull(request.getParameter("isAble")));
		TblDev dev = devService.getById(id);
		if(dev!=null){
			if(dev.getOnLines()!=0){
				dev.setIsAble(isAble);
				devService.update(dev);
				obj.put("desc", "更新成功!");
			}else{
				obj.put("desc", "当前设备在线!");
			}
		}else{
			obj.put("desc", "没有找到当前设备!");
		}
		try {
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String group(){
		response.setCharacterEncoding("UTF-8");
		devService.group(Util.dealNull(request.getParameter("ids")), Util.dealNull(request.getParameter("groupId")));
		try {
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String queryCom(){
		response.setCharacterEncoding("UTF-8");
		try {
			List<Company> cList = this.companyService.getResultList(" 1=1", null);
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(cList));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	//重置设备密码
	public void resetPwd(){
		response.setCharacterEncoding("UTF-8");
		JSONObject obj = new JSONObject();
		obj.put("key", "fail");
		String id = Util.dealNull(request.getParameter("id"));
		try {
			TblDev dev = devService.getById(id);
			if(dev!=null){
				dev.setDevKey(WorkUtil.pwdEncrypt("123456"));
				devService.update(dev);
				obj.put("desc", "更新成功!");
			}else{
				obj.put("desc", "没有找到当前设备!");
			}
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//人员上线统计
	public void queryOnLineList(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryDevNo = Util.dealNull(request.getParameter("queryDevNo"));
		String onLine = Util.dealNull(request.getParameter("onLine"));
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		TblUser u = WorkUtil.getCurrUser(request);
		PageView<TblDev> pageView = new PageView<TblDev>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" o.company.id=? ");
			param.add(u.getCompany().getId());
			if(!"".equals(queryDevNo)){
				jpql.append("and o.devNo like '%"+queryDevNo+"%' ");
			}
			/*String groupId = u.getGroupId();
			JSONArray array = new JSONArray();
			if (groupId != null && !groupId.equals("")) {
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					array = getArray(groupId,array);
					String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					if (array != null && !array.equals("")) {
						jpql.append(" and o.group.id in "+ jpqlStr);
					}
				}
			}*/
			JSONArray devArray = new JSONArray();
			if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
				if (groupId == null || "".equals(groupId)) {
					groupId = u.getGroupId();
				}
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					devArray = getArray(groupId,devArray);
					String jpqlStr = devArray.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					if (devArray != null && !devArray.equals("")) {
						jpql.append(" and o.group.id in "+ jpqlStr);
					}
				}
			}
			
			if ("".equals(onLine) || onLine == null) {
				onLine = "0";
			}
			String hql = "select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"'";
			if(!"".equals(queryDevNo)){
				hql+=" and o.dev.devNo like '%"+queryDevNo+"%' ";
			}
			if(!addTimeStart.equals("")){
				hql+=" and o.addTime >='"+addTimeStart+"'";
				if(!addTimeEnd.equals("")){
					hql+=" and o.addTime<='"+addTimeEnd+"'";
				}else{
					hql+=" and o.addTime<='"+Util.dateToStr(new Date())+"'";
				}
			}else {
				hql+=" and o.addTime like '"+Util.dateToStr(new Date()).substring(0,10)+"%'";
			}
			List<DevLog> devLogs = this.devLogService.getResultList(hql);
			String jpqlStr = "('')";
			if (devLogs != null && devLogs.size() >0) {
				for (DevLog devLog : devLogs) {
					devArray.add(devLog.getDev().getId());
				}
				jpqlStr = devArray.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
			}
			if ("0".equals(onLine)) {//上线统计
				jpql.append(" and o.id in "+ jpqlStr);
			}else {//未上线统计
				jpql.append(" and o.id not in "+ jpqlStr+" and o.company.id='"+u.getCompany().getId()+"'");
			}
			
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("lastLoginTime", "asc");
			QueryResult<TblDev> qr = devService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
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
	
	/**
	 * 根据用户的组织id，查询该组织所有子节点的设备id
	 */
	public JSONArray getDevArray(String id,JSONArray array){
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
				getDevArray(gid,array);//递归查询gid该节点的子节点
			}
		}
		return array;
	}
	
	//导出设备在线统计
	public void exportExcel(){
		response.setCharacterEncoding("UTF-8");
		String onLine = Util.dealNull(request.getParameter("onLine"));
		String queryDevNo = Util.dealNull(request.getParameter("devNo"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		String addTimeStart = Util.dealNull(request.getParameter("startTime"));
		String addTimeEnd = Util.dealNull(request.getParameter("endTime"));
		TblUser u = WorkUtil.getCurrUser(request);
		try {
			StringBuffer jpql = new StringBuffer("1 =1 ");
			jpql.append(" and o.company.id ='"+u.getCompany().getId()+"'");
			if(!"".equals(queryDevNo)){
				jpql.append("and o.devNo like '%"+queryDevNo+"%' ");
			}
			JSONArray devArray = new JSONArray();
			if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
				if (groupId == null || "".equals(groupId)) {
					groupId = u.getGroupId();
				}
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					devArray = getArray(groupId,devArray);
					String jpqlStr = devArray.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					if (devArray != null && !devArray.equals("")) {
						jpql.append(" and o.group.id in "+ jpqlStr);
					}
				}
			}
			if ("".equals(onLine) || onLine == null) {
				onLine = "0";
			}
			String hql = "select o from DevLog o where o.dev.company.id='"+u.getCompany().getId()+"'";
			if(!"".equals(queryDevNo)){
				hql+=" and o.dev.devNo like '%"+queryDevNo+"%' ";
			}
			if(!addTimeStart.equals("")){
				hql+=" and o.addTime >='"+addTimeStart+"'";
				if(!addTimeEnd.equals("")){
					hql+=" and o.addTime<='"+addTimeEnd+"'";
				}else{
					hql+=" and o.addTime<='"+Util.dateToStr(new Date())+"'";
				}
			}else {
				hql+=" and o.addTime like '"+Util.dateToStr(new Date()).substring(0,10)+"%'";
			}
			List<DevLog> devLogs = this.devLogService.getResultList(hql);
			String jpqlStr = "('')";
			if (devLogs != null && devLogs.size() >0) {
				for (DevLog devLog : devLogs) {
					devArray.add(devLog.getDev().getId());
				}
				jpqlStr = devArray.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
			}
			String fileName = "设备上线统计";//文件名
			if ("0".equals(onLine)) {//上线统计
				jpql.append(" and o.id in "+ jpqlStr);
			}else {//未上线统计
				jpql.append(" and o.id not in "+ jpqlStr+" and o.company.id='"+u.getCompany().getId()+"'");
				fileName = "设备未上线统计";//文件名
			}
			List<TblDev> devs = this.devService.getResultList(jpql.toString(), null);
			response.reset();//清除缓存
			String exportFileName = fileName+".xls";//文件名
			//设置下载文件名
			response.addHeader("Content-Disposition", "attachment;filename="+
			new String(exportFileName.getBytes("gb2312"),"iso8859-1"));
			Map<String, String> map=new LinkedHashMap<String, String>();
			map.put("higherGroupName", "上上级组织");
			map.put("parentGroupName", "上级组织");
			map.put("curGroupName", "当前组织");
			map.put("devNo", "设备账号");
			map.put("devName", "设备名称");
			map.put("lastLoginTime", "上次登录日期");
			response.setContentType("application/x-download");
        	com.sttri.util.ExcelUtil.ImportExcel(devs, response.getOutputStream(), map, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*public void exportExcel(){
		response.setCharacterEncoding("UTF-8");
		String onLine = Util.dealNull(request.getParameter("onLine"));
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
						array = getDevArray(groupId,array);
						String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
						if (array != null && !array.equals("")) {
							jpql.append(" and o.dev.id in "+ jpqlStr);
						}
					}
				}
				
				orderBy.put("addTime", "asc");
				List<DevLog> list = this.devLogService.getResultList(jpql.toString(),orderBy);
				String jpqlStr = "('')";
				if (list != null && list.size() >0) {
					for (DevLog devLog : list) {
						array.add(devLog.getDev().getId());
					}
					jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
				}
				List<TblDev> devs =  new ArrayList<TblDev>();
				String fileName = "设备上线统计";//文件名
				if ("0".equals(onLine)) {//上线统计
					devs = this.devService.getResultList(" o.id in "+jpqlStr,null);
				}else {//未上线统计
					devs = this.devService.getResultList(" o.id not in "+jpqlStr+" and o.company.id='"+u.getCompany().getId()+"'",null);
					fileName = "设备未上线统计";//文件名
				}
				response.reset();//清除缓存
				String exportFileName = fileName+".xls";//文件名
				//设置下载文件名
				response.addHeader("Content-Disposition", "attachment;filename="+
				new String(exportFileName.getBytes("gb2312"),"iso8859-1"));
				Map<String, String> map=new LinkedHashMap<String, String>();
				map.put("higherGroupName", "上上级组织");
				map.put("parentGroupName", "上级组织");
				map.put("curGroupName", "当前组织");
				map.put("devNo", "设备账号");
				map.put("devName", "设备名称");
				response.setContentType("application/x-download");
	        	com.sttri.util.ExcelUtil.ImportExcel(devs, response.getOutputStream(), map, fileName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} */
	
	/**
	 * 批量导出设备账号信息
	 */
	public void exportDevsToExcel(){
		response.setCharacterEncoding("UTF-8");
		String devNo = Util.dealNull(request.getParameter("devNo"));
		String devName = Util.dealNull(request.getParameter("devName"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		String isGroup = Util.dealNull(request.getParameter("isGroup"));
		TblUser u = WorkUtil.getCurrUser(request);
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		try {
			StringBuffer jpql = new StringBuffer(" o.company.id='"+u.getCompany().getId()+"'");
			if(isGroup.equals("yes"))
				jpql.append(" and o.group is not null");
			else if(isGroup.equals("no"))
				jpql.append(" and o.group is null");
			if(!"".equals(devNo)){
				jpql.append(" and o.devNo like '%"+devNo+"%' ");
			}
			if(!"".equals(devName)){
				jpql.append(" and o.devName like '%"+devName+"%' ");
			}
			
			JSONArray array = new JSONArray();
			if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
				if (groupId == null || "".equals(groupId)) {
					groupId = u.getGroupId();
				}
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					array = getArray(groupId,array);
					String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					if (array != null && !array.equals("")) {
						jpql.append(" and o.group.id in "+ jpqlStr);
					}
				}
			}
			orderBy.put("id", "asc");
			List<TblDev> list = this.devService.getResultList(jpql.toString(), orderBy);
			response.reset();//清除缓存
			String fileName = "设备统计.xls";//文件名
			//设置下载文件名
			response.addHeader("Content-Disposition", "attachment;filename="+
			new String(fileName.getBytes("gb2312"),"iso8859-1"));
			Map<String, String> map=new LinkedHashMap<String, String>();
			map.put("devName", "设备名称");
			map.put("devNo", "设备编号");
			response.setContentType("application/x-download");
        	com.sttri.util.ExcelUtil.ImportExcel(list, response.getOutputStream(), map, "设备统计");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量导入设备账号，更新设备信息
	 */
	public void upload(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String saveFilePath = ServletActionContext.getServletContext().getRealPath(File.separator);
			String wjml = "",key = "";
			StringBuffer sb = new StringBuffer("下面行数的设备编号更新失败：【");
			boolean createFolder = CreateFile.createFolder(saveFilePath+wjml);
			if(createFolder){
				if(upload!=null){
					String oldfiletype = Util.getExtendName(this.getUploadFileName()).toLowerCase();
					if(oldfiletype.equals(".xls")){
						wjml = "uploadFile"+File.separator+"excel"+File.separator;
						String newFileName = Util.getUUID(0)+"_"+this.getUploadFileName();
						File file = new File(saveFilePath+wjml, newFileName);
						if(file.exists())
							file.delete();
						FileUtils.copyFile(this.getUpload(), file);
						wjml += newFileName;
						if(File.separator.equals("\\") || File.separator.equals("/")){
							InputStream stream = new FileInputStream(new File(saveFilePath+wjml));
							Workbook wb=Workbook.getWorkbook(stream);
							Sheet sheet=wb.getSheet(0);
							int count=sheet.getRows();
							for(int i=1;i<count;i++) {
								Cell cell=sheet.getCell(0,i);
								String devName=cell.getContents().trim();
								cell= sheet.getCell(1,i);
								String oldDevNo=cell.getContents().trim();
								cell=sheet.getCell(2,i);
								String newDevNo=cell.getContents().trim();
								List<TblDev> dList = this.devService.getResultList(" o.devNo=?", null,new Object[]{oldDevNo});
								if (dList != null && dList.size() > 0) {
									TblDev dev = dList.get(0);
									if (!Util.isChinese(newDevNo)) {
										List<TblDev> list = this.devService.getResultList(" o.devNo=?", null,new Object[]{newDevNo});
										if (list !=null && list.size() >0 && !oldDevNo.equals(newDevNo)) {
//											dev.setDevNo(oldDevNo);
											sb.append(++i+"行,");
										}else {
											dev.setDevNo(newDevNo);
										}
									}else {
										sb.append(++i+"行,");
									}
									dev.setDevName(devName);
									dev.setEditTime(Util.dateToStr(new Date()));
									this.devService.update(dev);
								}else {
									sb.append(++i+"行,");
								}
							}
							key = "success";
						}else{
							key = "fail";
						}
					}else{
						key = "pictype";
					}
				}
			}
			sb.append("】失败原因是旧设备编号不存在或者新设备编号已存在");
			System.out.println(sb.toString());
			pw.print("{'key':'"+key+"','msg':'"+sb.toString()+"'}");
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

	public TblDev getDev() {
		return dev;
	}

	public void setDev(TblDev dev) {
		this.dev = dev;
	}
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
}
