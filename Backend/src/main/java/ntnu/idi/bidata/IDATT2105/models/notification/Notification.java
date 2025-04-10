package ntnu.idi.bidata.IDATT2105.models.notification;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * JPA entity representing a notification in the system.
 */
@Entity
@Table(name = "notifications", indexes = {
  @Index(name = "idx_user_read", columnList = "user_id, is_read"),
  @Index(name = "idx_created", columnList = "created_at")
})
public class Notification {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(name = "reference_id")
  private Long referenceId;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String message;

  @Column(name = "is_read", nullable = false)
  private Boolean isRead = false;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /**
   * Default constructor for JPA.
   */
  public Notification() {
  }

  /**
   * Constructor for creating a notification with user, type, title, and message.
   * 
   * @param user the user associated with the notification
   * @param type the type of notification
   * @param title the title of the notification
   * @param message the message of the notification
   */
  public Notification(User user, NotificationType type, String title, String message) {
    this.user = user;
    this.type = type;
    this.title = title;
    this.message = message;
  }

  // Constructor with all fields
  public Notification(User user, NotificationType type, Long referenceId, 
                     String title, String message) {
    this.user = user;
    this.type = type;
    this.referenceId = referenceId;
    this.title = title;
    this.message = message;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public NotificationType getType() {
    return type;
  }

  public void setType(NotificationType type) {
    this.type = type;
  }

  public Long getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(Long referenceId) {
    this.referenceId = referenceId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(Boolean isRead) {
    this.isRead = isRead;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Notification that = (Notification) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Notification{" +
           "id=" + id +
           ", user=" + user.getUsername() +
           ", type=" + type +
           ", title='" + title + '\'' +
           ", isRead=" + isRead +
           ", createdAt=" + createdAt +
           "}";
  }
}