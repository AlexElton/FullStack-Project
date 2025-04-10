package ntnu.idi.bidata.IDATT2105.services.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.Role;
import ntnu.idi.bidata.IDATT2105.models.items.Category;
import ntnu.idi.bidata.IDATT2105.repos.item.CategoryRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service for managing item categories.
 */
@Service
public class CategoryService {
    
    private static final Logger logger = Logger.getLogger(CategoryService.class.getName());
    
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }
    
    /**
     * Creates a new category
     * 
     * @param name the category name
     * @param description the category description
     * @param parentId the parent category ID (optional)
     * @param userRole the role of the user creating the category (for authorization)
     * @return the created category
     */
    @Transactional
    public Category createCategory(String name, String description, Long parentId, Role userRole) {
        // Only admins and moderators can create categories
        if (userRole != Role.ADMIN) {
            throw new ApiException("You don't have permission to create categories", HttpStatus.FORBIDDEN);
        }
        
        // Validate name
        if (name == null || name.trim().isEmpty()) {
            throw new ApiException("Category name is required", HttpStatus.BAD_REQUEST);
        }
        
        // Check if category name already exists
        if (categoryRepository.findByName(name).isPresent()) {
            throw new ApiException("A category with this name already exists", HttpStatus.CONFLICT);
        }
        
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            
            Category savedCategory = categoryRepository.save(category);
            logger.info("Created new category: " + name);
            return savedCategory;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error creating category: " + e.getMessage());
            throw new ApiException("Failed to create category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Updates an existing category
     * 
     * @param categoryId the category ID
     * @param name the updated name (optional)
     * @param description the updated description (optional)
     * @param parentId the updated parent category ID (optional)
     * @param userRole the role of the user updating the category (for authorization)
     * @return the updated category
     */
    @Transactional
    public Category updateCategory(Long categoryId, String name, 
                                  String description, Long parentId, Role userRole) {
        // Only admins and moderators can update categories
        if (userRole != Role.ADMIN) {
            throw new ApiException("You don't have permission to update categories", HttpStatus.FORBIDDEN);
        }
        
        Category category = getCategoryById(categoryId);
        
        try {
            // Update name if provided
            if (name != null && !name.trim().isEmpty()) {
                // Check if new name already exists for another category
                Optional<Category> existingCategory = categoryRepository.findByName(name);
                if (existingCategory.isPresent() && !existingCategory.get().getCategoryId().equals(categoryId)) {
                    throw new ApiException("A category with this name already exists", HttpStatus.CONFLICT);
                }
                category.setName(name);
            }
            
            // Update description if provided
            if (description != null) {
                category.setDescription(description);
            }
            
            // Update parent category if provided
            if (parentId != null) {
                // Prevent circular reference
                if (parentId.equals(categoryId)) {
                    throw new ApiException("A category cannot be its own parent", HttpStatus.BAD_REQUEST);
                }
                
                if (isCircularReference(categoryId, parentId)) {
                    throw new ApiException("This would create a circular reference in category hierarchy", 
                                          HttpStatus.BAD_REQUEST);
                }
                
                Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new ApiException("Parent category not found", HttpStatus.NOT_FOUND));
                category.setParentCategory(parent);
            } else if (parentId == null && name != null) { // If parentId is explicitly set to null, remove parent
                category.setParentCategory(null);
            }
            
            Category updatedCategory = categoryRepository.save(category);
            logger.info("Updated category ID: " + categoryId);
            return updatedCategory;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error updating category: " + e.getMessage());
            throw new ApiException("Failed to update category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Checks if setting a parent would create a circular reference
     * 
     * @param categoryId the category being updated
     * @param parentId the proposed parent category ID
     * @return true if there would be a circular reference
     */
    private boolean isCircularReference(Long categoryId, Long parentId) {
        Long currentParentId = parentId;
        
        while (currentParentId != null) {
            if (currentParentId.equals(categoryId)) {
                return true;
            }
            
            Category parent = categoryRepository.findById(currentParentId).orElse(null);
            if (parent == null || parent.getParentCategory() == null) {
                return false;
            }
            
            currentParentId = parent.getParentCategory().getCategoryId();
        }
        
        return false;
    }
    
    /**
     * Deletes a category
     * 
     * @param categoryId the category ID
     * @param userRole the role of the user deleting the category (for authorization)
     */
    @Transactional
    public void deleteCategory(Long categoryId, Role userRole) {
        // Only admins can delete categories
        if (userRole != Role.ADMIN) {
            throw new ApiException("Only administrators can delete categories", HttpStatus.FORBIDDEN);
        }
        
        Category category = getCategoryById(categoryId);
        
        // Check if category has items
        long itemCount = itemRepository.countByCategory(category);
        if (itemCount > 0) {
            throw new ApiException("Cannot delete category with items. Move or delete the items first.", 
                                  HttpStatus.CONFLICT);
        }
        
        // Check if category has children
        long childrenCount = categoryRepository.countByParentCategory(category);
        if (childrenCount > 0) {
            throw new ApiException("Cannot delete category with subcategories. Move or delete the subcategories first.", 
                                  HttpStatus.CONFLICT);
        }
        
        try {
            categoryRepository.delete(category);
            logger.info("Deleted category ID: " + categoryId);
        } catch (Exception e) {
            logger.severe("Error deleting category: " + e.getMessage());
            throw new ApiException("Failed to delete category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Gets a category by ID
     * 
     * @param categoryId the category ID
     * @return the category
     * @throws ApiException if category is not found
     */
    public Category getCategoryById(Long categoryId) {
        if (categoryId == null) {
            throw new ApiException("Category ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ApiException("Category not found", HttpStatus.NOT_FOUND));
    }
    
    /**
     * Gets all top-level categories (categories without a parent)
     * 
     * @return list of top-level categories
     */
    public List<Category> getTopLevelCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }
    
    /**
     * Gets all categories with pagination
     * 
     * @param pageable pagination parameters
     * @return page of categories
     */
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    /**
     * Gets direct subcategories of a category
     * 
     * @param parentId the parent category ID
     * @return list of subcategories
     */
    public List<Category> getSubcategories(Long parentId) {
        Category parent = getCategoryById(parentId);
        return categoryRepository.findByParentCategory(parent);
    }
    
    /**
     * Searches for categories by name
     * 
     * @param query the search query
     * @return list of matching categories
     */
    public List<Category> searchCategoriesByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new ApiException("Search query cannot be empty", HttpStatus.BAD_REQUEST);
        }
        
        return categoryRepository.findByNameContainingIgnoreCase(query);
    }
    
    /**
     * Gets the full category path (breadcrumbs)
     * 
     * @param categoryId the category ID
     * @return list of categories in the path, from root to the specified category
     */
    public List<Category> getCategoryPath(Long categoryId) {
        Category category = getCategoryById(categoryId);
        return categoryRepository.findCategoryPathById(categoryId);
    }
}