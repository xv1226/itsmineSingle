package com.sparta.itsminesingle.domain.images.entity;

import com.sparta.itsminesingle.domain.images.util.ImageType;
import com.sparta.itsminesingle.domain.product.entity.Product;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.global.common.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Images extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "imageUrl", nullable = false)
    private String imagesUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "contentType", nullable = true)
    private ImageType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public Images(String imagesUrl, ImageType contentType, Product product) {
        this.imagesUrl = imagesUrl;
        this.contentType = contentType;
        this.product = product;
    }

    public Images(String imagesUrl, ImageType contentType, User user) {
        this.imagesUrl = imagesUrl;
        this.contentType = contentType;
        this.user = user;
    }

}
