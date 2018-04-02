package com.sttri.action;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.MeetingRecord;
import com.sttri.pojo.TblUser;
import com.sttri.service.IMeetingRecordService;
import com.sttri.util.MeetingApiUtil;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MeetingAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String rows;            
	private String page;
	private MeetingRecord meetingRecord;
	
	@Autowired
	private IMeetingRecordService meetingRecordService;
	
	
	/*
	 * 注释日期：2018-1-15 
	 * 注释原因：查询机制从接口查询改成从数据库查询
	 * public void query(){
		response.setCharacterEncoding("UTF-8");
		String startTime = Util.dealNull(request.getParameter("addTimeStart"));
		String endTime = Util.dealNull(request.getParameter("addTimeEnd"));
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		if ("".equals(startTime) && "".equals(endTime)) {
			String now = Util.dateToStr(new Date());
			startTime = now.substring(0,8)+"01";
			endTime = now.substring(0,10);
		}
		try {
			PrintWriter pw = response.getWriter();
			JSONObject obj = MeetingApiUtil.queryMeetingList(2, startTime, endTime);
			JSONObject ob = obj.getJSONObject("Data");
			if(ob.getInt("total_records") ==0){
				String json = "{\"total\":1,\"rows\":[{\"host\":\"无记录数据\"}]}";
				pw.print(json);
			}else {
				JSONArray array = ob.getJSONArray(("meetings"));
				List<MeetingVo> list = JSONArray.toList(array);
				QueryResult<MeetingVo> qr = new QueryResult<MeetingVo>();
				int firstIndex = (pages > 1 ? (pages -1) * row : 0);
				int toIndex = (pages*row > list.size()?list.size():pages*row);
				if (firstIndex > toIndex) {
					pages =1;
					firstIndex =0;
				}
				list = list.subList(firstIndex, toIndex);
				qr.setResultlist(list);
				qr.setTotalRecord(ob.getLong("total_records"));
				PageView<MeetingVo> pageView = new PageView<MeetingVo>(row, pages);
				pageView.setQueryResult(qr);
				pageView.setRecords(list);
				pageView.setTotalRecord(qr.getTotalRecord());
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(JSONObject.fromObject((qu)));
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}*/
	
	public void query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		String userAccount = Util.dealNull(request.getParameter("userAccount"));
		String userName = Util.dealNull(request.getParameter("userName"));
		PageView<MeetingRecord> pageView = new PageView<MeetingRecord>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			TblUser user = WorkUtil.getCurrUser(request);
			StringBuffer jpql = new StringBuffer("1 =1 ");
			jpql.append(" and o.comId=? ");
			param.add(user.getCompany().getId());
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
			if (!"".equals(userAccount)) {
				jpql.append(" and o.userAccount like ?");
				param.add("'%"+userAccount+"%'");
			}
			if (!"".equals(userName)) {
				jpql.append(" and o.userName like ?");
				param.add("'%"+userName+"%'");
			}
			/*JSONArray array = new JSONArray();
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
			}*/
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("addTime", "desc");
			QueryResult<MeetingRecord> qr = this.meetingRecordService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"userAccount\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过会议UUID获取详细内容
	 */
	public void getMeetingInfo(){
		response.setCharacterEncoding("UTF-8");
		String meetingId = Util.dealNull(request.getParameter("meetingId"));
		try {
			PrintWriter pw = response.getWriter();
			List<MeetingRecord> mList = this.meetingRecordService.getResultList(" o.meetingId=?", null, meetingId);
			MeetingRecord meetingRecord = new MeetingRecord();
			if (mList != null && mList.size() >0) {
				meetingRecord = mList.get(0);
			}
			/*String mStr = meetingUUId.substring(0,meetingUUId.indexOf("=="));
			//判断meetingId中是否含有特殊字符，如果含有，就进行转码
			if (Util.isSpecialChar(mStr)) {
				meetingUUId = URLEncoder.encode(meetingUUId,"UTF-8");
			}*/
			String meetingUUId = URLEncoder.encode(meetingRecord.getMeetingUuid(),"UTF-8");
			JSONObject meetingDetail = MeetingApiUtil.getMeeting(meetingRecord.getZcode(), meetingRecord.getMeetingId());
			if (meetingDetail.get("code") != null) {
				String json = "{\"total\":1,\"rows\":[{\"user_name\":\"无记录数据\"}]}";
				pw.print(json);
				pw.flush();
				pw.close();
				return;
			}
			int status = meetingDetail.getInt("status");
			int type =1;
			if (status  == 0) {
				type = 2;
			}
			JSONObject obj = MeetingApiUtil.queryMeetingDetail(meetingUUId, type);
			if (obj.getInt("Code") == 0) {
				JSONObject ob = obj.getJSONObject("Data");
				//更新数据库中该会议记录的信息
				if (status == 0) {
					meetingRecord.setStatus(status);
					meetingRecord.setParticipants(ob.getInt("participants_count"));
					meetingRecord.setEndTime(Util.utcDateFormatter(ob.getString("end_time")));
					meetingRecord.setDuration(ob.getString("duration"));
					this.meetingRecordService.update(meetingRecord);
				}
				if (ob.getInt("participants_count") >0) {
					JSONArray participants = ob.getJSONArray(("participants"));
					QueryJSON qu = new QueryJSON();
					qu.setRows(participants);
					qu.setTotal(ob.getInt("participants_count"));
					pw.print(JSONObject.fromObject((qu)));
				}else {
					String json = "{\"total\":1,\"rows\":[{\"user_name\":\"无记录数据\"}]}";
					pw.print(json);
				}
			}else {
				String json = "{\"total\":1,\"rows\":[{\"user_name\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void deletebyids(){
		response.setCharacterEncoding("UTF-8");
		String ids = Util.dealNull(request.getParameter("ids"));
		try {
			PrintWriter pw = response.getWriter();
			if(!"".equals(ids) && null!=ids){
				String[] array = ids.split("_");
				//删除用户角色表里该角色关系
				for (int i = 0; i < array.length; i++) {
					String id = array[i];
					MeetingRecord record = this.meetingRecordService.getById(id);
					String meetingId = record.getMeetingId();
					String zcode = record.getZcode();
					MeetingApiUtil.deleteMeeting(zcode, meetingId);//调用接口进行删除
					/*if (obj.get("code") == null) {
						this.meetingRecordService.deletebyid(id);
						pw.print("success");
						pw.flush();
						pw.close();
					}*/
					this.meetingRecordService.deletebyid(id);//删除数据库中的记录
					pw.print("success");
					pw.flush();
					pw.close();
				}
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

	public MeetingRecord getMeetingRecord() {
		return meetingRecord;
	}

	public void setMeetingRecord(MeetingRecord meetingRecord) {
		this.meetingRecord = meetingRecord;
	}

}
