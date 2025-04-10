package ntnu.idi.bidata.IDATT2105.repos.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;
import ntnu.idi.bidata.IDATT2105.models.notification.Notification;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Notification entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for user notifications.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  
  /**
   * Finds all notifications for a specific user.
   * 
   * @param user the user to find notifications for
   * @return a list of notifications for the specified user
   */
  List<Notification> findByUser(User user);
  
  /**
   * Finds notifications for a specific user, ordered by creation date (newest first), with pagination.
   * 
   * @param user the user to find notifications for
   * @param pageable the pagination information
   * @return a paginated list of notifications for the specified user, ordered by creation date
   */
  Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
  
  /**
   * Finds unread notifications for a specific user.
   * 
   * @param user the user to find unread notifications for
   * @return a list of unread notifications for the specified user
   */
  List<Notification> findByUserAndIsReadFalse(User user);
  
  /**
   * Counts the number of unread notifications for a specific user.
   * 
   * @param user the user to count unread notifications for
   * @return the count of unread notifications for the specified user
   */
  int countByUserAndIsReadFalse(User user);
  
  /**
   * Finds all notifications of a specific type.
   * 
   * @param type the notification type to filter by
   * @return a list of notifications of the specified type
   */
  List<Notification> findByType(NotificationType type);
  
  /**
   * Finds notifications for a specific user of a specific type.
   * 
   * @param user the user to find notifications for
   * @param type the notification type to filter by
   * @return a list of notifications for the specified user of the specified type
   */
  List<Notification> findByUserAndType(User user, NotificationType type);
  
  /**
   * Finds notifications with a specific reference ID.
   * 
   * @param referenceId the reference ID to filter by (e.g., item ID, offer ID)
   * @return a list of notifications with the specified reference ID
   */
  List<Notification> findByReferenceId(Long referenceId);
  
  /**
   * Marks all notifications as read for a specific user.
   * 
   * @param user the user whose notifications should be marked as read
   */
  @Modifying
  @Transactional
  @Query("UPDATE Notification n SET n.isRead = true WHERE n.user = ?1")
  void markAllAsRead(User user);
  
  /**
   * Deletes read notifications older than a specified date.
   * 
   * @param date the date threshold for deletion
   */
  @Modifying
  @Transactional
  @Query("DELETE FROM Notification n WHERE n.isRead = true AND n.createdAt < ?1")
  void deleteReadNotificationsOlderThan(LocalDateTime date);

  /**
   * Finds read notifications older than a specified date.
   * 
   * @param threshold the date threshold for filtering
   * @return a list of read notifications older than the specified date
   */
  @Query("SELECT n FROM Notification n WHERE n.isRead = true AND n.createdAt < ?1")
  @Transactional(readOnly = true)
  List<Notification> findByIsReadTrueAndCreatedAtBefore(LocalDateTime threshold);
}