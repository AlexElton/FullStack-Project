package ntnu.idi.bidata.IDATT2105.services.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;
import ntnu.idi.bidata.IDATT2105.models.notification.Notification;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.models.user.UserSettings;
import ntnu.idi.bidata.IDATT2105.repos.notification.NotificationRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserSettingsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;
    
    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                              UserRepository userRepository,
                              UserSettingsRepository userSettingsRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.userSettingsRepository = userSettingsRepository;
    }
    
    /**
     * Creates a new notification
     * 
     * @param userId the recipient user ID
     * @param type the notification type
     * @param referenceId ID of the related entity (item, offer, etc.)
     * @param title the notification title
     * @param message the notification message
     * @return the created notification
     */
    @Transactional
    public Notification createNotification(Long userId, NotificationType type, 
                                          Long referenceId, String title, String message) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        // Check user notification preferences
        UserSettings settings = userSettingsRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User settings not found", HttpStatus.NOT_FOUND));
        
        // Skip if notifications are disabled
        if (type == NotificationType.SYSTEM && !settings.getPushNotifications()) {
            return null;
        }
        
        // Create notification
        Notification notification = new Notification(user, type, referenceId, title, message);
        return notificationRepository.save(notification);
    }
    
    /**
     * Gets all notifications for a user with pagination
     * 
     * @param userId the user ID
     * @param pageable pagination parameters
     * @return page of notifications for the user
     */
    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }
    
    /**
     * Gets unread notifications for a user
     * 
     * @param userId the user ID
     * @return list of unread notifications for the user
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return notificationRepository.findByUserAndIsReadFalse(user);
    }
    
    /**
     * Gets the count of unread notifications for a user
     * 
     * @param userId the user ID
     * @return the count of unread notifications
     */
    public int getUnreadNotificationCount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return notificationRepository.countByUserAndIsReadFalse(user);
    }
    
    /**
     * Marks a notification as read
     * 
     * @param notificationId the notification ID
     * @param userId the user ID (for verification)
     * @return the updated notification
     */
    @Transactional
    public Notification markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ApiException("Notification not found", HttpStatus.NOT_FOUND));
        
        // Verify ownership
        if (!notification.getUser().getId().equals(userId)) {
            throw new ApiException("You can only update your own notifications", HttpStatus.FORBIDDEN);
        }
        
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }
    
    /**
     * Marks all notifications for a user as read
     * 
     * @param userId the user ID
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        notificationRepository.markAllAsRead(user);
    }
    
    /**
     * Deletes old read notifications
     * 
     * @param daysOld the minimum age in days
     * @return the number of notifications deleted
     */
    @Transactional
    public int deleteOldReadNotifications(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        
        List<Notification> oldNotifications = notificationRepository.findByIsReadTrueAndCreatedAtBefore(threshold);
        int count = oldNotifications.size();
        
        notificationRepository.deleteAll(oldNotifications);
        return count;
    }
}