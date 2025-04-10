package ntnu.idi.bidata.IDATT2105.dtos;

import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating user profile information.
 */
public class UpdateUserDTO {
    
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Size(min = 8, max = 20, message = "Phone number must be between 8 and 20 characters")
    private String phoneNumber;
    
    private String profilePicUrl;
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
    
    // Default constructor
    public UpdateUserDTO() {
    }
    
    // Getters and setters
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
}