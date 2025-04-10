package ntnu.idi.bidata.IDATT2105.dtos;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for review information.
 */
public class ReviewDTO {
    
    private Long id;
    private Long reviewerId;
    private String reviewerUsername;
    private Long reviewedUserId;
    private String reviewedUserUsername;
    private Long transactionId;
    private byte rating;
    private String reviewText;
    private boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default constructor
    public ReviewDTO() {
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getReviewerId() {
        return reviewerId;
    }
    
    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    public String getReviewerUsername() {
        return reviewerUsername;
    }
    
    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }
    
    public Long getReviewedUserId() {
        return reviewedUserId;
    }
    
    public void setReviewedUserId(Long reviewedUserId) {
        this.reviewedUserId = reviewedUserId;
    }
    
    public String getReviewedUserUsername() {
        return reviewedUserUsername;
    }
    
    public void setReviewedUserUsername(String reviewedUserUsername) {
        this.reviewedUserUsername = reviewedUserUsername;
    }
    
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public byte getRating() {
        return rating;
    }
    
    public void setRating(byte rating) {
        this.rating = rating;
    }
    
    public String getReviewText() {
        return reviewText;
    }
    
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}