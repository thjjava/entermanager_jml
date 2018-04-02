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
import com.sttri.pojo.RoleMenus;
import com.sttri.pojo.TblMenus;
import com.sttri.service.IMenusService;
import com.sttri.service.IRoleMenusService;
import com.sttri.util.Util;

public class MenuAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private TblMenus menus;
	
	@Autowired
	private IMenusService menusService;
	@Autowired
	private IRoleMenusService roleMenusService;
	
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryName = Util.dealNull(request.getParameter("queryName"));
		PageView<TblMenus> pageView = new PageView<TblMenus>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if(!queryName.equals("")){
				jpql.append("and o.name like '%"+queryName+"%' ");
			}
			
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblMenus> qr = this.menusService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"name\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void getParentMenus(){
		response.setCharacterEncoding("UTF-8");
		List<TblMenus> mList = null;
		try {
			PrintWriter pw = response.getWriter();
			mList = this.menusService.getResultList(" o.pId=? ", null,new Object[]{"0"});
			if(mList==null || mList.size()==0){
				mList = new ArrayList<TblMenus>();
			}
			pw.print(new JsonView(mList));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<TblMenus> mList = null;
		try {
			PrintWriter pw = response.getWriter();
			mList = this.menusService.getResultList("1=1 ", null);
			if(mList==null || mList.size()==0){
				mList = new ArrayList<TblMenus>();
			}
			pw.print(new JsonView(mList));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String save(){
		response.setCharacterEncoding("UTF-8");
		try {
			String result = "fail";
			List<TblMenus> list = this.menusService.getResultList("o.name=? and o.pId=?", null, new Object[]{menus.getName(),menus.getpId()});
			if(list!=null && list.size()>0){
				result = "hased";
			}else{
				menus.setId(Util.getUUID(6));
				menus.setAddTime(Util.dateToStr(new Date()));
				this.menusService.save(menus);
				result = "success";
			}
			
			PrintWriter pw = response.getWriter();
			pw.print(result);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String update(){
		response.setCharacterEncoding("UTF-8");
		try {
			this.menusService.update(menus);
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
			TblMenus u = null;
			if(!id.equals("")){
				u = this.menusService.getById(id);
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
				this.menusService.deletebyids(array);
				//删除角色菜单表里该角色关系
				for (int i = 0; i < array.length; i++) {
					List<RoleMenus> rMenus = this.roleMenusService.getResultList(" o.menus.id=?", null, new Object[]{array[i]});
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
	 * 根据当前菜单的id，查询该菜单的所有子节点
	 */
	public JSONArray getArray(String roleId,String mid,JSONArray array){
		//查询组织表中，该ID的根节点下的所有子节点
		List<TblMenus> mList = this.menusService.getResultList(" o.pId=?", null, mid);
		JSONObject ob = null;
		if(mList != null && mList.size()>0){
			for (TblMenus menus : mList) {
				ob = new JSONObject();
				String cid = menus.getId();
				ob.put("id", cid);
				ob.put("name", menus.getName());
				ob.put("pId", menus.getpId());
				ob.put("url", menus.getUrl());
				if (isRoleMenued(roleId, menus.getId())) {
					ob.put("checked", true);
				}
				array.add(ob);
				getArray(roleId,cid,array);//递归查询cid该节点的子节点
			}
		}
		return array;
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
	
	public void queryMenus() {
		response.setCharacterEncoding("UTF-8");
		String roleId = Util.dealNull(request.getParameter("roleId"));
		String roleType = Util.dealNull(request.getParameter("roleType"));
		try {
			PrintWriter pw = response.getWriter();
			JSONArray array = new JSONArray();
			//查询所有父节点
			List<TblMenus> pList = this.menusService.getResultList(" o.pId=? and o.type=?", null, new Object[]{"0",Integer.parseInt(roleType)});
			if (pList == null || pList.size() <= 0 ) {
				pw.print(array.toString());
				pw.flush();
				pw.close();
				return;
			}
			JSONObject ob = null;
			for (int i = 0; i < pList.size(); i++) {
				String id = pList.get(i).getId();
				if ("0".equals(pList.get(i).getpId())) {
					ob = new JSONObject();
					ob.put("id", id);
					ob.put("name", pList.get(i).getName());
					ob.put("pId", pList.get(i).getpId());
					ob.put("url", pList.get(i).getUrl());
					if (isRoleMenued(roleId, id)) {
						ob.put("checked", true);
					}
					array.add(ob);
				}
				List<TblMenus> cList = this.menusService.getResultList(" o.pId=? and o.type=?", null, new Object[]{id,Integer.parseInt(roleType)});
				if (cList != null && cList.size() >0) {
					for (TblMenus menus : cList) {
						if(!"菜单管理".equals(menus.getName())){
							ob = new JSONObject();
							ob.put("id", menus.getId());
							ob.put("name", menus.getName());
							ob.put("pId", menus.getpId());
							ob.put("url", menus.getUrl());
							if (isRoleMenued(roleId, menus.getId())) {
								ob.put("checked", true);
							}
							array.add(ob);
							array = getArray(roleId, menus.getId(), array);
						}
					}
				}
			}
			pw.print(array.toString());
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

	public TblMenus getMenus() {
		return menus;
	}

	public void setMenus(TblMenus menus) {
		this.menus = menus;
	}

}
