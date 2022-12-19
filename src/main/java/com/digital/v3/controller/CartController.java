package com.digital.v3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital.v3.context.AuthContext;
import com.digital.v3.schema.Cart;
import com.digital.v3.schema.CartProduct;
import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.service.CartService;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "장바구니", description = "Cart Related API")
@RequestMapping(value = "/rest/cart")
public class CartController {
	
	@Resource
	private CartService cartSvc;
	@Autowired
	private AuthContext authContext;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "장바구니 상품 추가", notes = "특정 상품을 장바구니에 추가하기 위한 API. *입력 필드: productId, quantity")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Cart.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> cartProductAdd (@ApiParam(value = "상품 정보", required = true) @RequestBody CartProduct cartProduct,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		Cart cart = new Cart();
		try {
			String token = request.getHeader("Authorization");
			long personId = authContext.getPersonId(token);

			if (cartSvc.cartProductWrite(personId, cartProduct, token)) {
				cart = cartSvc.cartSearch(personId);
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Cart>(cart, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "장바구니 상품 삭제", notes = "특정 상품을 장바구니에서 삭제하기 위한 API. *입력 필드: productId, quantity")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Cart.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> cartProductDelete (@ApiParam(value = "상품 정보", required = true) @RequestBody CartProduct cartProduct,
			HttpServletRequest request, HttpServletResponse response) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		Cart cart = new Cart();
		try {
			String token = request.getHeader("Authorization");
			long personId = authContext.getPersonId(token);
			
			if (cartSvc.cartProductDelete(personId, cartProduct)) {
				cart = cartSvc.cartSearch(personId);
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Cart>(cart, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/lookUp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "장바구니 조회", notes = "장바구니에 담긴 상품 목록을 조회하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Cart.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> cartLookUp (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			long personId = authContext.getPersonId(token);
			
			Cart cart = cartSvc.cartSearch(personId);
			return new ResponseEntity<Cart>(cart, header, HttpStatus.valueOf(200));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}	
	}
}
