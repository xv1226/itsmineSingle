package com.sparta.itsminesingle.domain.kakaopay.dto;

import lombok.Getter;

@Getter
public class KakaoPayGetTidRequestDto {
    private String username;
    private String productName;
    private Integer bidPrice;
}
