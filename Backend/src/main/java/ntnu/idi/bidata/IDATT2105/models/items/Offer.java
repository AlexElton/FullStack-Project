package ntnu.idi.bidata.IDATT2105.models.items;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.enums.OfferStatus;
import ntnu.idi.bidata.IDATT2105.models.transaction.Transaction;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Entity class representing an offer made by a buyer for an item.
 */
@Entity
@Table(name = "offers", indexes = {
  @Index(name = "idx_item", columnList = "item_id"),
  @Index(name = "idx_buyer", columnList = "buyer_id"),
  @Index(name = "idx_status", columnList = "status")
})
public class Offer {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "offer_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", nullable = false)
  private User buyer;

  @Column(name = "offer_amount", nullable = false, precision = 12, scale = 2)
  private BigDecimal offerAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OfferStatus status = OfferStatus.PENDING;

  @Column(name = "message", columnDefinition = "TEXT")
  private String message;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "offer", fetch = FetchType.LAZY)
  private Transaction transaction;

  // Default constructor
  public Offer() {
  }

  // Constructor with required fields
  public Offer(Item item, User buyer, BigDecimal offerAmount) {
    this.item = item;
    this.buyer = buyer;
    this.offerAmount = offerAmount;
  }

  // Constructor with all fields
  public Offer(Item item, User buyer, BigDecimal offerAmount, String message, LocalDateTime expiresAt) {
    this.item = item;
    this.buyer = buyer;
    this.offerAmount = offerAmount;
    this.message = message;
    this.expiresAt = expiresAt;
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

  public User getBuyer() {
    return buyer;
  }

  public void setBuyer(User buyer) {
    this.buyer = buyer;
  }

  public BigDecimal getOfferAmount() {
    return offerAmount;
  }

  public void setOfferAmount(BigDecimal offerAmount) {
    this.offerAmount = offerAmount;
  }

  public OfferStatus getStatus() {
    return status;
  }

  public void setStatus(OfferStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  
  /**
   * Compares this offer to another object for equality.
   * 
   * @param o the object to compare to
   * @return true if the object is an Offer with the same ID, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Offer offer = (Offer) o;
    return id.equals(offer.id);
  }

  /**
   * Returns the hash code of this offer.
   * 
   * @return the hash code of this offer
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Offer{" +
           "id=" + id +
           ", item=" + item.getItemId() +
           ", buyer=" + buyer.getUsername() +
           ", offerAmount=" + offerAmount +
           ", status=" + status +
           ", expiresAt=" + expiresAt +
           ", createdAt=" + createdAt +
           '}';
  }
}
