package ntnu.idi.bidata.IDATT2105.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import ntnu.idi.bidata.IDATT2105.models.enums.ItemCondition;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;

/**
 * Data Transfer Object for updating existing items.
 * All fields are optional since updates may be partial.
 */
public class UpdateItemDTO {
    
    private Long categoryId;
    
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @Size(max = 255, message = "Brief description cannot exceed 255 characters")
    private String briefDescription;
    
    private String fullDescription;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    private String currency;
    
    private ItemCondition condition;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private Boolean allowOffers;
    private Boolean acceptVipps;
    private String locationAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private ItemStatus status;
    private LocalDateTime expiresAt;
    private List<String> tags;
    private List<String> imageUrls;
    
    // Default constructor
    public UpdateItemDTO() {
    }
    
    // Getters and setters
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBriefDescription() {
        return briefDescription;
    }
    
    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }
    
    public String getFullDescription() {
        return fullDescription;
    }
    
    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public ItemCondition getCondition() {
        return condition;
    }
    
    public void setCondition(ItemCondition condition) {
        this.condition = condition;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Boolean getAllowOffers() {
        return allowOffers;
    }
    
    public void setAllowOffers(Boolean allowOffers) {
        this.allowOffers = allowOffers;
    }
    
    public Boolean getAcceptVipps() {
        return acceptVipps;
    }
    
    public void setAcceptVipps(Boolean acceptVipps) {
        this.acceptVipps = acceptVipps;
    }
    
    public String getLocationAddress() {
        return locationAddress;
    }
    
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public ItemStatus getStatus() {
        return status;
    }
    
    public void setStatus(ItemStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    @Override
    public String toString() {
        return "UpdateItemDTO{" +
               "categoryId=" + categoryId +
               ", title='" + title + '\'' +
               ", briefDescription='" + briefDescription + '\'' +
               ", price=" + price +
               ", status=" + status +
               '}';
    }
}
