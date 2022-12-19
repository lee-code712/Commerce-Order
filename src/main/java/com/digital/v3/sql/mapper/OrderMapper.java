package com.digital.v3.sql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.digital.v3.sql.vo.OrderSheetVO;
import com.digital.v3.sql.vo.PartyProductVO;

@Mapper
public interface OrderMapper {
	
	// orderSheet 레코드 생성 (orderSheet == 아직 purchase 레코드가 없는 orderSheet)
	public void createOrderSheet(OrderSheetVO orderSheetVo);
	
	// personId로 orderSheet 조회
	public OrderSheetVO getOrderSheetByPerson(long personId);
	
	// orderId로 orderSheet 조회
	public OrderSheetVO getOrderSheetById(long orderId);
	
	// personId로 orderSheet 삭제
	public void deleteOrderSheet(long personId);
	
	// personId, productId, quantity로 cart product 중 제일 오래된 레코드의 orderId 변경
	public void updateOrderIdOfOrderProduct(PartyProductVO orderProductVo);
	
	// purchase 레코드 생성
	public void createPurchase(long orderId);
	
	// orderId로 order 조회 (order == purchase 레코드가 있는 orderSheet)
	public OrderSheetVO getOrderById(long orderId);
	
	// personId, purchase의 createDate로 order 목록 조회
	public List<OrderSheetVO> getOrderByDate(@Param("personId") long personId, @Param("date") String purchaseDate);

}
