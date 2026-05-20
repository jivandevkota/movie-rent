package org.example.movie_rental.service;

import org.example.movie_rental.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        System.out.println("\n=== CATEGORIES FROM DB ===");
        categories.forEach(c -> System.out.println("  " + c.id() + " - " + c.name()));
        System.out.println("=== TOTAL: " + categories.size() + " ===\n");
    }
}
