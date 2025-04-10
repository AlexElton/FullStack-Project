package ntnu.idi.bidata.IDATT2105.models.messaging;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Entity class representing a participant in a conversation.
 * Uses a composite primary key of conversation_id and user_id.
 */
@Entity
@Table(name = "conversation_participants", indexes = {
  @Index(name = "idx_user", columnList = "user_id")
})
@IdClass(ConversationParticipantId.class)
public class ConversationParticipant {
  
  @Id
  @ManyToOne
  @JoinColumn(name = "conversation_id", nullable = false)
  private Conversation conversation;

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "is_muted", nullable = false)
  private Boolean isMuted = false;

  @Column(name = "last_read_at")
  private LocalDateTime lastReadAt;

  // Default constructor
  public ConversationParticipant() {
  }

  // Constructor with required fields
  public ConversationParticipant(Conversation conversation, User user) {
    this.conversation = conversation;
    this.user = user;
  }

  public Conversation getConversation() {
    return conversation;
  }

  public void setConversation(Conversation conversation) {
    this.conversation = conversation;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Boolean getIsMuted() {
    return isMuted;
  }

  public void setIsMuted(Boolean isMuted) {
    this.isMuted = isMuted;
  }

  public LocalDateTime getLastReadAt() {
    return lastReadAt;
  }

  public void setLastReadAt(LocalDateTime lastReadAt) {
    this.lastReadAt = lastReadAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    ConversationParticipant that = (ConversationParticipant) o;
    return conversation.equals(that.conversation) && user.equals(that.user);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(conversation, user);
  }

  @Override
  public String toString() {
    return "ConversationParticipant{" +
           "conversation=" + conversation.getId() +
           ", user=" + user.getUsername() +
           ", isMuted=" + isMuted +
           '}';
  }
}
