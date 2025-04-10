package ntnu.idi.bidata.IDATT2105.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.dtos.CreateReviewDTO;
import ntnu.idi.bidata.IDATT2105.dtos.ReviewDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;
import ntnu.idi.bidata.IDATT2105.models.enums.TransactionStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.Role;
import ntnu.idi.bidata.IDATT2105.models.transaction.Transaction;
import ntnu.idi.bidata.IDATT2105.models.user.Review;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.repos.transaction.TransactionRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.ReviewRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.services.notification.NotificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service for managing user reviews.
 */
@Service
public class ReviewService {
    
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());
    
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    
    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                        UserRepository userRepository,
                        TransactionRepository transactionRepository,
                        NotificationService notificationService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }
    
    /**
     * Creates a new review
     * 
     * @param reviewerId the reviewer ID
     * @param reviewDTO the review details
     * @return the created review
     */
    @Transactional
    public Review createReview(Long reviewerId, CreateReviewDTO reviewDTO) {
        User reviewer = userRepository.findById(reviewerId)
            .orElseThrow(() -> new ApiException("Reviewer not found", HttpStatus.NOT_FOUND));
            
        User reviewedUser = userRepository.findById(reviewDTO.getReviewedUserId())
            .orElseThrow(() -> new ApiException("Reviewed user not found", HttpStatus.NOT_FOUND));
            
        // Cannot review yourself
        if (reviewer.getId().equals(reviewedUser.getId())) {
            throw new ApiException("You cannot review yourself", HttpStatus.BAD_REQUEST);
        }
        
        // Check rating range (1-5)
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new ApiException("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
        }
        
        Transaction transaction = null;
        
        // If transaction ID is provided, verify it
        if (reviewDTO.getTransactionId() != null) {
            transaction = transactionRepository.findById(reviewDTO.getTransactionId())
                .orElseThrow(() -> new ApiException("Transaction not found", HttpStatus.NOT_FOUND));
                
            // Verify transaction involves both users
            boolean isValidTransaction = (transaction.getBuyer().getId().equals(reviewerId) && 
                                        transaction.getSeller().getId().equals(reviewDTO.getReviewedUserId())) ||
                                       (transaction.getSeller().getId().equals(reviewerId) && 
                                        transaction.getBuyer().getId().equals(reviewDTO.getReviewedUserId()));
                                        
            if (!isValidTransaction) {
                throw new ApiException("Invalid transaction for these users", HttpStatus.BAD_REQUEST);
            }
            
            // Check if transaction is completed
            if (transaction.getStatus() != TransactionStatus.COMPLETED) {
                throw new ApiException("Can only review completed transactions", HttpStatus.BAD_REQUEST);
            }
            
            // Check if user already reviewed this transaction
            Optional<Review> existingReview = reviewRepository.findByTransactionAndReviewer(transaction, reviewer);
            if (existingReview.isPresent()) {
                throw new ApiException("You have already reviewed this transaction", HttpStatus.CONFLICT);
            }
        }
        
        try {
            // Create review
            Review review = new Review();
            review.setReviewer(reviewer);
            review.setReviewedUser(reviewedUser);
            review.setTransaction(transaction);
            review.setRating((byte) reviewDTO.getRating());
            review.setReviewText(reviewDTO.getReviewText());
            review.setIsVisible(true); // Default to visible
            review.setCreatedAt(LocalDateTime.now());
            
            Review savedReview = reviewRepository.save(review);
            
            // Notify user about the review
            notificationService.createNotification(
                reviewedUser.getId(),
                NotificationType.REVIEW,
                savedReview.getId(),
                "New review received",
                reviewer.getUsername() + " left you a " + reviewDTO.getRating() + "-star review"
            );
            
            logger.info("New review created by user ID: " + reviewerId + 
                       " for user ID: " + reviewDTO.getReviewedUserId());
            return savedReview;
        } catch (Exception e) {
            logger.severe("Error creating review: " + e.getMessage());
            throw new ApiException("Failed to create review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Updates an existing review
     * 
     * @param reviewId the review ID
     * @param reviewerId the reviewer ID (for verification)
     * @param reviewDTO the updated review details
     * @return the updated review
     */
    @Transactional
    public Review updateReview(Long reviewId, Long reviewerId, CreateReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ApiException("Review not found", HttpStatus.NOT_FOUND));
            
        // Verify ownership
        if (!review.getReviewer().getId().equals(reviewerId)) {
            throw new ApiException("You can only update your own reviews", HttpStatus.FORBIDDEN);
        }
        
        // Check rating range
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new ApiException("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Update review
            review.setRating((byte) reviewDTO.getRating());
            review.setReviewText(reviewDTO.getReviewText());
            review.setUpdatedAt(LocalDateTime.now());
            
            Review updatedReview = reviewRepository.save(review);
            
            // Notify user about updated review
            notificationService.createNotification(
                review.getReviewedUser().getId(),
                NotificationType.REVIEW,
                updatedReview.getId(),
                "Review updated",
                "A review from " + review.getReviewer().getUsername() + " was updated"
            );
            
            logger.info("Review ID: " + reviewId + " updated by user ID: " + reviewerId);
            return updatedReview;
        } catch (Exception e) {
            logger.severe("Error updating review: " + e.getMessage());
            throw new ApiException("Failed to update review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Deletes a review
     * 
     * @param reviewId the review ID
     * @param userId the user ID (must be reviewer or admin)
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ApiException("Review not found", HttpStatus.NOT_FOUND));
            
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        // Check if user is reviewer or admin
        boolean isReviewer = review.getReviewer().getId().equals(userId);
        boolean isAdmin = user.getRole() == Role.ADMIN;
        
        if (!isReviewer && !isAdmin) {
            throw new ApiException("You can only delete your own reviews", HttpStatus.FORBIDDEN);
        }
        
        try {
            reviewRepository.delete(review);
            
            // Notify user if admin deleted their review
            if (isAdmin && !isReviewer) {
                notificationService.createNotification(
                    review.getReviewer().getId(),
                    NotificationType.SYSTEM,
                    null,
                    "Review removed",
                    "Your review for " + review.getReviewedUser().getUsername() + 
                    " was removed by an administrator"
                );
            }
            
            logger.info("Review ID: " + reviewId + " deleted by user ID: " + userId);
        } catch (Exception e) {
            logger.severe("Error deleting review: " + e.getMessage());
            throw new ApiException("Failed to delete review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Gets a review by ID
     * 
     * @param reviewId the review ID
     * @return the review
     */
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ApiException("Review not found", HttpStatus.NOT_FOUND));
    }
    
    /**
     * Gets reviews made by a specific user
     * 
     * @param reviewerId the reviewer ID
     * @param pageable pagination parameters
     * @return page of reviews made by the user
     */
    public Page<Review> getReviewsByReviewer(Long reviewerId, Pageable pageable) {
        User reviewer = userRepository.findById(reviewerId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        return reviewRepository.findByReviewerOrderByCreatedAtDesc(reviewer, pageable);
    }
    
    /**
     * Gets average rating for a user
     * 
     * @param userId the user ID
     * @return the average rating
     */
    public double getAverageRatingForUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        Double rating = reviewRepository.calculateAverageRating(user);
        return rating != null ? rating : 0.0;
    }
    
    /**
     * Gets review count for a user
     * 
     * @param userId the user ID
     * @return the review count
     */
    public long getReviewCountForUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        return reviewRepository.countVisibleReviews(user);
    }
    
    /**
     * Moderates a review (admin only)
     * 
     * @param reviewId the review ID
     * @param adminId the admin user ID
     * @param visible whether the review should be visible
     * @return the updated review
     */
    @Transactional
    public Review moderateReview(Long reviewId, Long adminId, boolean visible) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ApiException("Review not found", HttpStatus.NOT_FOUND));
            
        User admin = userRepository.findById(adminId)
            .orElseThrow(() -> new ApiException("Admin not found", HttpStatus.NOT_FOUND));
            
        // Verify admin role
        if (admin.getRole() != Role.ADMIN) {
            throw new ApiException("Only administrators can moderate reviews", HttpStatus.FORBIDDEN);
        }
        
        try {
            review.setIsVisible(visible);
            Review updatedReview = reviewRepository.save(review);
            
            // Notify the reviewer
            notificationService.createNotification(
                review.getReviewer().getId(),
                NotificationType.SYSTEM,
                review.getId(),
                visible ? "Review approved" : "Review hidden",
                visible ? 
                    "Your review for " + review.getReviewedUser().getUsername() + " is now visible" :
                    "Your review for " + review.getReviewedUser().getUsername() + " has been hidden by a moderator"
            );
            
            logger.info("Review ID: " + reviewId + " moderated by admin ID: " + adminId + 
                       ", visibility set to: " + visible);
            return updatedReview;
        } catch (Exception e) {
            logger.severe("Error moderating review: " + e.getMessage());
            throw new ApiException("Failed to moderate review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Checks if a user can review another user based on transaction history
     * 
     * @param reviewerId the potential reviewer
     * @param reviewedUserId the user to be reviewed
     * @return true if review is allowed, false otherwise
     */
    public boolean canReviewUser(Long reviewerId, Long reviewedUserId) {
        if (reviewerId.equals(reviewedUserId)) {
            return false; // Cannot review yourself
        }
        
        // Check if there's a completed transaction between users
        List<Transaction> transactions = transactionRepository
            .findCompletedTransactionsBetweenUsers(reviewerId, reviewedUserId);
            
        return !transactions.isEmpty();
    }
    
    /**
     * Maps a Review entity to a ReviewDTO
     * 
     * @param review the review entity
     * @return the review DTO
     */
    public ReviewDTO mapReviewToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewerUsername(review.getReviewer().getUsername());
        dto.setReviewedUserId(review.getReviewedUser().getId());
        dto.setReviewedUserUsername(review.getReviewedUser().getUsername());
        
        if (review.getTransaction() != null) {
            dto.setTransactionId(review.getTransaction().getId());
        }
        
        dto.setRating(review.getRating());
        dto.setReviewText(review.getReviewText());
        dto.setVisible(review.getIsVisible());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        
        return dto;
    }
}