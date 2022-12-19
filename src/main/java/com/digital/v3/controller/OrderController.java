package com.digital.v3.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital.v3.context.AuthContext;
import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.schema.Order;
import com.digital.v3.schema.OrderList;
import com.digital.v3.schema.OrderSheet;
import com.digital.v3.schema.Purchase;
import com.digital.v3.schema.SuccessMsg;
import com.digital.v3.service.OrderService;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "구매", description = "Order Related API")
@RequestMapping(value = "/rest/order")
public class OrderController {
	
	@Resource
	private OrderService orderSvc;
	@Autowired
	private AuthContext authContext;
	
	@RequestMapping(value = "/sheet/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "가주문서 등록", notes = "결제 전 가주문서 등록을 위한 API. *입력 필드: product(s), addressId, phoneId")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = OrderSheet.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> orderSheetWrite (@ApiParam(value = "주문 정보", required = true) @RequestBody OrderSheet orderSheet,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		OrderSheet resOrderSheet = new OrderSheet();
		try {
			String token = request.getHeader("Authorization");
			long personId = authContext.getPersonId(token);
			
			orderSheet.setPersonId(personId);
			if (orderSvc.orderSheetWrite(orderSheet, token)) {
				resOrderSheet = orderSvc.orderSheetSearch(personId);
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<OrderSheet>(resOrderSheet, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/sheet/lookUp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "가주문서 조회", notes = "가주문서를 확인하기 위한 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = OrderSheet.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> orderSheetLookUp (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		try {
			String token = request.getHeader("Authorization");
			long personId = authContext.getPersonId(token);
			
			OrderSheet orderSheet = orderSvc.orderSheetSearch(personId);
			if (orderSheet.getPersonId() == 0) {
				return ExceptionUtils.setException(errors, 500, "현재 등록된 가주문서가 없습니다.", header);
			}
			return new ResponseEntity<OrderSheet>(orderSheet, header, HttpStatus.valueOf(200));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/sheet/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "가주문서 삭제", notes = "가주문서를 삭제하기 위한 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = SuccessMsg.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> orderSheetDelete (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		SuccessMsg success = new SuccessMsg();

		try {
			String token = request.getHeader("Authorization");
			long personId = authContext.getPersonId(token);
			
			if (orderSvc.orderSheetDelete(personId)) {
				success.setSuccessCode(200);
				success.setSuccessMsg("가주문서를 삭제했습니다.");
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<SuccessMsg>(success, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/purchase", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "결제", notes = "가주문서에 있는 상품(들)을 결제하기 위한 API. *입력 필드: orderSheetId")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Order.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> purchase (@ApiParam(value = "가주문서 정보", required = true) @RequestBody Purchase purchase,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		Order order = new Order();
		try {
			String token = request.getHeader("Authorization");
			
			purchase.setPurchaseDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
			if (orderSvc.purchase(purchase, token)) {
				order = orderSvc.orderSearchById(purchase.getOrderSheetId());
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}

		return new ResponseEntity<Order>(order, header, HttpStatus.valueOf(200));
	}

	@RequestMapping(value = "/inquiry/{keyword}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "구매 목록 검색", notes = "키워드를 포함하는 결제 날짜로 구매 목록을 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = OrderList.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> orderSearchByKeyword (@ApiParam(value = "결제일 키워드", required = true) @PathVariable String keyword, 
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			long personId = authContext.getPersonId(token);
			
			OrderList orders = orderSvc.orderSearchByDate(personId, keyword);
			return new ResponseEntity<OrderList>(orders, header, HttpStatus.valueOf(200));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
}
