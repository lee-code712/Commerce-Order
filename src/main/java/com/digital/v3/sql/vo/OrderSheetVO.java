package com.digital.v3.sql.vo;

import java.util.List;

import lombok.Data;

@Data
public class OrderSheetVO {

	private long orderId;
	private long personId;
	private AddressVO addressVo;
	private PhoneVO phoneVo;
	private List<PartyProductVO> partyProductVoList;
	private String purchaseDate;
	
	public long getOrderId() {
		long orderId = this.orderId;
		return orderId;
	}
	
	public long getPersonId() {
		long personId = this.personId;
		return personId;
	}
	
	public AddressVO getAddressVo() {
		AddressVO addressVo = this.addressVo;
		return addressVo;
	}
	
	public PhoneVO getPhoneVo() {
		PhoneVO phoneVo = this.phoneVo;
		return phoneVo;
	}
	
	public List<PartyProductVO> getPartyProductVoList() {
		List<PartyProductVO> partyProductVoList = this.partyProductVoList;
		return partyProductVoList;
	}
	
	public String getPurchaseDate() {
		String purchaseDate = this.purchaseDate;
		return purchaseDate;
	}
	
}
