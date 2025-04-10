package ntnu.idi.bidata.IDATT2105.models.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.items.Item;

/**
 * JPA entity representing a user's favorite/bookmarked item.
 * This is a junction table with a composite key.
 */
@Entity
@Table(name = "favorites", indexes = {
  @Index(name = "idx_user_item", columnList = "user_id, item_id")
})
@IdClass(FavoriteId.class)
public class Favorite {
  
  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Id
  @ManyToOne
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /**
   * Default constructor required by JPA.
   */
  public Favorite() {
  }

  /**
   * Constructor with user and item parameters.
   *
   * @param user the user who favorited the item
   * @param item the item that was favorited
   */
  public Favorite(User user, Item item) {
    this.user = user;
    this.item = item;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Favorite favorite = (Favorite) o;
    return user.equals(favorite.user) && item.equals(favorite.item);
  }

  @Override
  public int hashCode() {
    int result = user.hashCode();
    result = 31 * result + item.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Favorite{" +
        "user=" + user.getId() +
        ", item=" + item.getItemId() +
        ", createdAt=" + createdAt +
        '}';
  }
}
