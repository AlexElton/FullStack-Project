package ntnu.idi.bidata.IDATT2105.models.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.enums.Theme;

/**
 * JPA entity representing a user's preferences and settings.
 * This entity uses a shared primary key with User (user_id).
 */
@Entity
@Table(name = "user_settings")
public class UserSettings {
  
  @Id
  @Column(name = "user_id")
  private Long userId;

  @MapsId
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, length = 10)
  private String language = "no";

  @Column(name = "email_notifications", nullable = false)
  private Boolean emailNotifications = true;

  @Column(name = "push_notifications", nullable = false)
  private Boolean pushNotifications = true;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Theme theme = Theme.SYSTEM;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // Default constructor
  public UserSettings() {
  }

  // Constructor with user
  public UserSettings(User user) {
    this.user = user;
  }

  // Constructor with all fields
  public UserSettings(User user, String language, Boolean emailNotifications, 
                     Boolean pushNotifications, Theme theme) {
    this.user = user;
    this.language = language;
    this.emailNotifications = emailNotifications;
    this.pushNotifications = pushNotifications;
    this.theme = theme;
  }

  public Long getUserId() {
    return userId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Boolean getEmailNotifications() {
    return emailNotifications;
  }

  public void setEmailNotifications(Boolean emailNotifications) {
    this.emailNotifications = emailNotifications;
  }

  public Boolean getPushNotifications() {
    return pushNotifications;
  }

  public void setPushNotifications(Boolean pushNotifications) {
    this.pushNotifications = pushNotifications;
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    UserSettings that = (UserSettings) o;
    return userId.equals(that.userId);
  }

  @Override
  public int hashCode() {
    return userId.hashCode();
  }

  @Override
  public String toString() {
    return "UserSettings{" +
           "userId=" + userId +
           ", language='" + language + '\'' +
           ", emailNotifications=" + emailNotifications +
           ", pushNotifications=" + pushNotifications +
           ", theme=" + theme +
           '}';
  }
}
