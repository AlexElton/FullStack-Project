package ntnu.idi.bidata.IDATT2105.repos.messaging;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.messaging.Conversation;
import ntnu.idi.bidata.IDATT2105.models.messaging.Message;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Message entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for messages between users.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  
  /**
   * Finds all messages in a specific conversation.
   * 
   * @param conversation the conversation to find messages for
   * @return a list of messages in the specified conversation
   */
  List<Message> findByConversation(Conversation conversation);
  
  /**
   * Finds messages in a specific conversation, ordered by creation date (newest first), with pagination.
   * 
   * @param conversation the conversation to find messages for
   * @param pageable the pagination information
   * @return a paginated list of messages in the specified conversation, ordered by creation date
   */
  Page<Message> findByConversationOrderByCreatedAtDesc(Conversation conversation, Pageable pageable);
  
  /**
   * Finds all unread messages for a specific recipient.
   * 
   * @param receiver the user who received the messages
   * @return a list of unread messages for the specified recipient
   */
  List<Message> findByReceiverAndIsReadFalse(User receiver);
  
  /**
   * Counts the number of unread messages for a specific recipient.
   * 
   * @param receiver the user who received the messages
   * @return the count of unread messages for the specified recipient
   */
  int countByReceiverAndIsReadFalse(User receiver);
  
  /**
   * Finds new messages in a conversation that were created after a specific time.
   * 
   * @param conversation the conversation to find messages for
   * @param since the time threshold for finding new messages
   * @return a list of messages created after the specified time
   */
  @Query("SELECT m FROM Message m WHERE m.conversation = ?1 AND m.createdAt > ?2")
  List<Message> findNewMessagesInConversation(Conversation conversation, LocalDateTime since);
  
  /**
   * Finds all messages sent by a specific user.
   * 
   * @param sender the user who sent the messages
   * @return a list of messages sent by the specified user
   */
  List<Message> findBySender(User sender);
}