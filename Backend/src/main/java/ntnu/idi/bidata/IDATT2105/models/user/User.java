package ntnu.idi.bidata.IDATT2105.models.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.enums.AccountStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.Role;

/**
 * JPA entity representing a user in the system.
 * <p>
 * This class is used to map the users table in the database to a Java object.
 * </p>
 */
@Entity
@Table(name = "users", indexes = {
  @Index(name = "idx_username", columnList = "username"),
  @Index(name = "idx_email", columnList = "email"),
  @Index(name = "idx_role", columnList = "role"),
  @Index(name = "idx_status", columnList = "status")
})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, updatable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false, name = "password_hash")
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "profile_pic_url")
  private String profilePicUrl;

  @Column(columnDefinition = "TEXT")
  private String bio;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role = Role.USER;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "account_status", nullable = false)
  private AccountStatus status = AccountStatus.ACTIVE;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

    public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getProfilePicUrl() {
    return profilePicUrl;
  }

  public void setProfilePicUrl(String profilePicUrl) {
    this.profilePicUrl = profilePicUrl;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public AccountStatus getStatus() {
    return status;
  }

  public void setStatus(AccountStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public LocalDateTime getLastLoginAt() {
    return lastLoginAt;
  }

  public void setLastLoginAt(LocalDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    User user = (User) o;
    return id.equals(user.id) && 
           username.equals(user.username) && 
           email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id, username, email);
  }

  @Override
  public String toString() {
    return "User{" +
           "id=" + id +
           ", username='" + username + '\'' +
           ", email='" + email + '\'' +
           ", firstName='" + firstName + '\'' +
           ", lastName='" + lastName + '\'' +
           ", role=" + role +
           ", status=" + status +
           '}';
  }
}

