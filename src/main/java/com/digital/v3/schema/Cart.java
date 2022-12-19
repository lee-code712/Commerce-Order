package com.digital.v3.schema;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@ArraySchema
public class Cart {

	@ApiModelProperty(required = true, position = 1, notes = "장바구니 상품 목록", example = "0", dataType = "array")
	private List<CartProduct> cart;

	public List<CartProduct> getCart() {
		List<CartProduct> cart = this.cart;
		return cart;
	}

	public void setCart(List<CartProduct> cart) {
		this.cart = cart;
	}
	
}
