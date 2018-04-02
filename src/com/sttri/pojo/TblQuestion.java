package com.sttri.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 晨会自评问题表
 */
@Entity
@Table(name = "tbl_question")
public class TblQuestion implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String question;
	private String answer;
	private Integer status;
	private Integer type;
	private String editUser;
	private String addTime;
	private String editTime;
	
	public TblQuestion() {
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "Question", length = 200)
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	@Column(name = "Answer", length = 200)
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Column(name = "Status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "Type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name = "EditUser", length = 50)
	public String getEditUser() {
		return editUser;
	}

	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}
	
	@Column(name = "AddTime", length = 20)
	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	@Column(name = "EditTime", length = 20)
	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

}