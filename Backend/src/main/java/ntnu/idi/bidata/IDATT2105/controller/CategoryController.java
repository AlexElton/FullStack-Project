package ntnu.idi.bidata.IDATT2105.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ntnu.idi.bidata.IDATT2105.models.enums.Role;
import ntnu.idi.bidata.IDATT2105.models.items.Category;
import ntnu.idi.bidata.IDATT2105.services.item.CategoryService;
import ntnu.idi.bidata.IDATT2105.services.auth.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    private final CategoryService categoryService;
    private final AuthService authService;
    
    @Autowired
    public CategoryController(CategoryService categoryService, AuthService authService) {
        this.categoryService = categoryService;
        this.authService = authService;
    }
    
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long parentId) {
        Long userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        Role userRole = authService.getUserRole(userId);
        Category category = categoryService.createCategory(name, description, parentId, userRole);
        return ResponseEntity.ok(category);
    }
    
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long categoryId,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long parentId) {
        Long userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        Role userRole = authService.getUserRole(userId);
        Category category = categoryService.updateCategory(categoryId, name, description, parentId, userRole);
        return ResponseEntity.ok(category);
    }
    
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        Role userRole = authService.getUserRole(userId);
        categoryService.deleteCategory(categoryId, userRole);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }
    
    @GetMapping("/top-level")
    public ResponseEntity<List<Category>> getTopLevelCategories() {
        List<Category> categories = categoryService.getTopLevelCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping
    public ResponseEntity<Page<Category>> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<Category>> getSubcategories(@PathVariable Long parentId) {
        List<Category> subcategories = categoryService.getSubcategories(parentId);
        return ResponseEntity.ok(subcategories);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String query) {
        List<Category> categories = categoryService.searchCategoriesByName(query);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{categoryId}/path")
    public ResponseEntity<List<Category>> getCategoryPath(@PathVariable Long categoryId) {
        List<Category> path = categoryService.getCategoryPath(categoryId);
        return ResponseEntity.ok(path);
    }
} 