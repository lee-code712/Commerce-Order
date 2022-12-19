package com.digital.v3.context;

import static com.digital.v3.utils.HttpConnectionUtils.*;

import org.springframework.stereotype.Component;

import com.digital.v3.schema.Inventory;

@Component
public class InventoryContext {

	private static final String baseUrlKey = "API-G";
	
	/* 재고 검색 - productId */
	public Inventory inventorySearchById (long productId, String token) throws Exception {
		
		String responseJson = requestGET(baseUrlKey, "/rest/inventory/inquiry/byId/" + productId, token);
		Inventory resInventory = (Inventory) jsonToObject(responseJson, Inventory.class);
		
		return resInventory;
	}
	
	/* 재고 변경 */
	public void inventoryUpdate (Inventory inventory, String token) throws Exception {

		requestPOST(baseUrlKey, "/rest/inventory/update", inventory, token);
	}
	
	/* 상품에 대한 입력 수량 유효성 확인 */
	public boolean quantityCheck (long productId, long quantity, String token) throws Exception {
		
		String params = "?productId=" + productId + "&quantity=" + quantity;
		String responseJson = requestGET(baseUrlKey, "/rest/inventory/quantityCheck" + params, token);
		Inventory resInventory = (Inventory) jsonToObject(responseJson, Inventory.class);
		
		return resInventory.isValidQuantity();
	}

}
