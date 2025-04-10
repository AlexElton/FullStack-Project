package ntnu.idi.bidata.IDATT2105.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating a new offer on an item.
 */
public class CreateOfferDTO {
    
    @NotNull(message = "Item ID is required")
    private Long itemId;
    
    @NotNull(message = "Offer amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Offer amount must be greater than 0")
    private BigDecimal offerAmount;
    
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;
    
    // Default constructor
    public CreateOfferDTO() {
    }
    
    // Constructor with fields
    public CreateOfferDTO(Long itemId, BigDecimal offerAmount, String message) {
        this.itemId = itemId;
        this.offerAmount = offerAmount;
        this.message = message;
    }
    
    // Getters and setters
    public Long getItemId() {
        return itemId;
    }
    
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    
    public BigDecimal getOfferAmount() {
        return offerAmount;
    }
    
    public void setOfferAmount(BigDecimal offerAmount) {
        this.offerAmount = offerAmount;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}