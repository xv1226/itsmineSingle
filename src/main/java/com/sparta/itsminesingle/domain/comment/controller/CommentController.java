package com.sparta.itsminesingle.domain.comment.controller;

import static com.sparta.itsminesingle.global.common.response.ResponseCodeEnum.COMMENT_SUCCESS_CREATE;
import static com.sparta.itsminesingle.global.common.response.ResponseCodeEnum.COMMENT_SUCCESS_DELETE;
import static com.sparta.itsminesingle.global.common.response.ResponseCodeEnum.COMMENT_SUCCESS_GET;
import static com.sparta.itsminesingle.global.common.response.ResponseCodeEnum.COMMENT_SUCCESS_UPDATE;
import static com.sparta.itsminesingle.global.common.response.ResponseUtils.of;

import com.sparta.itsminesingle.domain.comment.dto.AddCommentResponseDto;
import com.sparta.itsminesingle.domain.comment.dto.CommentRequestDto;
import com.sparta.itsminesingle.domain.comment.dto.CommentResponseDto;
import com.sparta.itsminesingle.domain.comment.service.CommentService;
import com.sparta.itsminesingle.global.common.response.HttpResponseDto;
import com.sparta.itsminesingle.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/qnas/{qnaId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<HttpResponseDto> addComment(
            @PathVariable Long qnaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CommentRequestDto commentRequestDto) {

        AddCommentResponseDto comment = commentService.addComment(qnaId,userDetails.getUser(),commentRequestDto);
        return of(COMMENT_SUCCESS_CREATE, comment);
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<HttpResponseDto> getComment(
            @PathVariable Long qnaId) {
        CommentResponseDto comment = commentService.getComment(qnaId);
        return of(COMMENT_SUCCESS_GET, comment);
    }

    // 댓글 수정
    @PatchMapping
    public ResponseEntity<HttpResponseDto> updateComment(
            @PathVariable Long qnaId,
            @RequestBody @Valid CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateComment(qnaId,commentRequestDto,userDetails.getUser());
        return of(COMMENT_SUCCESS_UPDATE);
    }

    // 댓글 삭제
    @DeleteMapping
    public ResponseEntity<HttpResponseDto> deleteComment(
            @PathVariable Long qnaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.deleteComment(qnaId,userDetails.getUser());
        return of(COMMENT_SUCCESS_DELETE);
    }
}
