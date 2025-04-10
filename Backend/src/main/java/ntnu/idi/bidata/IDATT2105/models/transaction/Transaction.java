package ntnu.idi.bidata.IDATT2105.models.transaction;

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
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.enums.PaymentMethod;
import ntnu.idi.bidata.IDATT2105.models.enums.TransactionStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.Offer;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Entity class representing a transaction between users.
 */
@Entity
@Table(name = "transactions", indexes = {
  @Index(name = "idx_item", columnList = "item_id"),
  @Index(name = "idx_seller", columnList = "seller_id"),
  @Index(name = "idx_buyer", columnList = "buyer_id"),
  @Index(name = "idx_status", columnList = "status"),
  @Index(name = "idx_created", columnList = "created_at")
})
public class Transaction {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  private User seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", nullable = false)
  private User buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offer_id")
  private Offer offer;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false, length = 3)
  private String currency = "NOK";

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentMethod paymentMethod;

  @Column(name = "payment_reference", length = 100)
  private String paymentReference;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionStatus status = TransactionStatus.PENDING;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  /**
   * Default constructor for JPA.
   */
  public Transaction() {
  }

  /**
   * Constructor for creating a transaction with required fields.
   * 
   * @param item the item involved in the transaction
   * @param seller the seller of the item
   * @param buyer the buyer of the item
   * @param amount the amount of money involved in the transaction
   * @param paymentMethod the payment method used for the transaction
   */
  public Transaction(Item item, User seller, User buyer, BigDecimal amount, PaymentMethod paymentMethod) {
    this.item = item;
    this.seller = seller;
    this.buyer = buyer;
    this.amount = amount;
    this.paymentMethod = paymentMethod;
  }

  // Getters and setters
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

  public User getSeller() {
    return seller;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }

  public User getBuyer() {
    return buyer;
  }

  public void setBuyer(User buyer) {
    this.buyer = buyer;
  }

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
  }

  /**
   * Override equals and hashCode methods to compare transactions based on their ID.
   * 
   * @param o the object to compare to
   * @return true if the object is equal to this transaction, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Transaction transaction = (Transaction) o;
    return id.equals(transaction.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Transaction{" +
           "id=" + id +
           ", item=" + item.getItemId() +
           ", seller=" + seller.getUsername() +
           ", buyer=" + buyer.getUsername() +
           ", amount=" + amount +
           ", currency='" + currency + '\'' +
           ", status=" + status +
           ", createdAt=" + createdAt +
           '}';
  }
}
