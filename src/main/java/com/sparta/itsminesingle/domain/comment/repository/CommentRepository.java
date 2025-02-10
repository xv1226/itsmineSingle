package com.sparta.itsminesingle.domain.comment.repository;

import com.sparta.itsminesingle.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByQnaId(Long qnaId);
}
