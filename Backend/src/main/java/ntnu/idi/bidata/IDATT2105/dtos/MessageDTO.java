package ntnu.idi.bidata.IDATT2105.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for messages.
 */
public class MessageDTO {
    
    @NotBlank(message = "Message text is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String messageText;
    
    // Default constructor
    public MessageDTO() {
    }
    
    // Constructor with fields
    public MessageDTO(String messageText) {
        this.messageText = messageText;
    }
    
    // Getters and setters
    public String getMessageText() {
        return messageText;
    }
    
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}