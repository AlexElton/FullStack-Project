package ntnu.idi.bidata.IDATT2105.services.offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.dtos.CreateOfferDTO;
import ntnu.idi.bidata.IDATT2105.dtos.OfferDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;
import ntnu.idi.bidata.IDATT2105.models.enums.OfferStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.Offer;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.OfferRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.services.notification.NotificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for managing offers on items.
 */
@Service
public class OfferService {
    
    private static final Logger logger = Logger.getLogger(OfferService.class.getName());
    
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final NotificationService notificationService;
    
    @Autowired
    public OfferService(OfferRepository offerRepository, 
                       UserRepository userRepository,
                       ItemRepository itemRepository,
                       NotificationService notificationService) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.notificationService = notificationService;
    }
    
    /**
     * Creates a new offer on an item
     * 
     * @param buyerId the buyer making the offer
     * @param offerDTO the offer details
     * @return the created offer
     * @throws ApiException if validation fails or user/item not found
     */
    @Transactional
    public Offer createOffer(Long buyerId, CreateOfferDTO offerDTO) {
        if (offerDTO == null) {
            throw new ApiException("Offer data cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        // Get buyer
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new ApiException("Buyer not found", HttpStatus.NOT_FOUND));
            
        // Get item
        Item item = itemRepository.findById(offerDTO.getItemId())
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
            
        // Check if item allows offers
        if (!item.getAllowOffers()) {
            throw new ApiException("This item does not accept offers", HttpStatus.BAD_REQUEST);
        }
        
        // Check if item is still active
        if (item.getStatus() != ItemStatus.ACTIVE) {
            throw new ApiException("Cannot make offers on inactive items", HttpStatus.BAD_REQUEST);
        }
        
        // Check if buyer is not the seller
        if (item.getSeller().getId().equals(buyerId)) {
            throw new ApiException("You cannot make offers on your own items", HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Create and save offer
            Offer offer = new Offer();
            offer.setItem(item);
            offer.setBuyer(buyer);
            offer.setOfferAmount(offerDTO.getOfferAmount());
            offer.setMessage(offerDTO.getMessage());
            offer.setStatus(OfferStatus.PENDING);
            offer.setCreatedAt(LocalDateTime.now());
            offer.setExpiresAt(LocalDateTime.now().plusDays(7)); // Expires in 7 days
            
            Offer savedOffer = offerRepository.save(offer);
            
            // Notify the seller
            notificationService.createNotification(
                item.getSeller().getId(),
                NotificationType.OFFER,
                savedOffer.getId(),
                "New offer on your item",
                "You received a new offer of " + offerDTO.getOfferAmount() + 
                " for your item: " + item.getTitle()
            );
            
            logger.info("Created new offer for item ID: " + item.getItemId() + " from user ID: " + buyerId);
            return savedOffer;
        } catch (Exception e) {
            logger.severe("Error creating offer: " + e.getMessage());
            throw new ApiException("Failed to create offer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Accepts an offer on an item
     * 
     * @param sellerId the seller accepting the offer
     * @param offerId the ID of the offer to accept
     * @return the updated offer
     */
    @Transactional
    public Offer acceptOffer(Long sellerId, Long offerId) {
        Offer offer = getOfferById(offerId);
        
        // Verify the seller owns the item
        if (!offer.getItem().getSeller().getId().equals(sellerId)) {
            throw new ApiException("You can only accept offers on your own items", HttpStatus.FORBIDDEN);
        }
        
        // Check if offer is still pending
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new ApiException("This offer cannot be accepted", HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Update offer status
            offer.setStatus(OfferStatus.ACCEPTED);
            offer.setUpdatedAt(LocalDateTime.now());
            
            // Mark item as sold
            Item item = offer.getItem();
            item.setStatus(ItemStatus.SOLD);
            itemRepository.save(item);
            
            // Reject all other offers on this item
            List<Offer> otherOffers = offerRepository.findByItemAndStatusAndIdNot(
                item, OfferStatus.PENDING, offerId);
            
            for (Offer otherOffer : otherOffers) {
                otherOffer.setStatus(OfferStatus.REJECTED);
                otherOffer.setUpdatedAt(LocalDateTime.now());
                offerRepository.save(otherOffer);
                
                // Notify other buyers their offers were rejected
                notificationService.createNotification(
                    otherOffer.getBuyer().getId(),
                    NotificationType.MESSAGE,
                    otherOffer.getId(),
                    "Offer rejected",
                    "Your offer on " + item.getTitle() + " was rejected because another offer was accepted."
                );
            }
            
            // Save and notify buyer
            Offer savedOffer = offerRepository.save(offer);
            
            notificationService.createNotification(
                offer.getBuyer().getId(),
                NotificationType.MESSAGE,
                savedOffer.getId(),
                "Offer accepted",
                "Your offer on " + item.getTitle() + " was accepted. Please proceed to payment."
            );
            
            logger.info("Seller ID: " + sellerId + " accepted offer ID: " + offerId);
            return savedOffer;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error accepting offer: " + e.getMessage());
            throw new ApiException("Failed to accept offer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Rejects an offer on an item
     * 
     * @param sellerId the seller rejecting the offer
     * @param offerId the ID of the offer to reject
     * @return the updated offer
     */
    @Transactional
    public Offer rejectOffer(Long sellerId, Long offerId) {
        Offer offer = getOfferById(offerId);
        
        // Verify the seller owns the item
        if (!offer.getItem().getSeller().getId().equals(sellerId)) {
            throw new ApiException("You can only reject offers on your own items", HttpStatus.FORBIDDEN);
        }
        
        // Check if offer is still pending
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new ApiException("This offer cannot be rejected", HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Update offer status
            offer.setStatus(OfferStatus.REJECTED);
            offer.setUpdatedAt(LocalDateTime.now());
            
            Offer savedOffer = offerRepository.save(offer);
            
            // Notify buyer
            notificationService.createNotification(
                offer.getBuyer().getId(),
                NotificationType.MESSAGE,
                savedOffer.getId(),
                "Offer rejected",
                "Your offer on " + offer.getItem().getTitle() + " was rejected."
            );
            
            logger.info("Seller ID: " + sellerId + " rejected offer ID: " + offerId);
            return savedOffer;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error rejecting offer: " + e.getMessage());
            throw new ApiException("Failed to reject offer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Cancels an offer made by a buyer
     * 
     * @param buyerId the buyer cancelling their offer
     * @param offerId the ID of the offer to cancel
     * @return the updated offer
     */
    @Transactional
    public Offer cancelOffer(Long buyerId, Long offerId) {
        Offer offer = getOfferById(offerId);
        
        // Verify the buyer made the offer
        if (!offer.getBuyer().getId().equals(buyerId)) {
            throw new ApiException("You can only cancel your own offers", HttpStatus.FORBIDDEN);
        }
        
        // Check if offer is still pending
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new ApiException("This offer cannot be cancelled", HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Update offer status
            offer.setStatus(OfferStatus.RETRACTED);
            offer.setUpdatedAt(LocalDateTime.now());
            
            Offer savedOffer = offerRepository.save(offer);
            
            // Notify seller
            notificationService.createNotification(
                offer.getItem().getSeller().getId(),
                NotificationType.MESSAGE,
                savedOffer.getId(),
                "Offer cancelled",
                "A buyer cancelled their offer on " + offer.getItem().getTitle() + "."
            );
            
            logger.info("Buyer ID: " + buyerId + " cancelled offer ID: " + offerId);
            return savedOffer;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error cancelling offer: " + e.getMessage());
            throw new ApiException("Failed to cancel offer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Gets an offer by ID
     * 
     * @param offerId the offer ID
     * @return the offer
     * @throws ApiException if offer is not found
     */
    public Offer getOfferById(Long offerId) {
        if (offerId == null) {
            throw new ApiException("Offer ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        return offerRepository.findById(offerId)
            .orElseThrow(() -> new ApiException("Offer not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Gets offers made by a buyer
     * 
     * @param buyerId the buyer ID
     * @param pageable pagination parameters
     * @return page of offers made by the buyer
     */
    public Page<Offer> getOffersByBuyer(Long buyerId, Pageable pageable) {
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        return offerRepository.findByBuyerOrderByCreatedAtDesc(buyer, pageable);
    }
    
    /**
     * Gets offers received by a seller
     * 
     * @param sellerId the seller ID
     * @param pageable pagination parameters
     * @return page of offers on the seller's items
     */
    public Page<Offer> getOffersBySeller(Long sellerId, Pageable pageable) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        return offerRepository.findBySellerOrderByCreatedAtDesc(seller, pageable);
    }
    
    /**
     * Counts pending offers for a seller
     * 
     * @param sellerId the seller ID
     * @return count of pending offers
     */
    public int countPendingOffersBySeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        return offerRepository.countBySellerAndStatus(seller, OfferStatus.PENDING);
    }
}