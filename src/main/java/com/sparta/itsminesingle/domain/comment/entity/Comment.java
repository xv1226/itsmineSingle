package com.sparta.itsminesingle.domain.comment.entity;

import com.sparta.itsminesingle.domain.comment.dto.CommentRequestDto;
import com.sparta.itsminesingle.domain.qna.entity.Qna;
import com.sparta.itsminesingle.global.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private Qna qna;

    public Comment(CommentRequestDto commentRequestDto, Qna qna) {
        this.author = commentRequestDto.getAuthor();
        this.content = commentRequestDto.getContent();
        this.qna = qna;
    }

    public void commentUpdate(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
