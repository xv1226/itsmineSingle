package com.sparta.itsminesingle.domain.kakaopay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)//JSON으로 변환될 때 필드 이름을 Snake Case 방식으로 변환
public class KakaoPayReadyRequestDto {
    private String cid;//가맹점 코드, 10자
    private Long partnerOrderId;//가맹점 주문번호, 최대 100자
    private String partnerUserId;//가맹점 회원 id, 최대 100자
    private String itemName;//상품명, 최대 100자
    private Integer quantity;//상품 수량
    private Integer totalAmount;//상품 총액
    private Integer taxFreeAmount;//상품 비과세 금액
    private Integer vatAmount;//상품 부가세 금액
    private String approvalUrl;//결제 성공 시 redirect url, 최대 255자
    private String cancelUrl;//결제 취소 시 redirect url, 최대 255자
    private String failUrl;//결제 실패 시 redirect url, 최대 255자
}
