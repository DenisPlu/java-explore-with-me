package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT * FROM categories ORDER BY id DESC LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Category> getAllWithPagination(Integer size, Integer from);
}