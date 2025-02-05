package com.sparta.itsminesingle.domain.images.repository;

import com.sparta.itsminesingle.domain.images.entity.Images;
import com.sparta.itsminesingle.domain.images.util.ImageType;
import com.sparta.itsminesingle.domain.product.entity.Product;
import com.sparta.itsminesingle.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUserAndContentType(User user, ImageType imageType);

    Optional<Images> findByIdAndProduct(Long imageId, Product product);
}
