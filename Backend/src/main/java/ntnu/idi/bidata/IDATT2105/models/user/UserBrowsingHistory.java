package ntnu.idi.bidata.IDATT2105.models.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import ntnu.idi.bidata.IDATT2105.models.items.Item;

/**
 * JPA entity representing a user's browsing history for items.
 * Used for generating personalized recommendations.
 */
@Entity
@Table(name = "user_browsing_history", 
       uniqueConstraints = {
         @UniqueConstraint(name = "uk_user_item", columnNames = {"user_id", "item_id"})
       }, 
       indexes = {
         @Index(name = "idx_user_viewed", columnList = "user_id, last_viewed_at")
       })
public class UserBrowsingHistory {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "history_id")
  private Long historyId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @Column(name = "view_count", nullable = false)
  private Integer viewCount = 1;

  @CreationTimestamp
  @Column(name = "first_viewed_at", nullable = false, updatable = false)
  private LocalDateTime firstViewedAt;
  
  @Column(name = "last_viewed_at", nullable = false)
  private LocalDateTime lastViewedAt = LocalDateTime.now();

  // Default constructor
  public UserBrowsingHistory() {
  }

  // Constructor with required fields
  public UserBrowsingHistory(User user, Item item) {
    this.user = user;
    this.item = item;
  }

  public Long getHistoryId() {
    return historyId;
  }

  public void setHistoryId(Long historyId) {
    this.historyId = historyId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public Integer getViewCount() {
    return viewCount;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }

  public LocalDateTime getFirstViewedAt() {
    return firstViewedAt;
  }

  public LocalDateTime getLastViewedAt() {
    return lastViewedAt;
  }

  public void setLastViewedAt(LocalDateTime lastViewedAt) {
    this.lastViewedAt = lastViewedAt;
  }

  /**
   * Increments the view count by one.
   * Should be called when a user views the same item again.
   */
  public void incrementViewCount() {
    this.viewCount += 1;
    this.lastViewedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    UserBrowsingHistory that = (UserBrowsingHistory) o;
    return historyId.equals(that.historyId);
  }

  @Override
  public int hashCode() {
    return historyId.hashCode();
  }

  @Override
  public String toString() {
    return "UserBrowsingHistory{" +
           "historyId=" + historyId +
           ", user=" + user.getUsername() +
           ", item=" + item.getItemId() +
           ", viewCount=" + viewCount +
           ", firstViewedAt=" + firstViewedAt +
           ", lastViewedAt=" + lastViewedAt +
           '}';
  }
}
