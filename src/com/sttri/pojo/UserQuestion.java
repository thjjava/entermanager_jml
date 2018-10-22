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
	private String comId;
	private TblDev dev;
	private Integer answer1;
	private Integer answer2;
	private Integer answer3;
	private Integer answer4;
	private Integer answer5;
	private Integer score;
	private Integer timeLen;
	private String addTime;
	private String devNo;
	private String devName;
	private String curGroupName;//设备当前组织
	private String parentGroupName;//设备的上级组织
	private String higherGroupName;//设备上上级组织
	private Integer answer6;
	
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

	@Column(name = "ComId", length = 50)
	public String getComId() {
		return this.comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
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
	
	@Column(name = "Answer1")
	public Integer getAnswer1() {
		return answer1;
	}

	public void setAnswer1(Integer answer1) {
		this.answer1 = answer1;
	}

	@Column(name = "Answer2")
	public Integer getAnswer2() {
		return answer2;
	}

	public void setAnswer2(Integer answer2) {
		this.answer2 = answer2;
	}
	
	@Column(name = "Answer3")
	public Integer getAnswer3() {
		return answer3;
	}

	public void setAnswer3(Integer answer3) {
		this.answer3 = answer3;
	}
	
	@Column(name = "Answer4")
	public Integer getAnswer4() {
		return answer4;
	}

	public void setAnswer4(Integer answer4) {
		this.answer4 = answer4;
	}
	
	@Column(name = "Answer5")
	public Integer getAnswer5() {
		return answer5;
	}

	public void setAnswer5(Integer answer5) {
		this.answer5 = answer5;
	}
	
	@Column(name = "Score")
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@Column(name = "TimeLen")
	public Integer getTimeLen() {
		return timeLen;
	}

	public void setTimeLen(Integer timeLen) {
		this.timeLen = timeLen;
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

	@Formula("(select case when g.groupName is NULL then '无' else g.groupName end from company_group g left join tbl_dev d on d.groupId=g.id where d.id = devId)")
	public String getCurGroupName() {
		return curGroupName;
	}

	public void setCurGroupName(String curGroupName) {
		this.curGroupName = curGroupName;
	}
	
	@Formula("(select case when p.groupName is NULL then '无' else p.groupName end from company_group g LEFT JOIN company_group p on p.ID=g.Pid left join tbl_dev d on d.groupId=g.id where d.id = devId)")
	public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}
	
	@Formula("(select case when r.groupName is NULL then '无' else r.groupName end from company_group g left join company_group p on p.id=g.pId left join company_group r on r.id=p.pId left join tbl_dev d on d.groupId=g.id where d.id = devId)")
	public String getHigherGroupName() {
		return higherGroupName;
	}

	public void setHigherGroupName(String higherGroupName) {
		this.higherGroupName = higherGroupName;
	}
	
	@Column(name = "Answer6")
	public Integer getAnswer6() {
		return answer6;
	}

	public void setAnswer6(Integer answer6) {
		this.answer6 = answer6;
	}
}