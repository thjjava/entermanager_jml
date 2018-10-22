package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.CompanyGroup;
import com.sttri.pojo.TblDev;
import com.sttri.pojo.TblUser;
import com.sttri.pojo.UserGroup;
import com.sttri.service.ICompanyGroupService;
import com.sttri.service.IDevService;
import com.sttri.service.IUserGroupService;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

public class CompanyGroupAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private CompanyGroup group;
	
	@Autowired
	private ICompanyGroupService groupService;
	@Autowired
	private IDevService devService;
	@Autowired
	private IUserGroupService userGroupService;
	
	
	/**
	 * 根据用户的组织id，查询该组织所有子节点的组织id
	 */
	public JSONArray getGroupIdArray(String id,JSONArray array){
		//查询组织表中，该ID的根节点下的所有子节点
		List<CompanyGroup> gList = this.groupService.getResultList(" o.pid=?", null, id);
		if(gList != null && gList.size()>0){
			for (CompanyGroup companyGroup : gList) {
				String gid = companyGroup.getId();
				array.add(gid);
				getGroupIdArray(gid,array);//递归查询gid该节点的子节点
			}
		}
		return array;
	}
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows);
		String queryGroupName = Util.dealNull(request.getParameter("queryGroupName"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		PageView<CompanyGroup> pageView = new PageView<CompanyGroup>(row, pages);
		List<Object> param = new ArrayList<Object>();
		TblUser u = WorkUtil.getCurrUser(request);
		try {
			StringBuffer jpql = new StringBuffer("o.comId=?");
			param.add(u.getCompany().getId());
			if (!"".equals(queryGroupName)) {
				jpql.append(" and o.groupName like ?");
				param.add('%' + queryGroupName + '%');
			}
			
			JSONArray array = new JSONArray();
			if (u.getGroupId() != null || (groupId != null && !"".equals(groupId))) {
				if (groupId == null || "".equals(groupId)) {
					groupId = u.getGroupId();
				}
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					array = getGroupIdArray(groupId,array);
					String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");
					if (array != null && !array.equals("")) {
						jpql.append(" and o.id in "+ jpqlStr);
					}
				}
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<CompanyGroup> qr = groupService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"groupName\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据用户的组织id，查询该组织所有子节点的组织id
	 */
	public JSONArray getArray(String id,JSONArray array){
		//查询组织表中，该ID的根节点下的所有子节点
		List<CompanyGroup> gList = this.groupService.getResultList(" o.pid=?", null, id);
		JSONObject ob = null;
		if(gList != null && gList.size()>0){
			for (CompanyGroup companyGroup : gList) {
				ob = new JSONObject();
				String gid = companyGroup.getId();
				ob.put("id", gid);
				ob.put("name", companyGroup.getGroupName());
				ob.put("pId", companyGroup.getPid());
				array.add(ob);
				getArray(gid,array);//递归查询gid该节点的子节点
			}
		}
		return array;
	}
	
	/**
	 * 根据企业编号获取该企业下的所有组织
	 * @param comId
	 * @param array
	 * @return
	 */
	public JSONArray getGroupArray(String comId,JSONArray array){
		JSONObject obj = null;
		List<CompanyGroup> glist = groupService.getResultList("o.comId=?", null, comId);
		for(CompanyGroup g:glist){
			obj = new JSONObject();
			obj.put("id", g.getId());
			obj.put("name", g.getGroupName());
			obj.put("pId", g.getPid());
			array.add(obj);
		}
		return array;
	}
	
	public void getTree(){
		response.setCharacterEncoding("UTF-8");
		TblUser u = WorkUtil.getCurrUser(request);
		try {
			PrintWriter pw = response.getWriter();
			String groupId = u.getGroupId();
			JSONArray array = new JSONArray();
			if (groupId == null || "".equals(groupId)) {
				array = getGroupArray(u.getCompany().getId(), array);
			}else {
				CompanyGroup group = this.groupService.getById(groupId);
				if ("0".equals(group.getPid())) {
					array = getGroupArray(u.getCompany().getId(), array);
				}else {
					JSONObject obj = new JSONObject();
					obj.put("id", group.getId());
					obj.put("name", group.getGroupName());
					obj.put("pId", group.getPid());
					array.add(obj);
					array = getArray(groupId,array);
				}
			}
			pw.print(array.toString());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<CompanyGroup> glist = null;
		TblUser u = WorkUtil.getCurrUser(request);
		try {
			PrintWriter pw = response.getWriter();
			glist =groupService.getResultList("1=1 and o.comId=?", null, u.getCompany().getId());
			if(glist==null || glist.size()==0){
				glist = new ArrayList<CompanyGroup>();
			}
			pw.print(new JsonView(glist));
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
			PrintWriter pw = response.getWriter();
			/*String groupName = group.getGroupName();
			List<CompanyGroup> groups = this.groupService.getResultList(" o.groupName=?", null, new Object[]{groupName});
			if (groups != null && groups.size() >0) {
				pw.print("hased");
				pw.flush();
				pw.close();
				return;
			}*/
			TblUser u = WorkUtil.getCurrUser(request);
			group.setComId(u.getCompany().getId());
			group.setId(Util.getUUID(6));
			group.setAddTime(Util.dateToStr(new Date()));
			if ("".equals(group.getPid())) {
				if(u.getGroupId() == null || u.getGroupId().equals("")){
					group.setPid("0");
				}else {
					group.setPid(u.getGroupId());
				}
			}
			groupService.save(group);
			
			pw.print("success");
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
			TblUser u = WorkUtil.getCurrUser(request);
			/*CompanyGroup g = this.groupService.getById(group.getId());
			String groupName = group.getGroupName();
			if (!groupName.equals(g.getGroupName())) {
				List<CompanyGroup> groups = this.groupService.getResultList(" o.groupName=?", null, new Object[]{groupName});
				if (groups != null && groups.size() >0) {
					pw.print("hased");
					pw.flush();
					pw.close();
					return;
				}
			}*/
			group.setComId(u.getCompany().getId());
			group.setEditTime(Util.dateToStr(new Date()));
			groupService.update(group);
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
			CompanyGroup g = null;
			if(!id.equals("")){
				g = groupService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(g));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deletebyids(){
		response.setCharacterEncoding("UTF-8");
		String ids = Util.dealNull(request.getParameter("ids"));
		try {
			PrintWriter pw = response.getWriter();
			if(!"".equals(ids) && null!=ids){
				String[] id = ids.split("_");
				//先删除设备表组织关系和用户组织表的关系
				for (int i = 0; i < id.length; i++) {
					List<TblDev> dList = this.devService.getResultList("o.group.id=?", null, new Object[]{id[i]});
					if (dList != null && dList.size() >0) {
						pw.print("hasDev");
						pw.flush();
						pw.close();
						return;
					}
					/*for (TblDev tblDev : dList) {
						tblDev.setGroup(null);
						this.devService.update(tblDev);
					}*/
					List<UserGroup> ugList = this.userGroupService.getResultList("o.groupId=?", null, new Object[]{id[i]});
					if (ugList != null && ugList.size() >0) {
						pw.print("hasUser");
						pw.flush();
						pw.close();
						return;
					}
					/*for (UserGroup userGroup : ugList) {
						userGroup.setGroupId("");
						this.userGroupService.update(userGroup);
					}*/
					//更新子组织的父组织关系，将父组织改为该组织的父组织
					CompanyGroup companyGroup = this.groupService.getById(id[i]);
					List<CompanyGroup> cgList = this.groupService.getResultList(" o.pid=?", null, new Object[]{id[i]});
					for (CompanyGroup childGroup : cgList) {
						childGroup.setPid(companyGroup.getPid());
						childGroup.setParentName(companyGroup.getParentName());
						this.groupService.update(childGroup);
					}
				}
				this.groupService.deletebyids(id);
				pw.print("success");
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

	public CompanyGroup getGroup() {
		return group;
	}

	public void setGroup(CompanyGroup group) {
		this.group = group;
	}

}
