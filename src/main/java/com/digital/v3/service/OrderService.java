package com.digital.v3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.digital.v3.context.InventoryContext;
import com.digital.v3.schema.CartProduct;
import com.digital.v3.schema.Inventory;
import com.digital.v3.schema.Order;
import com.digital.v3.schema.OrderList;
import com.digital.v3.schema.OrderSheet;
import com.digital.v3.schema.Purchase;
import com.digital.v3.sql.mapper.OrderMapper;
import com.digital.v3.sql.vo.AddressVO;
import com.digital.v3.sql.vo.OrderSheetVO;
import com.digital.v3.sql.vo.PartyProductVO;
import com.digital.v3.sql.vo.PhoneVO;

@Component
public class OrderService {

	@Autowired
	OrderMapper orderMapper;
	@Autowired
	InventoryContext inventoryContext;

	/* 주문서 등록 */
	@Transactional
	public boolean orderSheetWrite (OrderSheet orderSheet, String token) throws Exception {
		try {
			// 주문서 중복 여부 확인
			if (orderMapper.getOrderSheetByPerson(orderSheet.getPersonId()) != null) {
				throw new Exception("이미 등록된 가주문서가 존재합니다.");
			}
			
			// 중복이 아니면 상품들의 구매 수량 유효성 검사 - 중복 상품들의 입력 수량을 더한 값으로 계산
			String errorMsg = "아래 상품들의 재고 수량이 부족합니다.\n";
			boolean exceptionFlag = false;
			
			Map<Long, Long> productMap = new HashMap<Long, Long>();
			for (CartProduct product : orderSheet.getProducts()) {
				long productId = product.getProductId();
				long quantity = product.getQuantity();
				
				if (productMap.get(productId) == null) {
					productMap.put(productId, quantity);
				} else {
					productMap.put(productId, quantity + productMap.get(productId));
				}
				
				if (!inventoryContext.quantityCheck(productId, productMap.get(productId), token)) {
					errorMsg += "상품 ID: " + productId + "\n";
					if (!exceptionFlag) {
						exceptionFlag = true;
					}
				}
			}	
			
			if (exceptionFlag) {
				throw new Exception(errorMsg);
			}
			
			// 모든 상품의 구매 수량이 유효하면 orderSheet write
			orderSheet.setOrderSheetId(System.currentTimeMillis());
			OrderSheetVO orderSheetVO = setOrderSheetVO(orderSheet);
			
			orderMapper.createOrderSheet(orderSheetVO);
			
			// order product orderId update
			for (CartProduct product : orderSheet.getProducts()) {
				PartyProductVO orderProductVo = 
						setOrderProductVO(orderSheetVO.getPersonId(), orderSheetVO.getOrderId(), product);
				orderMapper.updateOrderIdOfOrderProduct(orderProductVo);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/* 주문서 삭제 */
	public boolean orderSheetDelete (long personId) throws Exception {
		try {
			// order sheet 존재 여부 확인
			if (orderMapper.getOrderSheetByPerson(personId) == null) {
				throw new Exception("현재 등록된 가주문서가 없습니다.");
			}
			
			// 존재하면 delete
			orderMapper.deleteOrderSheet(personId);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/* 결제 */
	@Transactional(rollbackFor = Exception.class)
	public boolean purchase (Purchase purchase, String token) throws Exception {
		try {
			OrderSheet orderSheet = orderSheetSearchById(purchase.getOrderSheetId());
			
			// 주문서 존재 여부 확인
			if (orderMapper.getOrderSheetById(purchase.getOrderSheetId()) == null) {
				throw new Exception("가주문서를 찾을 수 없습니다.");
			}
			
			// 존재하면 구매 정보 write
			orderMapper.createPurchase(purchase.getOrderSheetId());
			
			// order product들의 재고 수량 update
			List<CartProduct> products = orderSheet.getProducts();
			for (CartProduct product : products) {
				Inventory resInventory = inventoryContext.inventorySearchById(product.getProductId(), token);
				resInventory.setQuantity(resInventory.getQuantity() - Long.valueOf(product.getQuantity()));
				
				inventoryContext.inventoryUpdate(resInventory, token);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/* 주문서 검색 - personId */
	public OrderSheet orderSheetSearch (long personId) {
		
		OrderSheetVO orderSheetVo = orderMapper.getOrderSheetByPerson(personId);
		
		OrderSheet orderSheet = new OrderSheet();
		if (orderSheetVo != null) {
			orderSheet = setOrderSheet(orderSheetVo);
		}

		return orderSheet;
	}
	
	/* 주문서 검색 - orderSheetId */
	public OrderSheet orderSheetSearchById (long orderSheetId) {
		
		OrderSheetVO orderSheetVo = orderMapper.getOrderSheetById(orderSheetId);
		
		OrderSheet orderSheet = new OrderSheet();
		if (orderSheetVo != null) {
			orderSheet = setOrderSheet(orderSheetVo);
		}

		return orderSheet;
	}
	
	/* 주문 검색 - orderSheetId */
	public Order orderSearchById (long orderSheetId) throws Exception {

		OrderSheetVO orderVo = orderMapper.getOrderById(orderSheetId);
		
		Order order = new Order();
		if (orderVo != null) {
			order.setPurchaseDate(orderVo.getPurchaseDate());
			
			// purchaseInfo set
			OrderSheet purchaseInfo = setOrderSheet(orderVo);
			order.setPurchaseInfo(purchaseInfo);
		}

		return order;
	}
	
	/* 주문 검색 - purchaseDate */
	public OrderList orderSearchByDate (long personId, String purchaseDate) throws Exception {

		List<OrderSheetVO> orderVoList = orderMapper.getOrderByDate(personId, purchaseDate);
		
		OrderList orders = new OrderList();
		List<Order> orderList = new ArrayList<Order>();
		for (OrderSheetVO orderVo : orderVoList) {
			Order order = new Order();
			order.setPurchaseDate(orderVo.getPurchaseDate());
			
			// purchaseInfo set
			OrderSheet purchaseInfo = setOrderSheet(orderVo);
			order.setPurchaseInfo(purchaseInfo);
			
			orderList.add(order);
		}
		orders.setOrders(orderList);

		return orders;
	}

	public OrderSheet setOrderSheet (OrderSheetVO orderSheetVo) {
		OrderSheet orderSheet = new OrderSheet();
		
		orderSheet.setOrderSheetId(orderSheetVo.getOrderId());
		orderSheet.setPersonId(orderSheetVo.getPersonId());
		orderSheet.setPhoneId(orderSheetVo.getPhoneVo().getPhoneId());
		orderSheet.setAddressId(orderSheetVo.getAddressVo().getAddressId());
		
		// order product set
		List<CartProduct> products = new ArrayList<CartProduct>();
		for (PartyProductVO orderProductVo : orderSheetVo.getPartyProductVoList()) {
			CartProduct orderProduct = setOrderProduct(orderProductVo);
			products.add(orderProduct);
		}
		orderSheet.setProducts(products);
		
		return orderSheet;
	}
	
	public OrderSheetVO setOrderSheetVO (OrderSheet orderSheet) {
		OrderSheetVO orderSheetVo = new OrderSheetVO();
		
		orderSheetVo.setOrderId(orderSheet.getOrderSheetId());
		orderSheetVo.setPersonId(orderSheet.getPersonId());
		
		// phoneVo set
		PhoneVO phoneVo = new PhoneVO();
		phoneVo.setPhoneId(orderSheet.getPhoneId());
		orderSheetVo.setPhoneVo(phoneVo);
		
		// addressVo set
		AddressVO addressVo = new AddressVO();
		addressVo.setAddressId(orderSheet.getAddressId());
		orderSheetVo.setAddressVo(addressVo);
		
		return orderSheetVo;
	}
	
	public CartProduct setOrderProduct (PartyProductVO orderProductVo) {
		CartProduct orderProduct = new CartProduct();
		
		orderProduct.setProductId(orderProductVo.getProductId());
		orderProduct.setQuantity(orderProductVo.getQuantity());
		
		return orderProduct;
	}
	
	public PartyProductVO setOrderProductVO (long personId, long orderId, CartProduct orderProduct) {
		PartyProductVO orderProductVo = new PartyProductVO();
		
		orderProductVo.setPersonId(personId);
		orderProductVo.setOrderId(orderId);
		orderProductVo.setProductId(orderProduct.getProductId());
		orderProductVo.setQuantity(orderProduct.getQuantity());
		
		return orderProductVo;
	}

}
