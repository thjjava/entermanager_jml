package com.sttri.enums;

/**
 * 
 * @author thj
 * 用户类型： 0-免费用户  1-普通用户  2-高级用户  
 */
public enum MeetingUserTypeEnum {
	FreeUser(0,"免费用户"),AverageUser(1,"普通用户"),PowerUser(2,"高级用户");
	
	private Integer value;
	private String desc;
	
	private MeetingUserTypeEnum(Integer value,String desc){
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
