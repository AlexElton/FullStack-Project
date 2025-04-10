package ntnu.idi.bidata.IDATT2105.dtos;

import java.math.BigDecimal;
import java.util.List;

import ntnu.idi.bidata.IDATT2105.models.enums.ItemCondition;

/**
 * Data Transfer Object for search criteria.
 */
public class SearchCriteriaDTO {
    
    private String keyword;
    private Long categoryId;
    private boolean includeSubcategories = true;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<ItemCondition> conditions;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double distance; // In kilometers
    private List<String> tags;
    private Boolean allowsOffers;
    private Boolean acceptsVipps;
    
    // Default constructor
    public SearchCriteriaDTO() {
    }

    // Getters and setters
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isIncludeSubcategories() {
        return includeSubcategories;
    }

    public void setIncludeSubcategories(boolean includeSubcategories) {
        this.includeSubcategories = includeSubcategories;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<ItemCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<ItemCondition> conditions) {
        this.conditions = conditions;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getAllowsOffers() {
        return allowsOffers;
    }

    public void setAllowsOffers(Boolean allowsOffers) {
        this.allowsOffers = allowsOffers;
    }

    public Boolean getAcceptsVipps() {
        return acceptsVipps;
    }

    public void setAcceptsVipps(Boolean acceptsVipps) {
        this.acceptsVipps = acceptsVipps;
    }
}