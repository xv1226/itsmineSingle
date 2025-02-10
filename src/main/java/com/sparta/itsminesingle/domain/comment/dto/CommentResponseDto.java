package com.sparta.itsminesingle.domain.comment.dto;

import com.sparta.itsminesingle.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final Long qnaId;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment, Long qnaId) {
        this.commentId = comment.getId();
        this.qnaId = qnaId;
        this.author = comment.getAuthor();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}