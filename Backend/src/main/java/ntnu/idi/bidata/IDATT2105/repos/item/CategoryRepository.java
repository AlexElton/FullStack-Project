package ntnu.idi.bidata.IDATT2105.repos.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.items.Category;

/**
 * Repository interface for Category entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Finds all top-level categories (categories with no parent).
   * 
   * @return a list of top-level categories
   */
  List<Category> findByParentCategoryIsNull();

  /**
   * Finds all child categories of a specific parent category.
   * 
   * @param parentCategory the parent category
   * @return a list of child categories
   */
  List<Category> findByParentCategory(Category parentCategory);

  /**
   * Finds all active categories.
   * 
   * @return a list of active categories
   */
  List<Category> findByIsActiveTrue();

  /**
   * Finds a category by name.
   * 
   * @param name the name of the category
   * @return an Optional containing the Category if found, or empty if not found
   */
  Optional<Category> findByName(String name);

  /**
   * Finds all categories ordered by their display order.
   * 
   * @return a list of categories ordered by display order
   */
  List<Category> findByOrderByDisplayOrderAsc();

  /**
   * Finds all top-level categories and eagerly fetches their child categories.
   * 
   * @return a list of top-level categories with their child categories
   */
  @Query("SELECT c FROM Category c LEFT JOIN FETCH c.childCategories WHERE c.parentCategory IS NULL")
  List<Category> findAllWithChildren();

  /**
   * Counts the number of child categories for a given parent category.
   * 
   * @param parentCategory the parent category
   * @return the number of child categories
   */
  @Query("SELECT COUNT(c) FROM Category c WHERE c.parentCategory = ?1")
  int countByParentCategory(Category parentCategory);

  /**
   * Finds categories whose names contain the specified query string (case-insensitive).
   * 
   * @param query the query string to search for in category names
   * @return a list of categories matching the query
   */
  @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
  List<Category> findByNameContainingIgnoreCase(String query);

  /**
   * Finds the path of categories from the root to the specified category.
   * 
   * @param categoryId the ID of the category
   * @return a list of categories representing the path from the root to the specified category
   */
  @Query("WITH RECURSIVE category_path AS (" +
         "  SELECT c FROM Category c WHERE c.categoryId = ?1 " +
         "  UNION ALL " +
         "  SELECT c FROM Category c JOIN category_path cp ON c.categoryId = cp.parentCategory.categoryId" +
         ") SELECT cp FROM category_path")
  List<Category> findCategoryPathById(Long categoryId);
}