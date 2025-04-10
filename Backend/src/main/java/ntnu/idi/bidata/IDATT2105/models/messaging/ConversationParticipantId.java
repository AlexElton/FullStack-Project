package ntnu.idi.bidata.IDATT2105.models.messaging;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for the ConversationParticipant entity.
 */
public class ConversationParticipantId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long conversation;
    private Long user;
    
    public ConversationParticipantId() {
    }
    
    public ConversationParticipantId(Long conversationId, Long userId) {
        this.conversation = conversationId;
        this.user = userId;
    }
    
    /**
     * Checks for equality between this object and another object.
     * 
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationParticipantId that = (ConversationParticipantId) o;
        return Objects.equals(conversation, that.conversation) &&
               Objects.equals(user, that.user);
    }
    
    /**
     * Generates a hash code for this object.
     * 
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(conversation, user);
    }
}