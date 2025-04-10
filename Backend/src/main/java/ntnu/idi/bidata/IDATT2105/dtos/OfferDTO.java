package ntnu.idi.bidata.IDATT2105.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ntnu.idi.bidata.IDATT2105.models.enums.OfferStatus;

/**
 * Data Transfer Object for offers.
 */
public class OfferDTO {
    
    private Long id;
    private Long itemId;
    private String itemTitle;
    private String itemImageUrl;
    private Long buyerId;
    private String buyerUsername;
    private Long sellerId;
    private String sellerUsername;
    private BigDecimal offerAmount;
    private BigDecimal itemPrice;
    private OfferStatus status;
    private String message;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default constructor
    public OfferDTO() {
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getItemId() {
        return itemId;
    }
    
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    
    public String getItemTitle() {
        return itemTitle;
    }
    
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    
    public String getItemImageUrl() {
        return itemImageUrl;
    }
    
    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }
    
    public Long getBuyerId() {
        return buyerId;
    }
    
    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }
    
    public String getBuyerUsername() {
        return buyerUsername;
    }
    
    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }
    
    public Long getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    
    public String getSellerUsername() {
        return sellerUsername;
    }
    
    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
    
    public BigDecimal getOfferAmount() {
        return offerAmount;
    }
    
    public void setOfferAmount(BigDecimal offerAmount) {
        this.offerAmount = offerAmount;
    }
    
    public BigDecimal getItemPrice() {
        return itemPrice;
    }
    
    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }
    
    public OfferStatus getStatus() {
        return status;
    }
    
    public void setStatus(OfferStatus status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
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