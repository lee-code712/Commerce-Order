package com.digital.v3.schema;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@ArraySchema
public class OrderList {

	@ApiModelProperty(required = false, position = 1, notes = "주문 목록", example = "", dataType = "array")
	private List<Order> orders;

	public List<Order> getOrders() {
		List<Order> orders = this.orders;
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
}
