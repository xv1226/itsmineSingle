package com.sparta.itsminesingle.domain.kakaopay.dto;

import lombok.Getter;

@Getter
public class KakaoPayGetTidResponseDto {

    private String tid;

    public KakaoPayGetTidResponseDto(String tid) {
        this.tid=tid;
    }
}

