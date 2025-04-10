package ntnu.idi.bidata.IDATT2105.dtos;

import java.math.BigDecimal;

import ntnu.idi.bidata.IDATT2105.models.enums.TransactionStatus;

/**
 * Data Transfer Object for payment response.
 */
public class PaymentResponseDTO {

    private Long transactionId;
    private BigDecimal amount;
    private String paymentReference;
    private TransactionStatus status;
    private String redirectUrl; // For redirect-based payment methods (e.g. Vipps)
    private String receiptUrl; // URL to payment receipt
    
    // Default constructor
    public PaymentResponseDTO() {
    }

    // Getters and setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }
}