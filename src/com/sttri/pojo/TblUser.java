package com.sttri.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * �û�
 * @author thj
 *
 */
@Entity
@Table(name = "tbl_user")
public class TblUser implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String groupId;
	private String groupName;
	private Integer accountType;
	//private String comId;
	private Company company;
	private String account;
	private transient String pwd;
	private String addTime;
	private String editTime;
	private Integer meetingFlag;
	private String zcode;
	private String email;
	private String userName;
	private Integer participants;
	
	public TblUser() {
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Formula("(select g.id from company_group g left join user_group ug on ug.groupId=g.id left join tbl_user u on u.id=ug.userId where u.id=id)")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Formula("(select g.groupName from company_group g left join user_group ug on ug.groupId=g.id left join tbl_user u on u.id=ug.userId where u.id=id)")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "AccountType")
	public Integer getAccountType() {
		return this.accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	
	@ManyToOne
	@JoinColumn(name="comId")
	@NotFound(action=NotFoundAction.IGNORE)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Column(name = "Account", length = 50)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "Pwd", length = 100)
	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name = "AddTime", length = 30)
	public String getAddTime() {
		return this.addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	@Column(name = "EditTime", length = 30)
	public String getEditTime() {
		return this.editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	
	@Column(name = "MeetingFlag")
	public Integer getMeetingFlag() {
		return meetingFlag;
	}

	public void setMeetingFlag(Integer meetingFlag) {
		this.meetingFlag = meetingFlag;
	}

	@Column(name = "Zcode", length = 20)
	public String getZcode() {
		return zcode;
	}

	public void setZcode(String zcode) {
		this.zcode = zcode;
	}
	
	@Column(name = "Email", length = 20)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "UserName", length = 100)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name = "Participants")
	public Integer getParticipants() {
		return participants;
	}

	public void setParticipants(Integer participants) {
		this.participants = participants;
	}
}