package com.sparta.itsminesingle.domain.category.repository;

import com.sparta.itsminesingle.domain.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
}
