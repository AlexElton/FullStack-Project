package ntnu.idi.bidata.IDATT2105.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.dtos.UserSettingsDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.Theme;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.models.user.UserSettings;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserSettingsRepository;

import java.util.logging.Logger;

/**
 * Service for managing user settings operations.
 */
@Service
public class UserSettingsService {
    
    private static final Logger logger = Logger.getLogger(UserSettingsService.class.getName());
    
    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public UserSettingsService(UserSettingsRepository userSettingsRepository, UserRepository userRepository) {
        this.userSettingsRepository = userSettingsRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Gets settings for a user
     * 
     * @param userId the user ID
     * @return the user's settings
     * @throws ApiException if settings are not found
     */
    public UserSettings getUserSettings(Long userId) {
        if (userId == null) {
            throw new ApiException("User ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        logger.info("Fetching settings for user ID: " + userId);
        return userSettingsRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User settings not found", HttpStatus.NOT_FOUND));
    }
    
    /**
     * Gets settings for a user as DTO
     * 
     * @param userId the user ID
     * @return DTO containing the user's settings
     * @throws ApiException if settings are not found
     */
    public UserSettingsDTO getUserSettingsDTO(Long userId) {
        UserSettings settings = getUserSettings(userId);
        return UserSettingsDTO.fromEntity(settings);
    }
    
    /**
     * Updates settings for a user
     * 
     * @param userId the user ID
     * @param settingsDTO the updated settings
     * @return the updated user settings
     * @throws ApiException if settings are not found or validation fails
     */
    @Transactional
    public UserSettings updateUserSettings(Long userId, UserSettingsDTO settingsDTO) {
        if (userId == null) {
            throw new ApiException("User ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (settingsDTO == null) {
            throw new ApiException("Settings cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        UserSettings settings = getUserSettings(userId);
        
        try {
            // Update settings fields with validation
            if (settingsDTO.getLanguage() != null) {
                settings.setLanguage(settingsDTO.getLanguage());
            }
            
            if (settingsDTO.getEmailNotifications() != null) {
                settings.setEmailNotifications(settingsDTO.getEmailNotifications());
            }
            
            if (settingsDTO.getPushNotifications() != null) {
                settings.setPushNotifications(settingsDTO.getPushNotifications());
            }
            
            if (settingsDTO.getTheme() != null) {
                settings.setTheme(settingsDTO.getTheme());
            }
            
            UserSettings updatedSettings = userSettingsRepository.save(settings);
            logger.info("Updated settings for user ID: " + userId);
            return updatedSettings;
        } catch (Exception e) {
            logger.severe("Error updating settings for user ID " + userId + ": " + e.getMessage());
            throw new ApiException("Failed to update user settings: " + e.getMessage(), 
                                  HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Creates default settings for a new user
     * 
     * @param userId the user ID
     * @return the created default settings
     * @throws ApiException if user is not found or settings creation fails
     */
    @Transactional
    public UserSettings createDefaultSettings(Long userId) {
        if (userId == null) {
            throw new ApiException("User ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
            // Check if settings already exist
            if (userSettingsRepository.existsById(userId)) {
                throw new ApiException("Settings already exist for user", HttpStatus.CONFLICT);
            }
            
            UserSettings settings = new UserSettings(user);
            // Set default values
            settings.setLanguage("en");
            settings.setEmailNotifications(true);
            settings.setPushNotifications(true);
            settings.setTheme(Theme.LIGHT);
            
            UserSettings savedSettings = userSettingsRepository.save(settings);
            logger.info("Created default settings for user ID: " + userId);
            return savedSettings;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error creating settings for user ID " + userId + ": " + e.getMessage());
            throw new ApiException("Failed to create user settings: " + e.getMessage(), 
                                  HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}