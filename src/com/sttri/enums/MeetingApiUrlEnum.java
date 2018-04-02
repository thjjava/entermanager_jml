package com.sttri.enums;

/**
 * 
 * @author thj
 * 保存API接口地址
 */
public enum MeetingApiUrlEnum {
	createUserUrl("url","https://api.zhumu.me/v3/user/create","创建用户接口地址"),
	deleteUserUrl("url","https://api.zhumu.me/v3/user/delete","删除用户接口地址"),
	getUserUrl("url","https://api.zhumu.me/v3/user/get","获取用户信息接口地址"),
	createMeetingUrl("url","https://api.zhumu.me/v3/meeting/create","创建会议接口地址"),
	endMeetingUrl("url","https://api.zhumu.me/v3/meeting/end","结束会议接口地址"),
	deleteMeetingUrl("url","https://api.zhumu.me/v3/meeting/delete","删除会议接口地址"),
	getMeetingUrl("url","https://api.zhumu.me/v3/meeting/get","查询会议信息接口地址"),
	queryRegularMeetingList("url","https://api.zhumu.me/v3/meeting/list","查询预约会议记录接口地址"),
	queryMeetingListUrl("url","https://api.zhumu.me/v3/meeting/metrics","查询用户下所有会议接口地址"),
	queryMeetingInfoUrl("url","https://api.zhumu.me/v3/meeting/metricsdetail","查询用户下某个会议详细内容接口地址"),
	updateUserUrl("url","https://api.zhumu.me/v3/user/update","修改用户信息接口地址");
	
	private String key;
	private String value;
	private String desc;
	
	private MeetingApiUrlEnum(String key,String value,String desc){
		this.key = key;
		this.value = value;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
