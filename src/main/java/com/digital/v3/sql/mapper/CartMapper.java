package com.digital.v3.sql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.digital.v3.sql.vo.PartyProductVO;

@Mapper
public interface CartMapper {

	// cart product 레코드 생성 (cart product == orderId가 null인 party product)
	public void createCartProduct(PartyProductVO cartProductVo);
	
	// personId로 cart product 조회
	public List<PartyProductVO> getCartProductByPerson(long personId);
	
	// personId, productId, quantity로 cart product 레코드 개수 조회 (존재여부 확인)
	public int isExistCartProduct(PartyProductVO cartProductVo);
	
	// personId, productId로 cart product의 quantity를 합한 결과 조회
	public int getQuantityOfPluralCartProduct(PartyProductVO cartProductVo);
	
	// personId, productId, quantity로 cart product 중 제일 오래된 레코드 삭제
	public void deleteCartProduct(PartyProductVO cartProductVo);
	
}
