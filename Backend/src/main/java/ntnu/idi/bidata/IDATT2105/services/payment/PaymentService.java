package ntnu.idi.bidata.IDATT2105.services.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.dtos.CreatePaymentDTO;
import ntnu.idi.bidata.IDATT2105.dtos.PaymentResponseDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;
import ntnu.idi.bidata.IDATT2105.models.enums.OfferStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.PaymentMethod;
import ntnu.idi.bidata.IDATT2105.models.enums.TransactionStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.Offer;
import ntnu.idi.bidata.IDATT2105.models.transaction.Transaction;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.OfferRepository;
import ntnu.idi.bidata.IDATT2105.repos.transaction.TransactionRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.services.notification.NotificationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Service for handling payment operations.
 */
@Service
public class PaymentService {
    
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;
    private final NotificationService notificationService;
    
    @Autowired
    public PaymentService(TransactionRepository transactionRepository,
                         UserRepository userRepository,
                         ItemRepository itemRepository,
                         OfferRepository offerRepository,
                         NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.offerRepository = offerRepository;
        this.notificationService = notificationService;
    }
    
    /**
     * Creates a new payment transaction for an item
     * 
     * @param buyerId the buyer ID
     * @param paymentDTO the payment information
     * @return the payment response with payment information
     */
    @Transactional
    public PaymentResponseDTO createPayment(Long buyerId, CreatePaymentDTO paymentDTO) {
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new ApiException("Buyer not found", HttpStatus.NOT_FOUND));
        
        // Check payment type (direct purchase or offer)
        if (paymentDTO.getItemId() != null) {
            // Direct purchase
            return processDirectPurchase(buyer, paymentDTO);
        } else if (paymentDTO.getOfferId() != null) {
            // Offer payment
            return processOfferPayment(buyer, paymentDTO);
        } else {
            throw new ApiException("Either itemId or offerId must be provided", HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Processes payment for direct purchase (no offer)
     * 
     * @param buyer the buyer
     * @param paymentDTO the payment information
     * @return payment response
     */
    private PaymentResponseDTO processDirectPurchase(User buyer, CreatePaymentDTO paymentDTO) {
        Item item = itemRepository.findById(paymentDTO.getItemId())
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
        
        // Check if item is available for purchase
        if (item.getStatus() != ItemStatus.ACTIVE) {
            throw new ApiException("This item is not available for purchase", HttpStatus.BAD_REQUEST);
        }
        
        // Check if buyer is not the seller
        if (item.getSeller().getId().equals(buyer.getId())) {
            throw new ApiException("You cannot purchase your own item", HttpStatus.BAD_REQUEST);
        }
        
        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(item.getSeller());
        transaction.setItem(item);
        transaction.setAmount(item.getPrice());
        transaction.setPaymentMethod(paymentDTO.getPaymentMethod());
        transaction.setPaymentReference(generatePaymentReference());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update item status
        item.setStatus(ItemStatus.PENDING_PAYMENT);
        itemRepository.save(item);
        
        // Notify seller
        notificationService.createNotification(
            item.getSeller().getId(),
            NotificationType.PAYMENT_INITIATED,
            savedTransaction.getId(),
            "Payment initiated",
            "A buyer has initiated payment for your item: " + item.getTitle()
        );
        
        logger.info("Direct purchase payment initiated for item ID: " + item.getItemId() + 
                   " by user ID: " + buyer.getId());
        
        return createPaymentResponse(savedTransaction, null);
    }
    
    /**
     * Processes payment for an accepted offer
     * 
     * @param buyer the buyer
     * @param paymentDTO the payment information
     * @return payment response
     */
    private PaymentResponseDTO processOfferPayment(User buyer, CreatePaymentDTO paymentDTO) {
        Offer offer = offerRepository.findById(paymentDTO.getOfferId())
            .orElseThrow(() -> new ApiException("Offer not found", HttpStatus.NOT_FOUND));
        
        // Verify the buyer made the offer
        if (!offer.getBuyer().getId().equals(buyer.getId())) {
            throw new ApiException("You can only pay for your own offers", HttpStatus.FORBIDDEN);
        }
        
        // Check if offer is accepted
        if (offer.getStatus() != OfferStatus.ACCEPTED) {
            throw new ApiException("This offer is not accepted and cannot be paid", HttpStatus.BAD_REQUEST);
        }
        
        Item item = offer.getItem();
        
        // Check if item is still available
        if (item.getStatus() != ItemStatus.SOLD) {
            throw new ApiException("This item is not available for purchase", HttpStatus.BAD_REQUEST);
        }
        
        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(item.getSeller());
        transaction.setItem(item);
        transaction.setOffer(offer);
        transaction.setAmount(offer.getOfferAmount());
        transaction.setPaymentMethod(paymentDTO.getPaymentMethod());
        transaction.setPaymentReference(generatePaymentReference());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update offer and item status
        offer.setTransaction(savedTransaction);
        offerRepository.save(offer);
        
        item.setStatus(ItemStatus.PENDING_PAYMENT);
        itemRepository.save(item);
        
        // Notify seller
        notificationService.createNotification(
            item.getSeller().getId(),
            NotificationType.PAYMENT_INITIATED,
            savedTransaction.getId(),
            "Payment initiated",
            "A buyer has initiated payment for your item: " + item.getTitle()
        );
        
        logger.info("Offer payment initiated for offer ID: " + offer.getId() + 
                   " by user ID: " + buyer.getId());
        
        return createPaymentResponse(savedTransaction, offer);
    }
    
    /**
     * Creates payment response DTO
     * 
     * @param transaction the created transaction
     * @param offer the offer (if applicable)
     * @return payment response
     */
    private PaymentResponseDTO createPaymentResponse(Transaction transaction, Offer offer) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setTransactionId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setPaymentReference(transaction.getPaymentReference());
        response.setStatus(transaction.getStatus());
        
        if (transaction.getPaymentMethod() == PaymentMethod.VIPPS) {
            // Set Vipps-specific details
            response.setRedirectUrl("https://api.vipps.no/payment/" + transaction.getPaymentReference());
        }
        
        return response;
    }
    
    /**
     * Generates a unique payment reference
     * 
     * @return unique payment reference
     */
    private String generatePaymentReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Completes a payment (callback from payment processor)
     * 
     * @param transactionId the transaction ID
     * @param paymentReference the payment reference
     * @return the updated transaction
     */
    @Transactional
    public Transaction completePayment(Long transactionId, String paymentReference) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new ApiException("Transaction not found", HttpStatus.NOT_FOUND));
        
