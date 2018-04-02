package com.sttri.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 会议资源表
 * @author thj
 *
 */
@Entity
@Table(name = "meeting_resource")
public class MeetingResource implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String comId;
	private String account;
	private transient String password;
	private String zcode;
	private Integer participants;//允许召集会议方数
	private Integer status;//status:会议状态  0-已结束 1-正进行
	private String usedAccount;
	private String usedTime;
	private String addTime;
	private String editTime;
	private String nickName;
	private Integer isMeeting;//是否正在开会  0-否 1-是
	
	public MeetingResource() {
	}
	
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
	
	@Column(name = "Account", length = 50)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "Password", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	@Column(name = "Status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "UsedAccount", length = 100)
	public String getUsedAccount() {
		return usedAccount;
	}

	public void setUsedAccount(String usedAccount) {
		this.usedAccount = usedAccount;
	}
	
	@Column(name = "UsedTime", length = 20)
	public String getUsedTime() {
		return this.usedTime;
	}

	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}
	
	@Column(name = "AddTime", length = 20)
	public String getAddTime() {
		return this.addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	@Column(name = "EditTime", length = 20)
	public String getEditTime() {
		return this.editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	
	@Column(name = "NickName", length = 50)
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	@Column(name = "IsMeeting")
	public Integer getIsMeeting() {
		return isMeeting;
	}

	public void setIsMeeting(Integer isMeeting) {
		this.isMeeting = isMeeting;
	}
}