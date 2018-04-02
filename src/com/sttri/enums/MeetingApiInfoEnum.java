package com.sttri.enums;

/**
 * 
 * @author thj
 * 保存API的key和秘钥信息
 *
 */
public enum MeetingApiInfoEnum {
	ApiKey("api_key","13F081697AF083AE2AB78B6971A94123"),ApiSecret("api_secret","2E4D11935B5DC1C272CAD38D2B394152");
	
	private String key;
	private String value;
	
	private MeetingApiInfoEnum(String key,String value){
		this.key = key;
		this.value = value;
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
	
}
