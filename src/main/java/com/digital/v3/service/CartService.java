package com.digital.v3.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.digital.v3.context.InventoryContext;
import com.digital.v3.schema.Cart;
import com.digital.v3.schema.CartProduct;
import com.digital.v3.sql.mapper.CartMapper;
import com.digital.v3.sql.vo.PartyProductVO;

@Component
public class CartService {
	
	@Autowired
	CartMapper cartMapper;
	@Autowired
	InventoryContext inventoryContext;
	
	/* 장바구니에 상품 등록 */
	public boolean cartProductWrite (long personId, CartProduct cartProduct, String token) throws Exception {
		try {
			PartyProductVO cartProductVo = setCartProductVO(personId, cartProduct);

			// 입력 수량 유효성 확인 - 중복 상품의 담은 수량을 입력 수량에 더한 값으로 계산
			int cartQuantity = cartMapper.getQuantityOfPluralCartProduct(cartProductVo);
			cartQuantity += cartProductVo.getQuantity();
			
			if (!inventoryContext.quantityCheck(cartProduct.getProductId(), cartQuantity, token)) {
				throw new Exception("상품 ID: " + cartProduct.getProductId() + "의 재고가 등록되지 않았거나, 입력 수량보다 재고 수량이 부족합니다.");
			}
	
			// 유효한 입력 수량이면 write
			cartMapper.createCartProduct(cartProductVo);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/* 장바구니 검색 - personId */
	public Cart cartSearch (long personId) throws Exception {

		List<PartyProductVO> cartProductVOList = cartMapper.getCartProductByPerson(personId);

		Cart cart = new Cart();
		List<CartProduct> cartProductList = new ArrayList<CartProduct>();
		for (PartyProductVO cartProductVo : cartProductVOList) {
			CartProduct cartProduct = setCartProduct(cartProductVo);
			cartProductList.add(cartProduct);
		}
		cart.setCart(cartProductList);
		
		return cart;
	}
	
	/* 장바구니 상품 삭제 */
	public boolean cartProductDelete (long personId, CartProduct cartProduct) throws Exception {
		try {
			PartyProductVO partyProductVo = setCartProductVO(personId, cartProduct);
			
			// cartProduct 존재 여부 확인
			if (cartMapper.isExistCartProduct(partyProductVo) == 0) {
				throw new Exception("장바구니에 해당하는 상품이 없습니다."); 
			}
			
			// 존재하면 delete
			cartMapper.deleteCartProduct(partyProductVo);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public CartProduct setCartProduct (PartyProductVO cartProductVo) {
		CartProduct cartProduct = new CartProduct();
		
		cartProduct.setProductId(cartProductVo.getProductId());
		cartProduct.setQuantity(cartProductVo.getQuantity());
		
		return cartProduct;
	}
	
	public PartyProductVO setCartProductVO (long personId, CartProduct cartProduct) {
		PartyProductVO cartProductVo = new PartyProductVO();
		
		cartProductVo.setPersonId(personId);
		cartProductVo.setProductId(cartProduct.getProductId());
		cartProductVo.setQuantity(cartProduct.getQuantity());
		
		return cartProductVo;
	}
	
}
