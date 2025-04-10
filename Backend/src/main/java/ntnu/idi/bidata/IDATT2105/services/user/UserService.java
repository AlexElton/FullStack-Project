package ntnu.idi.bidata.IDATT2105.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ntnu.idi.bidata.IDATT2105.dtos.UpdatePasswordDTO;
import ntnu.idi.bidata.IDATT2105.dtos.UserProfileDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.AccountStatus;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for user management operations.
 */
@Service
@Slf4j
public class UserService {
    
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Constructs a new UserService with the necessary dependencies.
     * 
     * @param userRepository repository for user data
     * @param passwordEncoder encoder for password hashing
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Gets all users.
     * 
     * @return list of all users
     */
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }
    
    /**
     * Gets users with pagination.
     * 
     * @param pageable pagination parameters
     * @return page of users
     */
    public Page<User> getUsers(Pageable pageable) {
        logger.info("Fetching users with pagination");
        return userRepository.findAll(pageable);
    }
    
    /**
     * Gets a user by ID.
     * 
     * @param id the user ID
     * @return the user
     * @throws ApiException if user is not found
     */
    public User getUserById(Long id) {
        if (id == null) {
            throw new ApiException("User ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        logger.info("Fetching user by ID: " + id);
        return userRepository.findById(id)
            .orElseThrow(() -> {
                logger.warning("User not found with ID: " + id);
                return new ApiException("User not found", HttpStatus.NOT_FOUND);
            });
    }
    
    /**
     * Gets a user by username.
     * 
     * @param username the username
     * @return the user
     * @throws ApiException if user is not found
     */
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ApiException("Username cannot be null or empty", HttpStatus.BAD_REQUEST);
        }
        
        logger.info("Fetching user by username: " + username);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> {
                logger.warning("User not found with username: " + username);
                return new ApiException("User not found", HttpStatus.NOT_FOUND);
            });
    }
    
    /**
     * Updates a user's profile.
     * 
     * @param id the user ID
     * @param profileDTO the updated profile data
     * @return the updated user
     * @throws ApiException if validation fails or user is not found
     */
    @Transactional
    public User updateProfile(Long id, UserProfileDTO profileDTO) {
        if (profileDTO == null) {
            throw new ApiException("Profile data cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        User user = getUserById(id);
        
        try {
            // Check email uniqueness if it's being changed
            if (!user.getEmail().equals(profileDTO.getEmail()) && 
                userRepository.existsByEmail(profileDTO.getEmail())) {
                throw new ApiException("Email already in use", HttpStatus.CONFLICT);
            }
            
            // Update user fields
            user.setEmail(profileDTO.getEmail());
            user.setFirstName(profileDTO.getFirstName());
            user.setLastName(profileDTO.getLastName());
            user.setPhoneNumber(profileDTO.getPhoneNumber());
            user.setBio(profileDTO.getBio());
            
            if (profileDTO.getProfilePictureUrl() != null && !profileDTO.getProfilePictureUrl().trim().isEmpty()) {
                user.setProfilePicUrl(profileDTO.getProfilePictureUrl());
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            
            User updatedUser = userRepository.save(user);
            logger.info("User profile updated successfully for ID: " + id);
            return updatedUser;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error updating user profile: " + e.getMessage());
            throw new ApiException("Error updating user profile: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Updates a user's password.
     * 
     * @param id the user ID
     * @param updatePasswordDTO current and new password
     * @return true if password was updated
     * @throws ApiException if validation fails or current password is incorrect
     */
    @Transactional
    public boolean updatePassword(Long id, UpdatePasswordDTO updatePasswordDTO) {
        if (updatePasswordDTO == null) {
            throw new ApiException("Password data cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        User user = getUserById(id);
        
        try {
            // Verify current password
            if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPasswordHash())) {
                logger.warning("Failed password update attempt - incorrect current password for user ID: " + id);
                throw new ApiException("Current password is incorrect", HttpStatus.BAD_REQUEST);
            }
            
            // Validate new password is different from current
            if (passwordEncoder.matches(updatePasswordDTO.getNewPassword(), user.getPasswordHash())) {
                throw new ApiException("New password must be different from current password", HttpStatus.BAD_REQUEST);
            }
            
            // Set new password
            user.setPasswordHash(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            
            logger.info("Password updated successfully for user ID: " + id);
            return true;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error updating user password: " + e.getMessage());
            throw new ApiException("Error updating password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Deactivates a user account.
     * 
     * @param id the user ID
     * @throws ApiException if user is not found or operation fails
     */
    @Transactional
    public void deactivateAccount(Long id) {
        try {
            User user = getUserById(id);
            user.setStatus(AccountStatus.SUSPENDED);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            logger.info("User account deactivated for ID: " + id);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error deactivating user account: " + e.getMessage());
            throw new ApiException("Error deactivating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Reactivates a suspended user account.
     * 
     * @param id the user ID
     * @throws ApiException if user is not found or operation fails
     */
    @Transactional
    public void reactivateAccount(Long id) {
        try {
            User user = getUserById(id);
            
            if (user.getStatus() == AccountStatus.DELETED) {
                throw new ApiException("Cannot reactivate a deleted account", HttpStatus.BAD_REQUEST);
            }
            
            user.setStatus(AccountStatus.ACTIVE);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            logger.info("User account reactivated for ID: " + id);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error reactivating user account: " + e.getMessage());
            throw new ApiException("Error reactivating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Deletes a user account (soft delete).
     * 
     * @param id the user ID
     * @throws ApiException if user is not found or operation fails
     */
    @Transactional
    public void deleteAccount(Long id) {
        try {
            User user = getUserById(id);
            // First mark as deleted for soft delete
            user.setStatus(AccountStatus.DELETED);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            logger.info("User account marked as deleted for ID: " + id);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error deleting user account: " + e.getMessage());
            throw new ApiException("Error deleting account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Gets users by status.
     *
     * @param status the account status
     * @return list of users with the specified status
     */
    public List<User> getUsersByStatus(AccountStatus status) {
        logger.info("Fetching users by status: " + status);
        return userRepository.findByStatus(status);
    }
    
    /**
     * Gets users by status with pagination.
     *
     * @param status the account status
     * @param pageable pagination parameters
     * @return page of users with the specified status
     */
    public Page<User> getUsersByStatus(AccountStatus status, Pageable pageable) {
        logger.info("Fetching users by status with pagination: " + status);
        return userRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }
    
    /**
     * Checks if a user exists by email.
     * 
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Checks if a user exists by username.
     * 
     * @param username the username to check
     * @return true if a user with the username exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByUsername(username);
    }
}