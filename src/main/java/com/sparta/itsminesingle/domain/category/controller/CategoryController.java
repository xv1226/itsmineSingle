package com.sparta.itsminesingle.domain.category.controller;

import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.CATEGORY_SUCCESS_GET;
import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.SUCCESS_TO_MAKE_NEW_CATEGORY;

import com.sparta.itsminesingle.domain.category.dto.CategoryDto;
import com.sparta.itsminesingle.domain.category.dto.CategoryResponseDto;
import com.sparta.itsminesingle.domain.category.service.CategoryService;
import com.sparta.itsminesingle.global.response.HttpResponseDto;
import com.sparta.itsminesingle.global.response.ResponseUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createCategory(
            @RequestBody CategoryDto categoryName
    ) {
        categoryService.createCategory(categoryName);
        return ResponseUtils.of(SUCCESS_TO_MAKE_NEW_CATEGORY);
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getCategory() {
        List<CategoryResponseDto> responseDto = categoryService.getCategory();
        return ResponseUtils.of(CATEGORY_SUCCESS_GET, responseDto);
    }
}
