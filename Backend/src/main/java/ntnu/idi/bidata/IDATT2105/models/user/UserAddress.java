package ntnu.idi.bidata.IDATT2105.models.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA entity representing a user's address in the system.
 */
@Entity
@Table(name = "user_addresses", indexes = {
  @Index(name = "idx_user_id", columnList = "user_id"),
  @Index(name = "idx_location", columnList = "latitude, longitude")
})
public class UserAddress {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Long addressId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "address_line1", nullable = false)
  private String addressLine1;

  @Column(name = "address_line2")
  private String addressLine2;

  @Column(nullable = false)
  private String city;

  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private String country;

  @Column(name = "is_primary", nullable = false)
  private Boolean isPrimary = false;

  @Column(precision = 10, scale = 8)
  private BigDecimal latitude;

  @Column(precision = 11, scale = 8)
  private BigDecimal longitude;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // Default constructor
  public UserAddress() {
  }

  // Constructdr with required fields
  public UserAddress(User user, String addressLine1, String city, String postalCode, String country) {
    this.user = user;
    this.addressLine1 = addressLine1;
    this.city = city;
    this.postalCode = postalCode;
    this.country = country;
  }

  public Long getAddressId() {
    return addressId;
  }

  public void setAddressId(Long addressId) {
    this.addressId = addressId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Boolean getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(Boolean isPrimary) {
    this.isPrimary = isPrimary;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    UserAddress address = (UserAddress) o;
    return addressId.equals(address.addressId);
  }

  @Override
  public int hashCode() {
    return addressId.hashCode();
  }

  @Override
  public String toString() {
    return "UserAddress{" +
           "addressId=" + addressId +
           ", user=" + user.getUsername() +
           ", addressLine1='" + addressLine1 + '\'' +
           ", city='" + city + '\'' +
           ", postalCode='" + postalCode + '\'' +
           ", country='" + country + '\'' +
           ", isPrimary=" + isPrimary +
           '}';
  }
}