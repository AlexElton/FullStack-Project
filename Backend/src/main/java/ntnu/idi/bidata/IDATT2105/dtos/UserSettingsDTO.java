package ntnu.idi.bidata.IDATT2105.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import ntnu.idi.bidata.IDATT2105.models.enums.Theme;

import java.util.Objects;

/**
 * Data Transfer Object for user settings.
 * Used for updating and retrieving user preference settings.
 */
public class UserSettingsDTO {
    
    @Size(min = 2, max = 10, message = "Language code must be between 2 and 10 characters")
    @Pattern(regexp = "^[a-zA-Z]{2,3}(-[a-zA-Z]{2,3})?$", message = "Language code must be in valid format like 'en', 'no', or 'en-US'")
    private String language;
    
    @NotNull(message = "Email notifications setting is required")
    private Boolean emailNotifications;
    
    @NotNull(message = "Push notifications setting is required")
    private Boolean pushNotifications;
    
    @NotNull(message = "Theme setting is required")
    private Theme theme;
    
    /**
     * Default constructor for Jackson serialization/deserialization.
     */
    public UserSettingsDTO() {
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param language language preference code
     * @param emailNotifications whether to enable email notifications
     * @param pushNotifications whether to enable push notifications
     * @param theme UI theme preference
     */
    public UserSettingsDTO(String language, Boolean emailNotifications, Boolean pushNotifications, Theme theme) {
        this.language = language;
        this.emailNotifications = emailNotifications;
        this.pushNotifications = pushNotifications;
        this.theme = theme;
    }
    
    /**
     * Creates a DTO from a UserSettings entity.
     * 
     * @param settings the user settings entity
     * @return a new DTO populated with entity data
     */
    public static UserSettingsDTO fromEntity(ntnu.idi.bidata.IDATT2105.models.user.UserSettings settings) {
        return new UserSettingsDTO(
            settings.getLanguage(), 
            settings.getEmailNotifications(),
            settings.getPushNotifications(),
            settings.getTheme()
        );
    }
    
    /**
     * Gets the language preference code.
     * 
     * @return the language code
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * Sets the language preference code.
     * 
     * @param language the language code to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    
    /**
     * Gets whether email notifications are enabled.
     * 
     * @return true if email notifications are enabled, false otherwise
     */
    public Boolean getEmailNotifications() {
        return emailNotifications;
    }
    
    /**
     * Sets whether email notifications are enabled.
     * 
     * @param emailNotifications true to enable email notifications, false to disable
     */
    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
    
    /**
     * Gets whether push notifications are enabled.
     * 
     * @return true if push notifications are enabled, false otherwise
     */
    public Boolean getPushNotifications() {
        return pushNotifications;
    }
    
    /**
     * Sets whether push notifications are enabled.
     * 
     * @param pushNotifications true to enable push notifications, false to disable
     */
    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }
    
    /**
     * Gets the UI theme preference.
     * 
     * @return the theme preference
     */
    public Theme getTheme() {
        return theme;
    }
    
    /**
     * Sets the UI theme preference.
     * 
     * @param theme the theme preference to set
     */
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettingsDTO that = (UserSettingsDTO) o;
        return Objects.equals(language, that.language) &&
               Objects.equals(emailNotifications, that.emailNotifications) &&
               Objects.equals(pushNotifications, that.pushNotifications) &&
               theme == that.theme;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(language, emailNotifications, pushNotifications, theme);
    }
    
    @Override
    public String toString() {
        return "UserSettingsDTO{" +
               "language='" + language + '\'' +
               ", emailNotifications=" + emailNotifications +
               ", pushNotifications=" + pushNotifications +
               ", theme=" + theme +
               '}';
    }
}