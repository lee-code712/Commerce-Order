package com.digital.v3.sql.vo;

import lombok.Data;

@Data
public class PhoneVO {

	private long phoneId;
	private String phoneNumber;
	
	public long getPhoneId() {
		long phoneId = this.phoneId;
		return phoneId;
	}
	
	public String getPhoneNumber() {
		String phoneNumber = this.phoneNumber;
		return phoneNumber;
	}
	
}
