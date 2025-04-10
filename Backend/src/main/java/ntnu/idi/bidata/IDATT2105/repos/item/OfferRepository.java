package ntnu.idi.bidata.IDATT2105.repos.item;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.enums.OfferStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.Offer;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Offer entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for offers made on items.
 */
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
  
  /**
   * Finds all offers for a specific item.
   * 
   * @param item the item to find offers for
   * @return a list of offers for the specified item
   */
  List<Offer> findByItem(Item item);
  
  /**
   * Finds offers for a specific item with a specific status.
   * 
   * @param item the item to find offers for
   * @param status the status to filter by
   * @return a list of offers for the specified item with the specified status
   */
  List<Offer> findByItemAndStatus(Item item, OfferStatus status);
  
  /**
   * Finds all offers made by a specific buyer.
   * 
   * @param buyer the user who made the offers
   * @return a list of offers made by the specified buyer
   */
  List<Offer> findByBuyer(User buyer);
  
  /**
   * Finds offers made by a specific buyer, ordered by creation date (newest first), with pagination.
   * 
   * @param buyer the user who made the offers
   * @param pageable the pagination information
   * @return a paginated list of offers made by the specified buyer, ordered by creation date
   */
  Page<Offer> findByBuyerOrderByCreatedAtDesc(User buyer, Pageable pageable);
  
  /**
   * Finds all offers for items listed by a specific seller.
   * 
   * @param seller the user who listed the items
   * @return a list of offers for items listed by the specified seller
   */
  @Query("SELECT o FROM Offer o JOIN o.item i WHERE i.seller = ?1")
  List<Offer> findBySeller(User seller);
  
  /**
   * Finds offers for items listed by a specific seller, ordered by creation date (newest first), with pagination.
   * 
   * @param seller the user who listed the items
   * @param pageable the pagination information
   * @return a paginated list of offers for items listed by the specified seller, ordered by creation date
   */
  @Query("SELECT o FROM Offer o JOIN o.item i WHERE i.seller = ?1 ORDER BY o.createdAt DESC")
  Page<Offer> findBySellerOrderByCreatedAtDesc(User seller, Pageable pageable);
  
  /**
   * Finds all offers with a specific status.
   * 
   * @param status the status to filter by
   * @return a list of offers with the specified status
   */
  List<Offer> findByStatus(OfferStatus status);
  
  /**
   * Finds all offers that have expired (expiration date is before the specified date).
   * 
   * @param date the reference date
   * @return a list of offers that have expired
   */
  List<Offer> findByExpiresAtBefore(LocalDateTime date);


  /**
   * Counts the number of offers made by a specific buyer with a specific status.
   * 
   * @param buyer the user who made the offers
   * @param status the status to filter by
   * @return the count of offers made by the specified buyer with the specified status
   */
  @Query("SELECT COUNT(o) FROM Offer o WHERE o.buyer = ?1 AND o.status = ?2")
  int countBySellerAndStatus(User seller, OfferStatus pending);

  /**
   * Finds offers for a specific item with a specific status, excluding a specific offer ID.
   * 
   * @param item the item to find offers for
   * @param status the status to filter by
   * @param offerId the ID of the offer to exclude
   * @return a list of offers for the specified item with the specified status, excluding the specified offer ID
   */
  @Query("SELECT o FROM Offer o WHERE o.item = ?1 AND o.status = ?2 AND o.id <> ?3")
  List<Offer> findByItemAndStatusAndIdNot(Item item, OfferStatus pending, Long offerId);
}