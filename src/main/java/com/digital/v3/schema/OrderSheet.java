package com.digital.v3.schema;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OrderSheet {
	
	@ApiModelProperty(required = false, position = 1, notes = "주문서 ID", example = "0", dataType = "long")
	private long orderSheetId;

	@ApiModelProperty(required = false, position = 2, notes = "회원 ID", example = "0", dataType = "long")
	private long personId;
	
	@ApiModelProperty(required = true, position = 3, notes = "전화번호 ID", example = "0", dataType = "long")
	private long phoneId;
	
	@ApiModelProperty(required = true, position = 4, notes = "주소 ID", example = "0", dataType = "long")
	private long addressId;
	
	@ApiModelProperty(required = true, position = 5, notes = "주문 상품 목록", example = "", dataType = "array")
	private List<CartProduct> products;
	
	public long getOrderSheetId() {
		long orderSheetId = this.orderSheetId;
		return orderSheetId;
	}

	public void setOrderSheetId(long orderSheetId) {
		this.orderSheetId = orderSheetId;
	}

	public long getPersonId() {
		long personId = this.personId;
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public long getPhoneId() {
		long phoneId = this.phoneId;
		return phoneId;
	}

	public void setPhoneId(long phoneId) {
		this.phoneId = phoneId;
	}

	public long getAddressId() {
		long addressId = this.addressId;
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	
	public List<CartProduct> getProducts() {
		List<CartProduct> products = this.products;
		return products;
	}

	public void setProducts(List<CartProduct> products) {
		this.products = products;
	}

}
