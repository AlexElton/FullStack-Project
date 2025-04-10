package ntnu.idi.bidata.IDATT2105.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating or updating a review.
 */
public class CreateReviewDTO {
    
    @NotNull(message = "Reviewed user ID is required")
    private Long reviewedUserId;
    
    private Long transactionId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int rating;
    
    @Size(max = 1000, message = "Review text cannot exceed 1000 characters")
    private String reviewText;
    
    // Default constructor
    public CreateReviewDTO() {
    }
    
    // Constructor with fields
    public CreateReviewDTO(Long reviewedUserId, Long transactionId, int rating, String reviewText) {
        this.reviewedUserId = reviewedUserId;
        this.transactionId = transactionId;
        this.rating = rating;
        this.reviewText = reviewText;
    }
    
    // Getters and setters
    public Long getReviewedUserId() {
        return reviewedUserId;
    }
    
    public void setReviewedUserId(Long reviewedUserId) {
        this.reviewedUserId = reviewedUserId;
    }
    
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getReviewText() {
        return reviewText;
    }
    
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}