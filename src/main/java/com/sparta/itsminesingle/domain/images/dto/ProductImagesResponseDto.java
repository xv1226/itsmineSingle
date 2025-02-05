package com.sparta.itsminesingle.domain.images.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ProductImagesResponseDto {

    private final List<String> imagesUrl;

    public ProductImagesResponseDto(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
}
