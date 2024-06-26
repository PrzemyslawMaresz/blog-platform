package pl.pmar.blogplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.pmar.blogplatform.model.entity.Category;
import pl.pmar.blogplatform.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id);
    }
}
