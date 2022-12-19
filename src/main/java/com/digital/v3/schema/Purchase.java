package com.digital.v3.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Purchase {

	@ApiModelProperty(required = true, position = 1, notes = "주문서 ID", example = "0", dataType = "long")
	private long orderSheetId;
	
	@ApiModelProperty(required = false, position = 2, notes = "결제 날짜", example = "yyyy-MM-dd HH:mm:ss", dataType = "string")
	private String purchaseDate;

	public long getOrderSheetId() {
		long orderSheetId = this.orderSheetId;
		return orderSheetId;
	}

	public void setOrderSheetId(long orderSheetId) {
		this.orderSheetId = orderSheetId;
	}

	public String getPurchaseDate() {
		String purchaseDate = this.purchaseDate;
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

}
