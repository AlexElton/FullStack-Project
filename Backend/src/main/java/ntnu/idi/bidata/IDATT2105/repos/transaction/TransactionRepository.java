package ntnu.idi.bidata.IDATT2105.repos.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.enums.TransactionStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.Offer;
import ntnu.idi.bidata.IDATT2105.models.transaction.Transaction;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Transaction entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for item purchase transactions.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  
  /**
   * Finds all transactions for a specific item.
   * 
   * @param item the item involved in the transactions
   * @return a list of transactions for the specified item
   */
  List<Transaction> findByItem(Item item);
  
  /**
   * Finds all transactions where a specific user is the seller.
   * 
   * @param seller the user who sold the items
   * @return a list of transactions where the specified user is the seller
   */
  List<Transaction> findBySeller(User seller);
  
  /**
   * Finds transactions where a specific user is the seller, ordered by creation date (newest first), with pagination.
   * 
   * @param seller the user who sold the items
   * @param pageable the pagination information
   * @return a paginated list of transactions where the specified user is the seller, ordered by creation date
   */
  Page<Transaction> findBySellerOrderByCreatedAtDesc(User seller, Pageable pageable);
  
  /**
   * Finds all transactions where a specific user is the buyer.
   * 
   * @param buyer the user who bought the items
   * @return a list of transactions where the specified user is the buyer
   */
  List<Transaction> findByBuyer(User buyer);
  
  /**
   * Finds transactions where a specific user is the buyer, ordered by creation date (newest first), with pagination.
   * 
   * @param buyer the user who bought the items
   * @param pageable the pagination information
   * @return a paginated list of transactions where the specified user is the buyer, ordered by creation date
   */
  Page<Transaction> findByBuyerOrderByCreatedAtDesc(User buyer, Pageable pageable);
  
  /**
   * Finds all transactions resulting from a specific offer.
   * 
   * @param offer the offer that led to the transactions
   * @return a list of transactions resulting from the specified offer
   */
  List<Transaction> findByOffer(Offer offer);
  
  /**
   * Finds all transactions with a specific status.
   * 
   * @param status the status to filter by
   * @return a list of transactions with the specified status
   */
  List<Transaction> findByStatus(TransactionStatus status);
  
  /**
   * Calculates the total sales amount for a specific seller from completed transactions.
   * 
   * @param seller the user who sold the items
   * @return the total sales amount for the specified seller
   */
  @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.seller = ?1 AND t.status = 'COMPLETED'")
  BigDecimal calculateTotalSalesBySeller(User seller);
  
  /**
   * Finds transactions created within a specific date range.
   * 
   * @param start the start date (inclusive)
   * @param end the end date (inclusive)
   * @return a list of transactions created within the specified date range
   */
  @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN ?1 AND ?2")
  List<Transaction> findTransactionsInDateRange(LocalDateTime start, LocalDateTime end);
}