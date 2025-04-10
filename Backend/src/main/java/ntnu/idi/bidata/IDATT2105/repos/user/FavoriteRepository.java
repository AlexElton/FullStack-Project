package ntnu.idi.bidata.IDATT2105.repos.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.user.Favorite;
import ntnu.idi.bidata.IDATT2105.models.user.FavoriteId;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Favorite entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for user favorites/bookmarks.
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
  
  /**
   * Finds all favorites for a specific user.
   * 
   * @param user the user to find favorites for
   * @return a list of favorites for the specified user
   */
  List<Favorite> findByUser(User user);
  
  /**
   * Finds favorites for a specific user, ordered by creation date (most recent first), with pagination.
   * 
   * @param user the user to find favorites for
   * @param pageable the pagination information
   * @return a paginated list of favorites for the specified user, ordered by creation date
   */
  Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
  
  /**
   * Finds all favorites for a specific item.
   * 
   * @param item the item to find favorites for
   * @return a list of favorites for the specified item
   */
  List<Favorite> findByItem(Item item);
  
  /**
   * Counts the number of users who have favorited a specific item.
   * 
   * @param item the item to count favorites for
   * @return the number of users who have favorited the specified item
   */
  @Query("SELECT COUNT(f) FROM Favorite f WHERE f.item = ?1")
  long countByItem(Item item);
  
  /**
   * Checks if a user has favorited a specific item.
   * 
   * @param user the user to check
   * @param item the item to check
   * @return true if the user has favorited the item, false otherwise
   */
  boolean existsByUserAndItem(User user, Item item);
  
  /**
   * Deletes a favorite for a specific user and item.
   * 
   * @param user the user whose favorite should be deleted
   * @param item the item to remove from favorites
   */
  void deleteByUserAndItem(User user, Item item);
}