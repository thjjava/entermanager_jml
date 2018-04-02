package com.sttri.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 会议列表
 * @author thj
 *
 */
@Entity
@Table(name = "meeting_record")
public class MeetingRecord implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String comId;
	private String userAccount;
	private String zcode;
	private String meetingId;
	private String meetingUuid;
	private String topic;
	private Integer participants;//参会人数
	private Integer status;//会议室状态
	private String duration;//会议时长
	private String startTime;
	private String endTime;
	private String startUrl;
	private String joinUrl;
	private String addTime;
	private String userName;
	
	
	public MeetingRecord() {}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ComId", length = 50)
	public String getComId() {
		return this.comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}
	
	@Column(name = "UserAccount", length = 50)
	public String getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	@Column(name = "Zcode", length = 20)
	public String getZcode() {
		return zcode;
	}

	public void setZcode(String zcode) {
		this.zcode = zcode;
	}

	@Column(name = "Participants")
	public Integer getParticipants() {
		return participants;
	}

	public void setParticipants(Integer participants) {
		this.participants = participants;
	}
	
	@Column(name = "Duration", length = 20)
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@Column(name = "Status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "MeetingId", length = 20)
	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	
	@Column(name = "MeetingUuid", length = 50)
	public String getMeetingUuid() {
		return meetingUuid;
	}

	public void setMeetingUuid(String meetingUuid) {
		this.meetingUuid = meetingUuid;
	}
	
	@Column(name = "Topic", length = 100)
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	@Column(name = "StartTime", length = 20)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "EndTime", length = 20)
	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "StartUrl", length = 500)
	public String getStartUrl() {
		return this.startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	
	@Column(name = "JoinUrl", length = 500)
	public String getJoinUrl() {
		return this.joinUrl;
	}

	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}
	
	@Column(name = "AddTime", length = 20)
	public String getAddTime() {
		return this.addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	@Column(name = "UserName", length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}