package ntnu.idi.bidata.IDATT2105.dtos;

import ntnu.idi.bidata.IDATT2105.models.enums.Role;

/**
 * Data Transfer Object for authentication responses.
 */
public class AuthResponseDTO {
    
    private String token;
    private Long userId;
    private String username;
    private Role role;
    
    // Default constructor for Jackson
    public AuthResponseDTO() {
    }
    
    public AuthResponseDTO(String token, Long userId, String username, Role role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
}