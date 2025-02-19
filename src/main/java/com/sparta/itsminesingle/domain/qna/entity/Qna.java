package com.sparta.itsminesingle.domain.qna.entity;

import static com.sparta.itsminesingle.domain.user.utils.UserRole.MANAGER;
import static com.sparta.itsminesingle.global.response.ResponseExceptionEnum.COMMENT_EQUAL_SELLER;
import static com.sparta.itsminesingle.global.response.ResponseExceptionEnum.QNA_USER_NOT_VALID;

import com.sparta.itsminesingle.domain.comment.entity.Comment;
import com.sparta.itsminesingle.domain.product.entity.Product;
import com.sparta.itsminesingle.domain.qna.dto.QnaRequestDto;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.global.TimeStamp;
import com.sparta.itsminesingle.global.exception.DataDuplicatedException;
import com.sparta.itsminesingle.global.exception.comment.CommentEqualSellerException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "qna_id")
    private Long id;

    private String title;

    private String content;

    private String passowrd;

    private boolean secretQna;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private Comment comment;

    public Qna(QnaRequestDto requestDto, User user, Product product) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.passowrd = requestDto.getPassword();
        this.user = user;
        this.product = product;
        this.secretQna = requestDto.isSecretQna();
    }

    public static Qna of(QnaRequestDto qnaRequestDTO, User user, Product product) {
        return new Qna(qnaRequestDTO, user, product);
    }

    public void update(QnaRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    // 판매자 확인
    public void equalsSeller(Long userId) {
        if (!this.product.getUser().getId().equals(userId) || !this.product.getUser().getUserRole()
                .equals(MANAGER)) {
            throw new CommentEqualSellerException(COMMENT_EQUAL_SELLER);
        }
    }

    /**
     * Qna내의 유저 정보와 인가된 유저 정보를 확인 후 일치 하지 않으면 Exception
     *
     * @param detailUser 인가된 유저 정보
     * @param qnaUser    qnaEntity 유저 정보
     */
    public void checkQnaUser(User detailUser, User qnaUser) {
        if (!detailUser.getId().equals(qnaUser.getId())) {
            throw new DataDuplicatedException(QNA_USER_NOT_VALID);
        }
    }
}
