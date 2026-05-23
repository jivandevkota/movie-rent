package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CategoryDto;
import org.example.movie_rental.entity.Category;
import org.example.movie_rental.exception.ResourceNotFoundException;
import org.example.movie_rental.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> list = categoryRepository.findAll().stream()
                .map(c -> new CategoryDto(c.getCategoryId(), c.getName()))
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        Category c = Category.builder()
                .name(name)
                .lastUpdate(LocalDateTime.now())
                .build();
        c = categoryRepository.save(c);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryDto(c.getCategoryId(), c.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        if (body.containsKey("name")) c.setName(body.get("name"));
        c.setLastUpdate(LocalDateTime.now());
        c = categoryRepository.save(c);
        return ResponseEntity.ok(new CategoryDto(c.getCategoryId(), c.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
