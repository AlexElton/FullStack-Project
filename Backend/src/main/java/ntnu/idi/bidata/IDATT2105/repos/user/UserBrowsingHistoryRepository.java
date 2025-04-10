package ntnu.idi.bidata.IDATT2105.repos.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.models.user.UserBrowsingHistory;

/**
 * Repository interface for UserBrowsingHistory entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for tracking user browsing history and generating recommendations.
 */
@Repository
public interface UserBrowsingHistoryRepository extends JpaRepository<UserBrowsingHistory, Long> {
  
  /**
   * Finds browsing history for a specific user, ordered by view date (newest first).
   * 
   * @param user the user to find browsing history for
   * @return a list of browsing history entries for the specified user, ordered by view date
   */
  List<UserBrowsingHistory> findByUserOrderByLastViewedAtDesc(User user);
  
  /**
   * Finds browsing history for a specific user, ordered by view count (highest first), with pagination.
   * 
   * @param user the user to find browsing history for
   * @param pageable the pagination information
   * @return a paginated list of browsing history entries for the specified user, ordered by view count
   */
  Page<UserBrowsingHistory> findByUserOrderByViewCountDesc(User user, Pageable pageable);
  
  /**
   * Finds the browsing history entry for a specific user and item.
   * 
   * @param user the user who viewed the item
   * @param item the item that was viewed
   * @return an Optional containing the browsing history entry if found, or empty if not found
   */
  Optional<UserBrowsingHistory> findByUserAndItem(User user, Item item);
  
  /**
   * Finds the most viewed items by a specific user, ordered by view count and last viewed date.
   * 
   * @param user the user to find most viewed items for
   * @param pageable the pagination information
   * @return a list of items most viewed by the specified user
   */
  @Query("SELECT h.item FROM UserBrowsingHistory h WHERE h.user = ?1 ORDER BY h.viewCount DESC, h.lastViewedAt DESC")
  List<Item> findMostViewedItemsByUser(User user, Pageable pageable);
  
  /**
   * Finds trending items based on the number of users who have viewed them.
   * 
   * @param pageable the pagination information
   * @return a list of trending items
   */
  @Query("SELECT h.item FROM UserBrowsingHistory h GROUP BY h.item ORDER BY COUNT(h.user) DESC")
  List<Item> findTrendingItems(Pageable pageable);
}