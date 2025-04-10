package ntnu.idi.bidata.IDATT2105.dtos;

import java.util.List;

import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating a new conversation.
 */
public class CreateConversationDTO {
    
    private List<Long> participantIds;
    private Long itemId;
    private String title;
    
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String initialMessage;
    
    // Default constructor
    public CreateConversationDTO() {
    }
    
    // Constructor with fields
    public CreateConversationDTO(List<Long> participantIds, Long itemId, String title, String initialMessage) {
        this.participantIds = participantIds;
        this.itemId = itemId;
        this.title = title;
        this.initialMessage = initialMessage;
    }
    
    // Getters and setters
    public List<Long> getParticipantIds() {
        return participantIds;
    }
    
    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
    
    public Long getItemId() {
        return itemId;
    }
    
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getInitialMessage() {
        return initialMessage;
    }
    
    public void setInitialMessage(String initialMessage) {
        this.initialMessage = initialMessage;
    }
}