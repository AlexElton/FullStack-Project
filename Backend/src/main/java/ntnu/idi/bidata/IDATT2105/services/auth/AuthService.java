package ntnu.idi.bidata.IDATT2105.services.auth;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ntnu.idi.bidata.IDATT2105.dtos.AuthRequestDTO;
import ntnu.idi.bidata.IDATT2105.dtos.AuthResponseDTO;
import ntnu.idi.bidata.IDATT2105.dtos.RegisterRequestDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.AccountStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.Role;
import ntnu.idi.bidata.IDATT2105.models.enums.Theme;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.models.user.UserSettings;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserSettingsRepository;
import ntnu.idi.bidata.IDATT2105.services.security.JwtService;

/**
 * Service for handling user authentication and registration.
 */
@Service
@Slf4j
public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    
    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs a new AuthService with the necessary dependencies.
     * 
     * @param userRepository repository for user data
     * @param userSettingsRepository repository for user settings
     * @param passwordEncoder encoder for password hashing
     * @param jwtService service for JWT operations
     * @param authenticationManager Spring Security authentication manager
     */
    @Autowired
    public AuthService(UserRepository userRepository, 
                      UserSettingsRepository userSettingsRepository,
                      PasswordEncoder passwordEncoder, 
                      JwtService jwtService, 
                      AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the system.
     *
     * @param request the registration request
     * @return authentication response with token
     * @throws ApiException if email or username already exists
     */
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Validate request
        if (request == null) {
            throw new ApiException("Registration request cannot be null", HttpStatus.BAD_REQUEST);
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already in use", HttpStatus.CONFLICT);
        }

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException("Username already taken", HttpStatus.CONFLICT);
        }

        try {
            // Create new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setStatus(AccountStatus.ACTIVE);
            user.setRole(Role.USER); // Default role
            user.setCreatedAt(LocalDateTime.now());
            user.setLastLoginAt(LocalDateTime.now());
            
            User savedUser = userRepository.save(user);
            userRepository.flush(); 
            log.info("User updated: " + savedUser);
            
            // Create default user settings
            UserSettings settings = new UserSettings(savedUser);
            settings.setLanguage("en"); // Default language
            settings.setEmailNotifications(true);
            settings.setPushNotifications(true);
            settings.setTheme(Theme.LIGHT);
            userSettingsRepository.save(settings);
            
            // Generate JWT token
            String token = jwtService.generateToken(savedUser);
            
            logger.info("User registered successfully: " + savedUser.getUsername());
            return new AuthResponseDTO(token, savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
        } catch (Exception e) {
            logger.severe("Error during user registration: " + e.getMessage());
            throw new ApiException("Registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logs out a user by invalidating their JWT token.
     *
     * @param token the JWT token to invalidate
     */
    public void logout(String token) {
        //TODO Implement: Invalidate the token 
        logger.info("User logged out: " + token);
    }

    /**
     * Refreshes the JWT token for a user.
     *
     * @param token the JWT token to refresh
     * @return authentication response with new token
     * @throws ApiException if token is invalid or user not found
     */
    public AuthResponseDTO refreshToken(String token) throws ApiException {
        // Validate the token and extract user details
        String username = jwtService.extractUsername(token);
        if (username == null) {
            throw new ApiException("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // Get user from repository
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        // Generate new JWT token
        String newToken = jwtService.generateToken(user);
        
        logger.info("Token refreshed for user: " + user.getUsername());
        return new AuthResponseDTO(newToken, user.getId(), user.getUsername(), user.getRole());
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the authentication request
     * @return authentication response with token
     * @throws ApiException if authentication fails or user is not active
     */
    public AuthResponseDTO login(AuthRequestDTO request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), 
                    request.getPassword()
                )
            );
            
            // Get user from repository
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
            // Check if user is active
            if (user.getStatus() != AccountStatus.ACTIVE) {
                throw new ApiException("Account is not active", HttpStatus.FORBIDDEN);
            }
            
            // Update last login time
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            
            // Generate JWT token
            String token = jwtService.generateToken(user);
            
            logger.info("User logged in successfully: " + user.getUsername());
            return new AuthResponseDTO(token, user.getId(), user.getUsername(), user.getRole());
        } catch (BadCredentialsException e) {
            logger.warning("Failed login attempt for user: " + request.getUsername());
            throw new ApiException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error during login: " + e.getMessage());
            throw new ApiException("Authentication failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Validates a JWT token and returns the associated user ID.
     * 
     * @param token the JWT token to validate
     * @return the user ID associated with the token
     * @throws ApiException if token is invalid or user not found
     */
    public Long validateTokenAndGetUserId(String token) {
        if (token == null || token.isEmpty()) {
            throw new ApiException("Token cannot be null or empty", HttpStatus.BAD_REQUEST);
        }
        
        try {
            String username = jwtService.extractUsername(token);
            if (username == null) {
                throw new ApiException("Invalid token", HttpStatus.UNAUTHORIZED);
            }
            
            return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error validating token: " + e.getMessage());
            throw new ApiException("Token validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    
    /**
     * Checks if a user with the given email exists.
     * 
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    public boolean emailExists(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Checks if a user with the given username exists.
     * 
     * @param username the username to check
     * @return true if a user with the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Gets a user's role by their ID
     * 
     * @param userId the user ID
     * @return the user's role
     * @throws ApiException if user not found
     */
    public Role getUserRole(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        return user.getRole();
    }
}