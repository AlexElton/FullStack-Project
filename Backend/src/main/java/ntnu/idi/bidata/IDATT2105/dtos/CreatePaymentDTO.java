package ntnu.idi.bidata.IDATT2105.dtos;

import ntnu.idi.bidata.IDATT2105.models.enums.PaymentMethod;

/**
 * Data Transfer Object for creating a new payment transaction.
 */
public class CreatePaymentDTO {

    private Long itemId; // For direct purchase
    private Long offerId; // For offer-based purchase
    private PaymentMethod paymentMethod;
    private String returnUrl; // URL to redirect after payment completion
    
    // Default constructor
    public CreatePaymentDTO() {
    }

    // Getters and setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}