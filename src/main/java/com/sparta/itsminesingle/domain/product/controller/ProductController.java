package com.sparta.itsminesingle.domain.product.controller;

import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.SUCCESS_SAVE_PRODUCT;

import com.sparta.itsminesingle.domain.product.dto.ProductCreateRequestDto;
import com.sparta.itsminesingle.domain.product.dto.ProductResponseDto;
import com.sparta.itsminesingle.domain.product.service.ProductService;
import com.sparta.itsminesingle.global.response.HttpResponseDto;
import com.sparta.itsminesingle.global.response.ResponseUtils;
import com.sparta.itsminesingle.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createProduct(
            @RequestBody ProductCreateRequestDto productCreateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ProductResponseDto product = productService.createProduct(
                productCreateRequestDto.getProductCreateDto(),
                productCreateRequestDto.getProductImagesRequestDto(),
                userDetails.getUser().getId());
        return ResponseUtils.of(SUCCESS_SAVE_PRODUCT, product);
    }
}
