package com.sttri.bean;

public class MeetingVo {
	private String uuid;
	private String id;
	private String host;
	private String topic;
	private String email;
	private String user_type;
	private String start_time;
	private String end_time;
	private String duration;
	private String participants;//参与者
	private boolean has_pstn;
	private boolean has_voip;
	private boolean has_3rd_party_audio;
	private boolean has_video;
	private boolean has_screen_share;
	private String recording;
	private String error;
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getParticipants() {
		return participants;
	}
	public void setParticipants(String participants) {
		this.participants = participants;
	}
	public boolean isHas_pstn() {
		return has_pstn;
	}
	public void setHas_pstn(boolean has_pstn) {
		this.has_pstn = has_pstn;
	}
	public boolean isHas_voip() {
		return has_voip;
	}
	public void setHas_voip(boolean has_voip) {
		this.has_voip = has_voip;
	}
	public boolean isHas_3rd_party_audio() {
		return has_3rd_party_audio;
	}
	public void setHas_3rd_party_audio(boolean has_3rd_party_audio) {
		this.has_3rd_party_audio = has_3rd_party_audio;
	}
	public boolean isHas_video() {
		return has_video;
	}
	public void setHas_video(boolean has_video) {
		this.has_video = has_video;
	}
	public boolean isHas_screen_share() {
		return has_screen_share;
	}
	public void setHas_screen_share(boolean has_screen_share) {
		this.has_screen_share = has_screen_share;
	}
	public String getRecording() {
		return recording;
	}
	public void setRecording(String recording) {
		this.recording = recording;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
