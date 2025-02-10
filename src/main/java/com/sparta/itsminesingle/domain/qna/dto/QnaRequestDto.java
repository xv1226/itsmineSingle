package com.sparta.itsminesingle.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QnaRequestDto {

    @NotBlank(message = "사용자 이름이 비어있습니다.")
    private String author;
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;

    @NotBlank(message = "제목이 비어있습니다.")
    @Size(min = 3)
    private String title;

    @NotBlank(message = "본문의 비어있습니다.")
    @Size(min = 3)
    private String content;

    private boolean secretQna;
}

