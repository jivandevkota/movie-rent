package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CategoryDto;
import org.example.movie_rental.repository.CategoryRepository;
import org.example.movie_rental.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDto(c.getCategoryId(), c.getName()))
                .toList();
    }
}
