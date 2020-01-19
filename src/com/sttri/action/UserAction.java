package com.sttri.action;

import java.io.IOException;
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
import com.sttri.pojo.RoleMenus;
import com.sttri.pojo.TblMenus;
import com.sttri.pojo.TblRole;
import com.sttri.pojo.TblUser;
import com.sttri.pojo.UserGroup;
import com.sttri.pojo.UserRole;
import com.sttri.service.ICompanyGroupService;
import com.sttri.service.IMenusService;
import com.sttri.service.IRoleMenusService;
import com.sttri.service.IRoleService;
import com.sttri.service.IUserGroupService;
import com.sttri.service.IUserRoleService;
import com.sttri.service.IUserService;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;


public class UserAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private TblUser user;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private ICompanyGroupService groupService;
	@Autowired
	private IUserGroupService userGroupService;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IRoleMenusService roleMenusService;
	@Autowired
	private IMenusService menusService;
	
	
	/**
	 * 根据用户的组织id，查询该组织所有子节点的用户id
	 */
	public JSONArray getArray(String id,JSONArray array){
		//查询用户组织表重，groupId为该id的所有数据
		List<UserGroup> list = this.userGroupService.getResultList(" o.groupId =?", null, id);
		if (list != null && list.size() >0 ) {
			for (UserGroup userGroup : list) {
				array.add(userGroup.getUserId());
			}
		}
		//查询组织表中，该ID的根节点下的所有子节点
		List<CompanyGroup> gList = this.groupService.getResultList(" o.pid=?", null, id);
		if(gList != null && gList.size()>0){
			for (CompanyGroup companyGroup : gList) {
				String gid = companyGroup.getId();
				List<UserGroup> ugList = this.userGroupService.getResultList(" o.groupId = ?", null, gid);
				if (ugList != null && ugList.size() >0) {
					for (UserGroup userGroup : ugList) {
						array.add(userGroup.getUserId());
					}
				}
				getArray(gid,array);//递归查询gid该节点的子节点
			}
		}
		return array;
	}
	
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryAccount = Util.dealNull(request.getParameter("queryAccount"));
		String accountType = Util.dealNull(request.getParameter("accountType"));
		String groupId = Util.dealNull(request.getParameter("groupId"));
		String meetingFlag = Util.dealNull(request.getParameter("meetingFlag"));
		
		TblUser u = WorkUtil.getCurrUser(request);
		PageView<TblUser> pageView = new PageView<TblUser>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" o.company.id='"+u.getCompany().getId()+"' ");
			if(!queryAccount.equals("")){
				jpql.append("and o.account like '%"+queryAccount+"%' ");
			}
			if (!accountType.equals("")) {
				jpql.append("and o.accountType = ? ");
				param.add(Integer.parseInt(accountType));
			}
			if (!meetingFlag.equals("")) {
				jpql.append("and o.meetingFlag = ? ");
				param.add(Integer.parseInt(meetingFlag));
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
						jpql.append(" and o.id in "+ jpqlStr);
					}
				}
			}
			/*String groupId = u.getGroupId();
			if (groupId != null && !groupId.equals("")) {
				CompanyGroup group = this.groupService.getById(groupId);
				if (!group.getPid().equals("0")) {
					array = getArray(groupId,array);
					String jpqlStr = array.toString().replace("[", "(").replace("]", ")").replaceAll("\"", "'");//返回结果为json数组，转换成sql数组
					if (array != null && !array.equals("")) {
						jpql.append(" and o.id in "+ jpqlStr);
					}
				}
			}*/
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblUser> qr = userService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"account\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<TblUser> ulist = null;
		try {
			PrintWriter pw = response.getWriter();
			ulist = userService.getResultList("1=1 ", null);
			if(ulist==null || ulist.size()==0){
				ulist = new ArrayList<TblUser>();
			}
			pw.print(new JsonView(ulist));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void save(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String result = "fail";
			List<TblUser> list = userService.getResultList("o.account=?", null, user.getAccount());
			if(list!=null && list.size()>0){
				result = "account";
			}else{
				TblUser u = WorkUtil.getCurrUser(request);
				user.setCompany(u.getCompany());
				user.setId(Util.getUUID(6));
				user.setAccountType(user.getAccountType());
				user.setAddTime(Util.dateToStr(new Date()));
				user.setPwd(WorkUtil.pwdEncrypt(user.getPwd()));
			/*	if(user.getAccountType()==1 && user.getMeetingFlag() == 1){
					String email = user.getAccount()+"@jml.com";
					JSONObject obj = MeetingApiUtil.createUser(user.getUserName(), email, MeetingUserTypeEnum.FreeUser.getValue());
					if ("100".equals(obj.getString("code"))) {
						user.setZcode(obj.getString("zcode"));
						user.setEmail(email);
					}else {
						LOG.info(obj.getString("msg"));
						pw.print("createUserFail");
						pw.flush();
						pw.close();
						return;
					}
				}*/
				this.userService.save(user, Util.dealNull(request.getParameter("groupId")));
				result = "success";
			}
			pw.print(result);
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
			TblUser u = userService.getById(user.getId());
			if(!user.getPwd().equals(u.getPwd())){
				user.setPwd(WorkUtil.pwdEncrypt(user.getPwd()));
			}
			user.setEditTime(Util.dateToStr(new Date()));
			/*if(user.getAccountType()==1 && user.getMeetingFlag() == 1){
				if ("".equals(u.getZcode()) || u.getZcode() == null || "".equals(u.getEmail()) || u.getEmail() == null) {
					String email = user.getAccount()+"@jml.com";
					JSONObject obj = MeetingApiUtil.createUser(user.getUserName(), email, MeetingUserTypeEnum.FreeUser.getValue());
					if ("100".equals(obj.getString("code"))) {
						user.setZcode(obj.getString("zcode"));
						user.setEmail(email);
					}else {
						LOG.info(obj.getString("msg"));
						pw.print("createUserFail");
						pw.flush();
						pw.close();
						return;
					}
				}
			}else if(user.getAccountType()==1 && user.getMeetingFlag() == 0){
				if (!"".equals(u.getZcode()) && u.getZcode() != null) {
					JSONObject obj = MeetingApiUtil.deleteUser(u.getZcode());
					if ("100".equals(obj.getString("code"))) {
						user.setZcode("");
						user.setEmail("");
					}else {
						LOG.info(obj.getString("msg"));
						pw.print("deleteUserFail");
						pw.flush();
						pw.close();
						return;
					}
				}
			}*/
			userService.update(user, Util.dealNull(request.getParameter("groupId")));
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
			TblUser u = null;
			if(!id.equals("")){
				u = userService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(u));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				String[] id = ids.split("_");
				TblUser user = WorkUtil.getCurrUser(request);
				if (ids.contains(user.getId())) {
					pw.print("unDelSelf");
					pw.flush();
					pw.close();
					return;
				}
				for (int i = 0; i < id.length; i++) {
					//删除组织关系
					List<UserGroup> ugList = this.userGroupService.getResultList(" o.userId=?", null, new Object[]{id[i]});
					if (ugList != null && ugList.size() >0) {
						this.userGroupService.deletebyid(ugList.get(0).getId());
					}
					//删除角色关系
					List<UserRole> urList = this.userRoleService.getResultList(" o.user.id=?", null, new Object[]{id[i]});
					if (urList != null && urList.size() >0) {
						this.userRoleService.deletebyid(urList.get(0).getId());
					}
				}
				userService.deletebyids(id);
				pw.print("success");
				pw.flush();
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改管理员密码
	 * @return
	 */
	public void modifyPwd(){
		response.setCharacterEncoding("UTF-8");
		String newPwd = Util.dealNull(request.getParameter("newPwd"));
		try {
			PrintWriter pw = response.getWriter();
			TblUser user= WorkUtil.getCurrUser(request);
			if(user !=null){
				user.setPwd(WorkUtil.pwdEncrypt(newPwd));
				this.userService.update(user);
				pw.print("success");
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//重置密码
	public void resetPwd(){
		response.setCharacterEncoding("UTF-8");
		String id = Util.dealNull(request.getParameter("id"));
		try {
			PrintWriter pw = response.getWriter();
			TblUser user = this.userService.getById(id);
			if(user != null){
				user.setPwd(WorkUtil.pwdEncrypt("123456"));
				this.userService.update(user);
				pw.print("success");
			}
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置角色
	 * @return
	 */
	public void saveRole(){
		response.setCharacterEncoding("UTF-8");
		String userId = Util.dealNull(request.getParameter("userId"));
		String roleId = Util.dealNull(request.getParameter("roleId"));
		try {
			PrintWriter pw = response.getWriter();
			TblRole role = this.roleService.getById(roleId);
			TblUser user = this.userService.getById(userId);
			if (role.getRoleType() != user.getAccountType()) {
				pw.print("false");
				pw.flush();
				pw.close();
				return;
			}
			List<UserRole> uRoles = this.userRoleService.getResultList("o.user.id=? ", null, new Object[]{userId});
			if (uRoles == null || uRoles.size() <= 0) {
				UserRole userRole = new UserRole();
				userRole.setId(Util.getUUID(6));
				userRole.setUser(user);
				userRole.setRole(role);
				this.userRoleService.save(userRole);
			}else {
				UserRole userRole = uRoles.get(0);
				if (userRole.getRole() == null) {
					userRole.setRole(role);
					this.userRoleService.update(userRole);
				}else {
					if(!roleId.equals(userRole.getRole().getId())){
						userRole.setRole(role);
						this.userRoleService.update(userRole);
					}
				}
			}
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证该aid用户下是否分配了mid菜单
	 * @param rid
	 * @param mid
	 * @return
	 */
	public boolean isRoleMenued(String rid,String mid){
		boolean flag = false;
		try {
			List<RoleMenus> rList = this.roleMenusService.getResultList("o.role.id=? and o.menus.id=?",null,new Object[]{rid,mid});
			if (rList != null && rList.size() >0) {
				flag = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag;
	}
	
	public void getMenus(){
		response.setCharacterEncoding("UTF-8");
		TblUser u = WorkUtil.getCurrUser(request);
		try {
			JSONArray array = new JSONArray();
			JSONObject fobject = null;
			JSONObject cobject = null;
			JSONObject obj = new JSONObject();
			List<UserRole> uRoles = this.userRoleService.getResultList(" o.user.id=?", null, new Object[]{u.getId()});
			if (uRoles != null && uRoles.size() >0 ) {
				String roleId = uRoles.get(0).getRole().getId();
				List<RoleMenus> rMenus = this.roleMenusService.getResultList(" o.role.id=?", null, new Object[]{roleId});
				for (RoleMenus roleMenus : rMenus) {
					if ("0".equals(roleMenus.getMenus().getpId())) {
						fobject = new JSONObject();
						fobject.put("menuid", roleMenus.getMenus().getId());
						fobject.put("icon", "icon-sys");
						fobject.put("menuname", roleMenus.getMenus().getName());
						List<TblMenus> mList = this.menusService.getResultList(" o.pId=?", null, new Object[]{roleMenus.getMenus().getId()});
						JSONArray carray = new JSONArray();
						for (int i = 0; i < mList.size(); i++) {
							String mId = mList.get(i).getId();
							if (isRoleMenued(roleMenus.getRole().getId(), mId)) {
								cobject = new JSONObject();
								cobject.put("icon", "icon-users");
								cobject.put("menuname", mList.get(i).getName());
								cobject.put("url", mList.get(i).getUrl());
								carray.add(cobject);
							}
							fobject.put("menus", carray);
						}
						array.add(fobject);
					}
				}
			}
			obj.put("menus", array);
			PrintWriter pw = response.getWriter();
			pw.print(obj.toString());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
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

	public TblUser getUser() {
		return user;
	}

	public void setUser(TblUser user) {
		this.user = user;
	}

}
