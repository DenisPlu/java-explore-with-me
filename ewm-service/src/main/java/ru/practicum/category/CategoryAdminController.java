package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryServiceImpl categoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Category create(@RequestBody @Valid Category category) {
        log.info("Received a request to create a new Category: {}", category);
        return categoryService.create(category);
    }

    @PatchMapping("/{id}")
    public Category update(@PathVariable Integer id, @RequestBody @Valid Category category) {
        log.info("Received a request to update a category with id: {}, Category: {}", id, category);
        return categoryService.update(id, category);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        log.info("Received a request to delete a category with id: {}", id);
        categoryService.delete(id);
    }
}