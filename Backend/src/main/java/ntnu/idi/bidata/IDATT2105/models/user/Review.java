package ntnu.idi.bidata.IDATT2105.models.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
import ntnu.idi.bidata.IDATT2105.models.transaction.Transaction;

/**
 * JPA entity representing a user review in the system.
 */
@Entity
@Table(name = "reviews", indexes = {
  @Index(name = "idx_reviewed_user", columnList = "reviewed_user_id"),
  @Index(name = "idx_rating", columnList = "rating")
})
public class Review {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reviewer_id", nullable = false)
  private User reviewer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reviewed_user_id", nullable = false)
  private User reviewedUser;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_id")
  private Transaction transaction;

  @Column(nullable = false)
  private Byte rating;

  @Column(name = "review_text", columnDefinition = "TEXT")
  private String reviewText;

  @Column(name = "is_visible", nullable = false)
  private Boolean isVisible = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // Default constructor
  public Review() {
  }

  // Constructor with required fields
  public Review(User reviewer, User reviewedUser, Byte rating) {
    this.reviewer = reviewer;
    this.reviewedUser = reviewedUser;
    this.rating = rating;
  }

  // Constructor with all fields
  public Review(User reviewer, User reviewedUser, Transaction transaction, 
                Byte rating, String reviewText) {
    this.reviewer = reviewer;
    this.reviewedUser = reviewedUser;
    this.transaction = transaction;
    this.rating = rating;
    this.reviewText = reviewText;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getReviewer() {
    return reviewer;
  }

  public void setReviewer(User reviewer) {
    this.reviewer = reviewer;
  }

  public User getReviewedUser() {
    return reviewedUser;
  }

  public void setReviewedUser(User reviewedUser) {
    this.reviewedUser = reviewedUser;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public Byte getRating() {
    return rating;
  }

  public void setRating(Byte rating) {
    this.rating = rating;
  }

  public String getReviewText() {
    return reviewText;
  }

  public void setReviewText(String reviewText) {
    this.reviewText = reviewText;
  }

  public Boolean getIsVisible() {
    return isVisible;
  }

  public void setIsVisible(Boolean isVisible) {
    this.isVisible = isVisible;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Review review = (Review) o;
    return id.equals(review.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Review{" +
           "id=" + id +
           ", reviewer=" + reviewer.getUsername() +
           ", reviewedUser=" + reviewedUser.getUsername() +
           ", rating=" + rating +
           ", isVisible=" + isVisible +
           ", createdAt=" + createdAt +
           "}";
  }
}