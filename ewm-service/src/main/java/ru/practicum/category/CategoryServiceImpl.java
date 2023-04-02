package ru.practicum.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllWithPagination(Integer from, Integer size) {

        return categoryRepository.getAllWithPagination(from, size);
    }

    @Override
    public Category get(Integer id) {
        try {
            categoryRepository.getReferenceById(id).getName();
            return categoryRepository.getReferenceById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category с запрошенным id не существует");
        }
    }

    @Override
    public Category update(Integer id, Category category) {
        System.out.println(category);

        Category curCategory = categoryRepository.getReferenceById(id);
        System.out.println(curCategory);

        if (!curCategory.getName().equals(category.getName())){
            if (Optional.ofNullable(category.getName()).isPresent()) {
                curCategory.setName(category.getName());
            }
            return categoryRepository.save(curCategory);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "У Категории уже установлено данное имя");
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            categoryRepository.getReferenceById(id).getName();
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category с запрошенным id не существует");
        }
    }
}
