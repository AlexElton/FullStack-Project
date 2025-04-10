package ntnu.idi.bidata.IDATT2105.models.items;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemCondition;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * JPA entity representing an item listing in the system.
 * 
 * This class is used to map the items table in the database to a Java object.
 */
@Entity
@Table(name = "items", indexes = {
  @Index(name = "idx_seller", columnList = "seller_id"),
  @Index(name = "idx_category", columnList = "category_id"),
  @Index(name = "idx_status", columnList = "status"),
  @Index(name = "idx_location", columnList = "latitude, longitude"),
  @Index(name = "idx_created", columnList = "created_at"),
  // TODO Implement Fulltext index with SQL migration?
})
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long itemId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  private User seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(name = "brief_description", nullable = false, length = 255)
  private String briefDescription;

  @Column(name = "full_description", nullable = false, columnDefinition = "TEXT")
  private String fullDescription;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal price;

  @Column(nullable = false, length = 3)
  private String currency = "NOK";

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ItemCondition condition;

  @Column(nullable = false)
  private Integer quantity = 1;

  @Column(name = "allow_offers", nullable = false)
  private Boolean allowOffers = false;

  @Column(name = "accept_vipps", nullable = false) // TODO remove if no vipps
  private Boolean acceptVipps = true;

  @Column(name = "location_address")
  private String locationAddress;

  @Column(precision = 10, scale = 8)
  private BigDecimal latitude;

  @Column(precision = 11, scale = 8)
  private BigDecimal longitude;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ItemStatus status = ItemStatus.ACTIVE;

  @Column(name = "views_count", nullable = false)
  private Integer viewsCount = 0;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @OneToMany(mappedBy = "item")
  private List<ItemImage> images = new ArrayList<>();

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public User getSeller() {
    return seller;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBriefDescription() {
    return briefDescription;
  }

  public void setBriefDescription(String briefDescription) {
    this.briefDescription = briefDescription;
  }

  public String getFullDescription() {
    return fullDescription;
  }

  public void setFullDescription(String fullDescription) {
    this.fullDescription = fullDescription;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}