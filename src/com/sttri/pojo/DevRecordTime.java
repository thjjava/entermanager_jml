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
 * Ö±²¥Ê±³¤
 *
 */
@Entity
@Table(name = "dev_record_time")
public class DevRecordTime implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	//private String devId;
	private TblDev dev;
	private String recordStartTime;
	private String recordEndTime;
	private String timeLen;
	private Integer status;
	private String addTime;
	private String devName;
	
	public DevRecordTime() {
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
	@JoinColumn(name="devId")
	@NotFound(action=NotFoundAction.IGNORE)
	public TblDev getDev() {
		return dev;
	}

	public void setDev(TblDev dev) {
		this.dev = dev;
	}

	@Column(name = "RecordStartTime", length = 20)
	public String getRecordStartTime() {
		return this.recordStartTime;
	}

	public void setRecordStartTime(String recordStartTime) {
		this.recordStartTime = recordStartTime;
	}

	@Column(name = "RecordEndTime", length = 20)
	public String getRecordEndTime() {
		return this.recordEndTime;
	}

	public void setRecordEndTime(String recordEndTime) {
		this.recordEndTime = recordEndTime;
	}
	
	@Column(name = "TimeLen", length = 50)
	public String getTimeLen() {
		return this.timeLen;
	}

	public void setTimeLen(String timeLen) {
		this.timeLen = timeLen;
	}

	@Column(name = "Status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "AddTime", length = 20)
	public String getAddTime() {
		return this.addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	@Formula("(select d.devName from tbl_dev d where d.id = devId)")
	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

}