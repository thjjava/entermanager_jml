package com.sttri.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user_group")
public class UserGroup implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String userId;
	private String groupId;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "UserId", length = 50)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "GroupId", length = 50)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}