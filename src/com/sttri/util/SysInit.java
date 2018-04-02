package com.sttri.util;


import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sttri.dao.CommonDao;
import com.sttri.pojo.MeetingRecord;
import com.sttri.pojo.MeetingResource;


public class SysInit implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {
		
	}

	public void contextInitialized(final ServletContextEvent event) {
		String cRootPath = event.getServletContext().getRealPath("/");
		if(cRootPath!=null) {
			cRootPath = cRootPath.replaceAll("\\\\", "/");
		}else {
			cRootPath = "/";
		}
		if (!cRootPath.endsWith("/")) {
			cRootPath = cRootPath + "/";
		}
		Constant.ROOTPATH = cRootPath;
		
		/**
		 * 定时执行更新会议记录状态任务
		 * 服务启动后5秒开始执行，每60秒执行一次
		 */
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("***进入会议记录状态更新线程***"+Util.dateToStr(new Date()));
				try {
					ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
					CommonDao dao = (CommonDao) ac.getBean("dao");
					List<MeetingRecord> list = dao.getResultList(MeetingRecord.class, " o.status =?", null, new Object[]{1});
					for (int i = 0; i < list.size(); i++) {
						MeetingRecord meetingRecord = list.get(i);
						String zcode = meetingRecord.getZcode();
						String meetingId = meetingRecord.getMeetingId();
						JSONObject meetingDetail = MeetingApiUtil.getMeeting(zcode, meetingId);
						int type = 1;
						//status:会议状态  0-已结束 1-正进行
						if (meetingDetail.get("code") == null && meetingDetail.getInt("status") == 0 && meetingDetail.getInt("status") != 1) {
							type=2;
							meetingRecord.setStatus(0);
							String meetingUuid = URLEncoder.encode(meetingRecord.getMeetingUuid(),"UTF-8");
							//查询会议详细信息，正在进行的会议 type=1，已结束的会议 type=2
							JSONObject obj = MeetingApiUtil.queryMeetingDetail(meetingUuid, type);
							if ("0".equals(obj.getString("Code"))) {
								JSONObject data =  obj.getJSONObject("Data");
								//更新会议记录信息
								meetingRecord.setParticipants(data.getInt("participants_count"));
								meetingRecord.setEndTime(Util.utcDateFormatter(data.getString("end_time")));
								meetingRecord.setDuration(data.getString("duration"));
							}
							dao.update(meetingRecord);
							//更新会议室资源信息
							List<MeetingResource> mList = dao.getResultList(MeetingResource.class, " o.zcode =?", null, new Object[]{zcode});
							MeetingResource meetingResource = mList.get(0);
							if (meetingResource.getIsMeeting() == 1) {
								meetingResource.setIsMeeting(0);//是否正在开会  0-否 1-是
								dao.update(meetingResource);
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 1000*5, 1000*60);
	}

}
