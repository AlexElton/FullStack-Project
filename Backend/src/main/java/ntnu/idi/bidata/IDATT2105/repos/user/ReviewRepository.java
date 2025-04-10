package ntnu.idi.bidata.IDATT2105.repos.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.transaction.Transaction;
import ntnu.idi.bidata.IDATT2105.models.user.Review;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Review entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for user reviews and ratings.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  
  /**
   * Finds all reviews written by a specific user.
   * 
   * @param reviewer the user who wrote the reviews
   * @return a list of reviews written by the specified user
   */
  List<Review> findByReviewer(User reviewer);
  
  /**
   * Finds all reviews about a specific user.
   * 
   * @param reviewedUser the user who received the reviews
   * @return a list of reviews about the specified user
   */
  List<Review> findByReviewedUser(User reviewedUser);
  
  /**
   * Finds reviews about a specific user, ordered by creation date (newest first), with pagination.
   * 
   * @param reviewedUser the user who received the reviews
   * @param pageable the pagination information
   * @return a paginated list of reviews about the specified user, ordered by creation date
   */
  Page<Review> findByReviewedUserOrderByCreatedAtDesc(User reviewedUser, Pageable pageable);
  
  /**
   * Finds all reviews associated with a specific transaction.
   * 
   * @param transaction the transaction to find reviews for
   * @return a list of reviews associated with the specified transaction
   */
  List<Review> findByTransaction(Transaction transaction);
  
  /**
   * Finds the review written by a specific user for a specific transaction.
   * 
   * @param transaction the transaction to find the review for
   * @param reviewer the user who wrote the review
   * @return an Optional containing the review if found, or empty if not found
   */
  Optional<Review> findByTransactionAndReviewer(Transaction transaction, User reviewer);
  
  /**
   * Calculates the average rating for a specific user from visible reviews.
   * 
   * @param user the user to calculate the average rating for
   * @return the average rating for the specified user, or null if no reviews exist
   */
  @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedUser = ?1 AND r.isVisible = true")
  Double calculateAverageRating(User user);
  
  /**
   * Counts the number of visible reviews for a specific user.
   * 
   * @param user the user to count reviews for
   * @return the count of visible reviews for the specified user
   */
  @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedUser = ?1 AND r.isVisible = true")
  Long countVisibleReviews(User user);
  
  /**
   * Finds all visible reviews.
   * 
   * @return a list of visible reviews
   */
  List<Review> findByIsVisibleTrue();
  
  /**
   * Finds all hidden reviews.
   * 
   * @return a list of hidden reviews
   */
  List<Review> findByIsVisibleFalse();
}