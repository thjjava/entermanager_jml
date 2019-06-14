package com.sttri.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * �������������
 */
@Entity
@Table(name = "tbl_question")
public class TblQuestion implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String comId;
	private String question;
	private String answerNo;
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

	@Column(name = "ComId", length = 50)
	public String getComId() {
		return this.comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}
	
	@Column(name = "Question", length = 200)
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	@Column(name = "AnswerNo", length = 20)
	public String getAnswerNo() {
		return answerNo;
	}

	public void setAnswerNo(String answerNo) {
		this.answerNo = answerNo;
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