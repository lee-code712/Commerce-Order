package com.digital.v3.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CartProduct {
	
	@ApiModelProperty(required = true, position = 1, notes = "상품 ID", example = "0", dataType = "long")
	private long productId;
	
	@ApiModelProperty(required = true, position = 2, notes = "상품 수량", example = "0", dataType = "long")
	private long quantity;

	public long getProductId() {
		long productId = this.productId;
		return productId;
	}
	
	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getQuantity() {
		long quantity = this.quantity;
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

}
