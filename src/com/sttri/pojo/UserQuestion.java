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
 * 用户晨会自评表
 */
@Entity
@Table(name = "user_question")
public class UserQuestion implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private TblUser user;
	private TblDev dev;
	private TblQuestion question;
	private String answer;
	private String addTime;
	private String devNo;
	private String devName;
	private String questionName;
	
	public UserQuestion() {
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="UserId")
	@NotFound(action=NotFoundAction.IGNORE)
	public TblUser getUser() {
		return user;
	}

	public void setUser(TblUser user) {
		this.user = user;
	}
	
	@ManyToOne
	@JoinColumn(name="DevId")
	@NotFound(action=NotFoundAction.IGNORE)
	public TblDev getDev() {
		return dev;
	}

	public void setDev(TblDev dev) {
		this.dev = dev;
	}
	
	@ManyToOne
	@JoinColumn(name="QId")
	@NotFound(action=NotFoundAction.IGNORE)
	public TblQuestion getQuestion() {
		return question;
	}

	public void setQuestion(TblQuestion question) {
		this.question = question;
	}
	
	@Column(name = "Answer", length = 200)
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Column(name = "AddTime", length = 20)
	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	@Formula("(select d.devNo from tbl_dev d where d.id = devId)")
	public String getDevNo() {
		return devNo;
	}

	public void setDevNo(String devNo) {
		this.devNo = devNo;
	}
	
	@Formula("(select d.devName from tbl_dev d where d.id = devId)")
	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	@Formula("(select q.question from tbl_question q where q.id = qId)")
	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
}