package com.sparta.itsminesingle.domain.comment.service;

import com.sparta.itsminesingle.domain.comment.dto.AddCommentResponseDto;
import com.sparta.itsminesingle.domain.comment.dto.CommentRequestDto;
import com.sparta.itsminesingle.domain.comment.dto.CommentResponseDto;
import com.sparta.itsminesingle.domain.comment.entity.Comment;
import com.sparta.itsminesingle.domain.comment.repository.CommentAdapter;
import com.sparta.itsminesingle.domain.comment.repository.CommentRepository;
import com.sparta.itsminesingle.domain.qna.entity.Qna;
import com.sparta.itsminesingle.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentAdapter commentAdapter;
    private final CommentRepository commentRepository;

    // 댓글 작성
    @Transactional
    public AddCommentResponseDto addComment(Long qnaId, User user, CommentRequestDto commentRequestDto) {
        Qna qna = getQna(qnaId);
        validationComment(qna, user);
        Comment comment = new Comment(commentRequestDto, qna);
        commentAdapter.save(comment);
        return new AddCommentResponseDto(comment, user);
    }

    // 댓글 조회
    public CommentResponseDto getComment(Long qnaId) {
        Comment comment = getCommentByQnaId(qnaId);
        return new CommentResponseDto(comment, qnaId);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long qnaId, CommentRequestDto requestDto, User user) {
        Qna qna = getQna(qnaId);

        commentAdapter.checkUserAndManager(qna, user);
        Comment comment = getCommentByQnaId(qnaId);
        comment.commentUpdate(requestDto);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long qnaId, User user) {
        Qna qna = getQna(qnaId);

        commentAdapter.checkUserAndManager(qna, user);
        Comment comment = getCommentByQnaId(qnaId);
        commentRepository.delete(comment);
    }

    // QnA 가져오기
    public Qna getQna(Long qnaId) {
        return commentAdapter.getQna(qnaId);
    }

    // 댓글 가져오기
    public Comment getCommentByQnaId(Long qnaId) {
        return commentAdapter.findByQnaId(qnaId);
    }

    // 문의사항에 이미 댓글이 있는지 확인
    public void validationComment(Qna qna, User user) {
        commentAdapter.checkUserAndManager(qna, user);
        commentAdapter.checkDuplicateComment(qna.getId());
    }
}
