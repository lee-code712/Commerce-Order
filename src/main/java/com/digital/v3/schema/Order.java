
package com.digital.v3.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Order {
	
	@ApiModelProperty(required = false, position = 1, notes = "주문 정보", example = "0", dataType = "object")
	private OrderSheet purchaseInfo;
	
	@ApiModelProperty(required = false, position = 2, notes = "결제 날짜", example = "yyyyMMddHHmmss", dataType = "string")
	private String purchaseDate;

	public String getPurchaseDate() {
		String purchaseDate = this.purchaseDate;
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public OrderSheet getPurchaseInfo() {
		OrderSheet purchaseInfo = this.purchaseInfo;
		return purchaseInfo;
	}

	public void setPurchaseInfo(OrderSheet purchaseInfo) {
		this.purchaseInfo = purchaseInfo;
	}

}
