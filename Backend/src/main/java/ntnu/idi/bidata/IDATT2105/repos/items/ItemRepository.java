package ntnu.idi.bidata.IDATT2105.repos.items;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.enums.ItemCondition;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Category;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Item entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  /**
   * Finds all Items listed by a specific seller.
   * 
   * @param seller the User who listed the items
   * @return a list of Items from the specified seller
   */
  List<Item> findBySeller(User seller);

  /**
   * Finds all Items with a specific status, with pagination.
   * 
   * @param status   the status to filter by
   * @param pageable the pagination information
   * @return a paginated list of Items with the specified status
   */
  Page<Item> findByStatus(ItemStatus status, Pageable pageable);

  /**
   * Finds all Items in a specific category, with pagination.
   * 
   * @param category the category to filter by
   * @param pageable the pagination information
   * @return a paginated list of Items in the specified category
   */
  Page<Item> findByCategory(Category category, Pageable pageable);

  /**
   * Searches for Items whose title contains the specified text
   * (case-insensitive), with pagination.
   * 
   * @param title    the text to search for in item titles
   * @param pageable the pagination information
   * @return a paginated list of Items matching the search criteria
   */
  Page<Item> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  /**
   * Finds Items within a specified price range, with pagination.
   * 
   * @param minPrice the minimum price (inclusive)
   * @param maxPrice the maximum price (inclusive)
   * @param pageable the pagination information
   * @return a paginated list of Items within the specified price range
   */
  @Query("SELECT i FROM Item i WHERE i.price BETWEEN ?1 AND ?2")
  Page<Item> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

  /**
   * Finds all Items with a specific condition.
   * 
   * @param condition the condition to filter by
   * @return a list of Items with the specified condition
   */
  List<Item> findByCondition(ItemCondition condition);

  /**
   * Increments the view count of an Item by 1.
   * 
   * @param itemId the ID of the item to increment views for
   */
  @Modifying
  @Query("UPDATE Item i SET i.viewsCount = i.viewsCount + 1 WHERE i.id = ?1")
  void incrementViewsCount(Long itemId);

  /**
   * Finds all Items that have expired (expiration date is before the specified
   * date).
   * 
   * @param date the reference date
   * @return a list of Items that have expired
   */
  List<Item> findByExpiresAtBefore(LocalDateTime date);

  /**
   * Finds Items near a specified geographic location.
   * 
   * @param longitude      the longitude coordinate
   * @param latitude       the latitude coordinate
   * @param distanceMeters the maximum distance in meters
   * @return a list of Items within the specified distance from the location
   */
  @Query(value = "SELECT * FROM items WHERE ST_Distance_Sphere(point(longitude, latitude), point(?1, ?2)) <= ?3", nativeQuery = true)
  List<Item> findItemsNearLocation(double longitude, double latitude, double distanceMeters);
}