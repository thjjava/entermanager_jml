package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.RoleMenus;
import com.sttri.pojo.TblMenus;
import com.sttri.pojo.TblRole;
import com.sttri.pojo.TblUser;
import com.sttri.pojo.UserRole;
import com.sttri.service.IMenusService;
import com.sttri.service.IRoleMenusService;
import com.sttri.service.IRoleService;
import com.sttri.service.IUserRoleService;
import com.sttri.service.IUserService;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

public class RoleAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private TblRole role;
	
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IRoleMenusService roleMenusService;
	@Autowired
	private IMenusService menusService;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IUserService userService;
	
	
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryRoleName = Util.dealNull(request.getParameter("queryRoleName"));
		TblUser u = WorkUtil.getCurrUser(request);
		PageView<TblRole> pageView = new PageView<TblRole>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 and (o.comId is null or o.comId ='"+u.getCompany().getId()+"') ");
			if(!queryRoleName.equals("")){
				jpql.append("and o.roleName like '%"+queryRoleName+"%' ");
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblRole> qr = this.roleService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"roleName\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void getList(){
		response.setCharacterEncoding("UTF-8");
		String userId = Util.dealNull(request.getParameter("userId"));
		List<TblRole> roles = null;
		try {
			PrintWriter pw = response.getWriter();
			TblUser user = this.userService.getById(userId);
			roles = this.roleService.getResultList("1=1 and o.roleType="+user.getAccountType()+" and (o.comId is null or o.comId ='"+user.getCompany().getId()+"')",null);
			if(roles==null || roles.size()==0){
				roles = new ArrayList<TblRole>();
			}
			pw.print(new JsonView(roles));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getList1(){
		response.setCharacterEncoding("UTF-8");
		String userId = Util.dealNull(request.getParameter("userId"));
		List<TblRole> roles = null;
		try {
			PrintWriter pw = response.getWriter();
			TblUser user = this.userService.getById(userId);
			roles = this.roleService.getResultList("1=1 and o.roleType="+user.getAccountType()+" and (o.comId is null or o.comId ='"+user.getCompany().getId()+"')",null);
			StringBuffer sb = new StringBuffer();
			if (roles !=null && roles.size() >0) {
				for (TblRole tblRole : roles) {
					List<UserRole> urList = this.userRoleService.getResultList("o.user.id=? and o.role.id=? ", null, new Object[]{userId,tblRole.getId()});
					if (urList !=null && urList.size() >0) {
						sb.append("<option selected ='selected' value='"+tblRole.getId()+"'>"+tblRole.getRoleName()+"</option>");
					}else {
						sb.append("<option value='"+tblRole.getId()+"'>"+tblRole.getRoleName()+"</option>");
					}
				}
			}
			pw.print(sb.toString());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(){
		response.setCharacterEncoding("UTF-8");
		TblUser u = WorkUtil.getCurrUser(request);
		try {
			String result = "fail";
			List<TblRole> list = this.roleService.getResultList("o.roleName=?", null, role.getRoleName());
			if(list!=null && list.size()>0){
				result = "account";
			}else{
				role.setId(Util.getUUID(6));
				role.setAddTime(Util.dateToStr(new Date()));
				role.setComId(u.getCompany().getId());
				this.roleService.save(role);
				result = "success";
			}
			
			PrintWriter pw = response.getWriter();
			pw.print(result);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String update(){
		response.setCharacterEncoding("UTF-8");
		try {
			this.roleService.update(role);
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			TblRole u = null;
			if(!id.equals("")){
				u = this.roleService.getById(id);
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
	
	public String deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				String[] array = ids.split("_");
				this.roleService.deletebyids(array);
				//删除用户角色表里该角色关系
				for (int i = 0; i < array.length; i++) {
					List<UserRole> uRoles = this.userRoleService.getResultList(" o.role.id=?", null, new Object[]{array[i]});
					for (UserRole userRole : uRoles) {
						this.userRoleService.deletebyid(userRole.getId());
					}
				}
				//删除角色菜单表里该角色关系
				for (int i = 0; i < array.length; i++) {
					List<RoleMenus> rMenus = this.roleMenusService.getResultList(" o.role.id=?", null, new Object[]{array[i]});
					for (RoleMenus roleMenus : rMenus) {
						this.roleMenusService.deletebyid(roleMenus.getId());
					}
				}
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
	
	public void setPermisson(){
		response.setCharacterEncoding("UTF-8");
		String roleId = Util.dealNull(request.getParameter("roleId"));
		String mIds = Util.dealNull(request.getParameter("menus"));
		try {
			PrintWriter pw = response.getWriter();
			boolean flag = false;
			if ("".equals(mIds)) {
				List<RoleMenus> rmList = this.roleMenusService.getResultList(" o.role.id=?", null, new Object[]{roleId});
				if (rmList != null && rmList.size() >0) {
					for (RoleMenus roleMenus : rmList) {
						this.roleMenusService.deletebyid(roleMenus.getId());
					}
					flag = true;
				}
			}else {
				String[] array = mIds.split(",");
				for (int i = 0; i < array.length; i++) {
					if (!isRoleMenued(roleId, array[i])) {
						TblRole role = this.roleService.getById(roleId);
						TblMenus menus = this.menusService.getById(array[i]);
						RoleMenus roleMenus = new RoleMenus();
						roleMenus.setId(Util.getUUID(6));
						roleMenus.setRole(role);
						roleMenus.setMenus(menus);
						this.roleMenusService.save(roleMenus);
					}else {
						List<RoleMenus> list = this.roleMenusService.getResultList("o.role.id=?", null, new Object[]{roleId});
						for (int j = 0; j < list.size(); j++) {
							if (!array[i].equals(list.get(j).getMenus().getId())) {
								this.roleMenusService.deletebyid(list.get(j).getId());
							}
						}
					}
				}
				flag = true;
			}
			if (flag) {
				pw.print("success");
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void getUserRole(){
		response.setCharacterEncoding("UTF-8");
		String userId = Util.dealNull(request.getParameter("userId"));
		try {
			PrintWriter pw = response.getWriter();
			List<UserRole> urList = this.userRoleService.getResultList("o.user.id=? ", null, new Object[]{userId});
			if (urList != null && urList.size() >0) {
				TblRole role = urList.get(0).getRole();
				pw.print(new JsonView(role));
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

	public TblRole getRole() {
		return role;
	}

	public void setRole(TblRole role) {
		this.role = role;
	}

}
