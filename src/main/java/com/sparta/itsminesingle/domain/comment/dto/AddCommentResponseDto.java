package com.sparta.itsminesingle.domain.comment.dto;

import com.sparta.itsminesingle.domain.comment.entity.Comment;
import com.sparta.itsminesingle.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AddCommentResponseDto {

    private final String userId; //댓글 작성한 유저 로그인 ID
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AddCommentResponseDto(Comment comment, User user) {
        this.userId = user.getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}

