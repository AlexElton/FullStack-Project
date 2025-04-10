package ntnu.idi.bidata.IDATT2105.models.items;

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

/**
 * JPA entity representing an image associated with an item listing.
 */
@Entity
@Table(name = "item_images", indexes = {
  @Index(name = "idx_item_id", columnList = "item_id")
})
public class ItemImage {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "image_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "is_primary", nullable = false)
  private Boolean isPrimary = false;

  @Column(name = "display_order", nullable = false)
  private Integer displayOrder = 0;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /**
   * Default constructor for JPA.
   */
  public ItemImage() {
  }


  /**
   * Constructor for creating an ItemImage with item and imageUrl.
   * This constructor is used when creating a new image for an item.
   *
   * @param item the item associated with the image
   * @param imageUrl the URL of the image
   */
  public ItemImage(Item item, String imageUrl) {
    this.item = item;
    this.imageUrl = imageUrl;
  }

  // Constructor with all fields
  public ItemImage(Item item, String imageUrl, Boolean isPrimary, Integer displayOrder) {
    this.item = item;
    this.imageUrl = imageUrl;
    this.isPrimary = isPrimary;
    this.displayOrder = displayOrder;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Boolean getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(Boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    ItemImage image = (ItemImage) o;
    return id.equals(image.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "ItemImage{" +
           "id=" + id +
           ", item=" + item.getItemId() +
           ", imageUrl='" + imageUrl + '\'' +
           ", isPrimary=" + isPrimary +
           ", displayOrder=" + displayOrder +
           ", createdAt=" + createdAt +
           '}';
  }
}