        // Verify payment reference
        if (!transaction.getPaymentReference().equals(paymentReference)) {
            throw new ApiException("Invalid payment reference", HttpStatus.BAD_REQUEST);
        }
        
        // Check if payment is already completed
        if (transaction.getStatus() == TransactionStatus.COMPLETED) {
            return transaction;
        }
        
        // Complete payment
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        
        Item item = transaction.getItem();
        item.setStatus(ItemStatus.SOLD);
        itemRepository.save(item);
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        // Notify seller
        notificationService.createNotification(
            transaction.getSeller().getId(),
            NotificationType.PAYMENT_COMPLETED,
            updatedTransaction.getId(),
            "Payment completed",
            "Payment for item " + item.getTitle() + " has been completed"
        );
        
        // Notify buyer
        notificationService.createNotification(
            transaction.getBuyer().getId(),
            NotificationType.PAYMENT_COMPLETED,
            updatedTransaction.getId(),
            "Payment completed",
            "Your payment for " + item.getTitle() + " has been completed"
        );
        
        logger.info("Payment completed for transaction ID: " + transactionId);
        return updatedTransaction;
    }
    
    /**
     * Cancels a payment
     * 
     * @param transactionId the transaction ID
     * @param userId the user ID (must be buyer or seller)
     * @param reason the cancellation reason
     * @return the updated transaction
     */
    @Transactional
    public Transaction cancelPayment(Long transactionId, Long userId, String reason) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new ApiException("Transaction not found", HttpStatus.NOT_FOUND));
        
        // Verify user is buyer or seller
        if (!transaction.getBuyer().getId().equals(userId) && 
            !transaction.getSeller().getId().equals(userId)) {
            throw new ApiException("You can only cancel your own transactions", HttpStatus.FORBIDDEN);
        }
        
        // Check if payment can be cancelled
        if (transaction.getStatus() == TransactionStatus.COMPLETED) {
            throw new ApiException("Completed payments cannot be cancelled", HttpStatus.BAD_REQUEST);
        }
        
        // Cancel payment
        transaction.setStatus(TransactionStatus.CANCELLED);
        transaction.setCancelledAt(LocalDateTime.now());
        
        // Update item status
        Item item = transaction.getItem();
        
        if (transaction.getOffer() != null) {
            // Was an offer-based transaction
            item.setStatus(ItemStatus.ACTIVE);
            
            // Reactivate the offer
            Offer offer = transaction.getOffer();
            offer.setStatus(OfferStatus.PENDING);
            offerRepository.save(offer);
        } else {
            // Was a direct purchase
            item.setStatus(ItemStatus.ACTIVE);
        }
        
        itemRepository.save(item);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        // Notify the other party
        Long notifyUserId = transaction.getBuyer().getId().equals(userId) 
            ? transaction.getSeller().getId() 
            : transaction.getBuyer().getId();
            
        String cancellerType = transaction.getBuyer().getId().equals(userId) ? "Buyer" : "Seller";
        
        notificationService.createNotification(
            notifyUserId,
            NotificationType.PAYMENT_CANCELLED,
            updatedTransaction.getId(),
            "Payment cancelled",
            cancellerType + " has cancelled payment for item " + item.getTitle() + 
            (reason != null ? ": " + reason : "")
        );
        
        logger.info("Payment cancelled for transaction ID: " + transactionId + 
                   " by user ID: " + userId);
        return updatedTransaction;
    }
    
    /**
     * Gets a transaction by ID
     * 
     * @param transactionId the transaction ID
     * @param userId the user ID (must be buyer or seller)
     * @return the transaction
     */
    public Transaction getTransaction(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new ApiException("Transaction not found", HttpStatus.NOT_FOUND));
        
        // Verify user is buyer or seller
        if (!transaction.getBuyer().getId().equals(userId) && 
            !transaction.getSeller().getId().equals(userId)) {
            throw new ApiException("You can only view your own transactions", HttpStatus.FORBIDDEN);
        }
        
        return transaction;
    }
    
    /**
     * Gets transactions for a buyer
     * 
     * @param buyerId the buyer ID
     * @param status optional status filter
     * @param pageable pagination parameters
     * @return page of transactions
     */
    public Page<Transaction> getBuyerTransactions(Long buyerId, TransactionStatus status, Pageable pageable) {
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        if (status != null) {
            return transactionRepository.findByBuyerAndStatus(buyer, status, pageable);
        }
        return transactionRepository.findByBuyer(buyer, pageable);
    }
    
    /**
     * Gets transactions for a seller
     * 
     * @param sellerId the seller ID
     * @param status optional status filter
     * @param pageable pagination parameters
     * @return page of transactions
     */
    public Page<Transaction> getSellerTransactions(Long sellerId, TransactionStatus status, Pageable pageable) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        if (status != null) {
            return transactionRepository.findBySellerAndStatus(seller, status, pageable);
        }
        return transactionRepository.findBySeller(seller, pageable);
    }
    
    /**
     * Gets sales statistics for a seller
     * 
     * @param sellerId the seller ID
     * @return map with sales statistics
     */
    public Map<String, Object> getSellerStats(Long sellerId) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
        Map<String, Object> stats = new HashMap<>();
        
        // Total number of completed sales
        long completedSales = transactionRepository.countBySellerAndStatus(seller, TransactionStatus.COMPLETED);
        stats.put("completedSales", completedSales);
        
        // Total revenue
        BigDecimal totalRevenue = transactionRepository.calculateTotalSalesBySeller(seller);
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        
        // Recent sales
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        List<Transaction> recentSales = transactionRepository.findTransactionsInDateRange(lastMonth, LocalDateTime.now());
        stats.put("recentSales", recentSales.size());
        
        return stats;
    }
}