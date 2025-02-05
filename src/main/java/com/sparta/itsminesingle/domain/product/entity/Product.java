package com.sparta.itsminesingle.domain.product.entity;

import com.sparta.itsminesingle.domain.auction.entity.Auction;
import com.sparta.itsminesingle.domain.category.entity.Category;
import com.sparta.itsminesingle.domain.images.entity.Images;
import com.sparta.itsminesingle.domain.product.utils.ProductStatus;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.global.common.TimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product", indexes = {
        @Index(name = "idx_product_name", columnList = "productName"),
        @Index(name = "idx_user_product_name", columnList = "user_id, productName"),
        @Index(name = "idx_product_deleted", columnList = "deletedAt")
})
@NoArgsConstructor
public class Product extends TimeStamp {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer startPrice;
    @Column
    private Integer currentPrice;//요청시 시작가만 받게 하기위해 요청 안해도 되게끔 만들었습니다
    @Column(nullable = false)
    private Integer auctionNowPrice;
    // 입찰, 낙찰, 유찰
    // 상품이 등록이 되는 즉시 입찰 시작이기 때문에 바로 입찰로 상태를 변경한다.
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    private LocalDateTime dueDate;
    private LocalDateTime deletedAt;

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Images> imagesList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auction> auction = new ArrayList<>();

    private int likeCount;

}
