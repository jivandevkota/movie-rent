package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.*;
import org.example.movie_rental.entity.*;
import org.example.movie_rental.exception.BusinessException;
import org.example.movie_rental.exception.ResourceNotFoundException;
import org.example.movie_rental.repository.*;
import org.example.movie_rental.service.ActorService;
import org.example.movie_rental.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final InventoryRepository inventoryRepository;
    private final FilmActorRepository filmActorRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final FilmCategoryRepository filmCategoryRepository;
    private final RentalRepository rentalRepository;
    private final StoreRepository storeRepository;
    private final ActorService actorService;

    @Override
    @Transactional(readOnly = true)
    public FilmDetailsDto getFilmDetailsByFilmId(int filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new RuntimeException("Film not found: " + filmId));

        String categoryName = filmRepository.findCategoryNameByFilmId(filmId);
        long availableCopies = rentalRepository.countAvailableInventory(filmId);
        long totalRentals = rentalRepository.countByFilmId(filmId);

        return new FilmDetailsDto(
                film.getFilmId(),
                film.getTitle(),
                film.getDescription(),
                categoryName != null ? categoryName : "Uncategorized",
                film.getLanguage() != null ? film.getLanguage().getName() : null,
                film.getReleaseYear(),
                film.getRentalDuration(),
                film.getRentalRate(),
                film.getLength(),
                film.getReplacementCost(),
                film.getRating(),
                film.getSpecialFeatures(),
                actorService.getActorsByFilmId(filmId),
                "/img/films/" + film.getFilmId() + ".jpg",
                availableCopies,
                totalRentals
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilmDto> getAllFilms(int page, int size) {
        Pageable data = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "filmId"));
        return filmRepository.findAllFilmDtos(data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilmDto> getFilmByCategoryId(Long categoryId, int page, int size) {
        Pageable data = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "filmId"));
        return filmRepository.findFilmDtosByCategory(categoryId, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilmDto> searchFilmsByTitle(String title, int page, int size) {
        Pageable data = PageRequest.of(page, size, Sort.by("title").ascending());
        return filmRepository.searchFilmDtos(title, data);
    }

    // ---- Admin methods ----

    @Override
    @Transactional(readOnly = true)
    public FilmDetailDto getFilmDetailForAdmin(Integer filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));

        String categoryName = filmRepository.findCategoryNameByFilmId(filmId);
        long availableCopies = rentalRepository.countAvailableInventory(filmId);
        long totalCopies = inventoryRepository.countByFilmFilmId(filmId);

        Integer categoryId = null;
        if (film.getFilmCategories() != null && !film.getFilmCategories().isEmpty()) {
            categoryId = film.getFilmCategories().get(0).getCategory().getCategoryId();
        }

        return new FilmDetailDto(
                film.getFilmId(),
                film.getTitle(),
                film.getDescription(),
                film.getReleaseYear(),
                film.getRentalDuration(),
                film.getRentalRate() != null ? BigDecimal.valueOf(film.getRentalRate()) : BigDecimal.ZERO,
                film.getLength(),
                film.getReplacementCost() != null ? BigDecimal.valueOf(film.getReplacementCost()) : BigDecimal.ZERO,
                film.getRating(),
                film.getSpecialFeatures(),
                film.getLanguage() != null ? film.getLanguage().getLanguageId() : null,
                film.getLanguage() != null ? film.getLanguage().getName() : null,
                categoryId,
                categoryName,
                actorService.getActorsByFilmId(filmId),
                availableCopies,
                totalCopies
        );
    }

    @Override
    @Transactional
    public FilmDetailDto createFilm(CreateFilmRequest request) {
        Language language = languageRepository.findById(request.languageId())
                .orElseThrow(() -> new ResourceNotFoundException("Language", request.languageId()));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

        LocalDateTime now = LocalDateTime.now();

        Film film = Film.builder()
                .title(request.title())
                .description(request.description())
                .releaseYear(request.releaseYear())
                .language(language)
                .rentalDuration(request.rentalDuration())
                .rentalRate(request.rentalRate().floatValue())
                .length(request.length())
                .replacementCost(request.replacementCost().floatValue())
                .rating(request.rating())
                .specialFeatures(request.specialFeatures())
                .lastUpdate(now)
                .build();

        if (request.originalLanguageId() != null) {
            Language origLang = languageRepository.findById(request.originalLanguageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Original Language", request.originalLanguageId()));
            film.setOriginalLanguage(origLang);
        }

        film = filmRepository.save(film);

        FilmCategory filmCategory = new FilmCategory();
        filmCategory.setFilm(film);
        filmCategory.setCategory(category);
        filmCategory.setLastUpdate(now);
        filmCategoryRepository.save(filmCategory);

        return getFilmDetailForAdmin(film.getFilmId());
    }

    @Override
    @Transactional
    public FilmDetailDto updateFilm(Integer filmId, CreateFilmRequest request) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));

        Language language = languageRepository.findById(request.languageId())
                .orElseThrow(() -> new ResourceNotFoundException("Language", request.languageId()));

        film.setTitle(request.title());
        film.setDescription(request.description());
        film.setReleaseYear(request.releaseYear());
        film.setLanguage(language);
        film.setRentalDuration(request.rentalDuration());
        film.setRentalRate(request.rentalRate().floatValue());
        film.setLength(request.length());
        film.setReplacementCost(request.replacementCost().floatValue());
        film.setRating(request.rating());
        film.setSpecialFeatures(request.specialFeatures());
        film.setLastUpdate(LocalDateTime.now());

        if (request.originalLanguageId() != null) {
            Language origLang = languageRepository.findById(request.originalLanguageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Original Language", request.originalLanguageId()));
            film.setOriginalLanguage(origLang);
        } else {
            film.setOriginalLanguage(null);
        }

        filmRepository.save(film);

        Category newCategory = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

        if (film.getFilmCategories() != null && !film.getFilmCategories().isEmpty()) {
            FilmCategory fc = film.getFilmCategories().get(0);
            fc.setCategory(newCategory);
            fc.setLastUpdate(LocalDateTime.now());
            filmCategoryRepository.save(fc);
        }

        return getFilmDetailForAdmin(filmId);
    }

    @Override
    @Transactional
    public void deleteFilm(Integer filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));

        if (film.getFilmCategories() != null) filmCategoryRepository.deleteAll(film.getFilmCategories());
        if (film.getFilmActors() != null) filmActorRepository.deleteAll(film.getFilmActors());
        if (film.getInventories() != null) inventoryRepository.deleteAll(film.getInventories());

        filmRepository.delete(film);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getFilmInventory(Integer filmId) {
        List<Inventory> inventories = inventoryRepository.findByFilmFilmId(filmId);
        return inventories.stream().map(inv -> {
            boolean isRented = inv.getRentals() != null &&
                    inv.getRentals().stream().anyMatch(r -> r.getReturnDate() == null);
            return new InventoryDto(inv.getInventoryId(), inv.getStore().getStoreId(), isRented);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryDto addInventoryCopy(Integer filmId, Integer storeId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));

        Store store = storeRepository.getReferenceById(storeId);

        Inventory inventory = Inventory.builder()
                .film(film)
                .store(store)
                .lastUpdate(LocalDateTime.now())
                .build();

        inventory = inventoryRepository.save(inventory);
        return new InventoryDto(inventory.getInventoryId(), storeId, false);
    }

    @Override
    @Transactional
    public void removeInventoryCopy(Integer inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategoriesForAdmin() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDto(c.getCategoryId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<FilmDetailDto> getAllFilmDetailsForAdmin(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "filmId"));
        return filmRepository.findAllFilmDtos(pageable)
                .map(f -> getFilmDetailForAdmin(f.filmId()));
    }
}