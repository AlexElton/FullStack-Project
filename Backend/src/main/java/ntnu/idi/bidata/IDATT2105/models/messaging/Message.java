package ntnu.idi.bidata.IDATT2105.models.messaging;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Entity class representing a message between users.
 */
@Entity
@Table(name = "messages", indexes = {
  @Index(name = "idx_conversation", columnList = "conversation_id"),
  @Index(name = "idx_sender", columnList = "sender_id"),
  @Index(name = "idx_receiver", columnList = "receiver_id"),
  @Index(name = "idx_created", columnList = "created_at")
})
public class Message {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conversation_id", nullable = false)
  private Conversation conversation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver;

  @Column(name = "message_text", columnDefinition = "TEXT", nullable = false)
  private String messageText;

  @Column(name = "is_read", nullable = false)
  private Boolean isRead = false;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // Default constructor
  public Message() {
  }

  // Constructor with all required fields
  public Message(Conversation conversation, User sender, User receiver, String messageText) {
    this.conversation = conversation;
    this.sender = sender;
    this.receiver = receiver;
    this.messageText = messageText;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Conversation getConversation() {
    return conversation;
  }

  public void setConversation(Conversation conversation) {
    this.conversation = conversation;
  }

  public User getSender() {
    return sender;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public User getReceiver() {
    return receiver;
  }

  public void setReceiver(User receiver) {
    this.receiver = receiver;
  }

  public String getMessageText() {
    return messageText;
  }

  public void setMessageText(String messageText) {
    this.messageText = messageText;
  }

  public Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(Boolean isRead) {
    this.isRead = isRead;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Message message = (Message) o;
    return id.equals(message.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Message{" +
           "id=" + id +
           ", conversation=" + conversation.getId() +
           ", sender=" + sender.getUsername() +
           ", receiver=" + receiver.getUsername() +
           ", messageText='" + (messageText != null ? messageText.substring(0, Math.min(20, messageText.length())) + "..." : null) + '\'' +
           ", isRead=" + isRead +
           ", createdAt=" + createdAt +
           '}';
  }
}
