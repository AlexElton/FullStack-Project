package ntnu.idi.bidata.IDATT2105.models.messaging;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ntnu.idi.bidata.IDATT2105.models.items.Item;

/**
 * Entity class representing a conversation between users.
 */
@Entity
@Table(name = "conversations", indexes = {
  @Index(name = "idx_item", columnList = "item_id"),
  @Index(name = "idx_updated", columnList = "updated_at")
})
public class Conversation {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "conversation_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  @Column(name = "title", length = 100)
  private String title;

  @OneToMany(mappedBy = "conversation")
  private Set<ConversationParticipant> participants = new HashSet<>();

  @OneToMany(mappedBy = "conversation")
  private Set<Message> messages = new HashSet<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // Default constructor
  public Conversation() {
  }

  // Constructor with optional fields
  public Conversation(Item item, String title) {
    this.item = item;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Set<ConversationParticipant> getParticipants() {
    return participants;
  }

  public void setParticipants(Set<ConversationParticipant> participants) {
    this.participants = participants;
  }

  public Set<Message> getMessages() {
    return messages;
  }

  public void setMessages(Set<Message> messages) {
    this.messages = messages;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Conversation that = (Conversation) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Conversation{" +
           "id=" + id +
           ", item=" + (item != null ? item.getItemId() : null) +
           ", title='" + title + '\'' +
           ", createdAt=" + createdAt +
           '}';
  }
}
