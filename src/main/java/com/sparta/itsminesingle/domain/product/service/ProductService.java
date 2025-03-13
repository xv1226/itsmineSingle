package com.sparta.itsminesingle.domain.product.service;

import com.sparta.itsminesingle.domain.category.entity.Category;
import com.sparta.itsminesingle.domain.images.dto.ProductImagesRequestDto;
import com.sparta.itsminesingle.domain.images.service.ImagesService;
import com.sparta.itsminesingle.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsminesingle.domain.product.dto.ProductCreateDto;
import com.sparta.itsminesingle.domain.product.dto.ProductResponseDto;
import com.sparta.itsminesingle.domain.product.entity.Product;
import com.sparta.itsminesingle.domain.product.repository.ProductAdapter;
import com.sparta.itsminesingle.domain.product.repository.ProductRepository;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter adapter;
    private final ImagesService imagesService;
    private final KakaoPayService kakaoPayService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductResponseDto createProduct(ProductCreateDto createDto,
            ProductImagesRequestDto imagesRequestDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow();
        Category category = adapter.findCategoryByCategoryName(createDto.getCategoryName());

        Product product = createDto.toEntity(category);
        product.assignUser(user);

        Product newProduct = productRepository.save(product);
        imagesService.createProductImages(imagesRequestDto, product);
        return new ProductResponseDto(newProduct, imagesRequestDto);
    }

}
