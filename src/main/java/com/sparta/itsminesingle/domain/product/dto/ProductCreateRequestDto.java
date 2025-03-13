package com.sparta.itsminesingle.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.itsminesingle.domain.images.dto.ProductImagesRequestDto;
import lombok.Getter;

@Getter
public class ProductCreateRequestDto {

    private final ProductCreateDto productCreateDto;
    private final ProductImagesRequestDto productImagesRequestDto;

    @JsonCreator
    public ProductCreateRequestDto(@JsonProperty("productCreateDto") ProductCreateDto productCreateDto,
            @JsonProperty("productImagesRequestDto") ProductImagesRequestDto productImagesRequestDto) {
        this.productCreateDto = productCreateDto;
        this.productImagesRequestDto = productImagesRequestDto;
    }
}

