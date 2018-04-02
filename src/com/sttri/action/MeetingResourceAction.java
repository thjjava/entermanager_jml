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
import com.sttri.pojo.MeetingResource;
import com.sttri.pojo.TblUser;
import com.sttri.pojo.UserGroup;
import com.sttri.service.ICompanyGroupService;
import com.sttri.service.IMeetingResourceService;
import com.sttri.service.IUserGroupService;
import com.sttri.util.MeetingApiUtil;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

public class MeetingResourceAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String rows;            
	private String page;
	private MeetingResource meetingResource;
	
	@Autowired
	private IMeetingResourceService meetingResourceService;
	@Autowired
	private IUserGroupService userGroupService;
	@Autowired
	private ICompanyGroupService groupService;
	
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
		PageView<MeetingResource> pageView = new PageView<MeetingResource>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			TblUser user = WorkUtil.getCurrUser(request);
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			jpql.append(" and o.comId =?");
			param.add(user.getCompany().getId());
			if(!queryAccount.equals("")){
				jpql.append("and o.account like '%"+queryAccount+"%' ");
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<MeetingResource> qr = this.meetingResourceService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
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
		List<MeetingResource> ulist = null;
		try {
			PrintWriter pw = response.getWriter();
			ulist = this.meetingResourceService.getResultList("1=1 ", null);
			if(ulist==null || ulist.size()==0){
				ulist = new ArrayList<MeetingResource>();
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
			List<MeetingResource> list = this.meetingResourceService.getResultList("o.account=?", null, meetingResource.getAccount());
			if(list!=null && list.size()>0){
				result = "account";
			}else{
				TblUser user = WorkUtil.getCurrUser(request);
				JSONObject obj = MeetingApiUtil.getUser(meetingResource.getAccount());
				if(obj.getInt("code")==100){
					meetingResource.setId(Util.getUUID(6));
					meetingResource.setComId(user.getCompany().getId());
					meetingResource.setZcode(obj.getString("zcode"));
					meetingResource.setStatus(0);
					meetingResource.setAddTime(Util.dateToStr(new Date()));
					this.meetingResourceService.save(meetingResource);
					result = "success";
					MeetingApiUtil.updateUser(obj.getString("zcode"), meetingResource.getNickName());
				}else {
					result = "accountFalse";
				}
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
			MeetingResource u = this.meetingResourceService.getById(meetingResource.getId());
			if (u != null) {
				JSONObject obj = MeetingApiUtil.updateUser(u.getZcode(), meetingResource.getNickName());
				if(obj.getInt("code") !=100){
					meetingResource.setNickName(u.getNickName());
				}
				meetingResource.setStatus(u.getStatus());
				meetingResource.setEditTime(Util.dateToStr(new Date()));
				this.meetingResourceService.update(meetingResource);
				pw.print("success");
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			MeetingResource u = null;
			if(!id.equals("")){
				u = this.meetingResourceService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(u));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				this.meetingResourceService.deletebyids(ids.split("_"));
				PrintWriter pw = response.getWriter();
				pw.print("success");
				pw.flush();
				pw.close();
			}
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
	public MeetingResource getMeetingResource() {
		return meetingResource;
	}
	public void setMeetingResource(MeetingResource meetingResource) {
		this.meetingResource = meetingResource;
	}
}
