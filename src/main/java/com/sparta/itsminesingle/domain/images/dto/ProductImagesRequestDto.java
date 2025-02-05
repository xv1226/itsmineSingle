package com.sparta.itsminesingle.domain.images.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductImagesRequestDto {

    private final List<String> imagesUrl;

    @JsonCreator
    public ProductImagesRequestDto(@JsonProperty("imagesUrl") List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

}

