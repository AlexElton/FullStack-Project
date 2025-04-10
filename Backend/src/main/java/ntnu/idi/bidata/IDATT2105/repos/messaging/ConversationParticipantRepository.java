package ntnu.idi.bidata.IDATT2105.repos.messaging;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.messaging.Conversation;
import ntnu.idi.bidata.IDATT2105.models.messaging.ConversationParticipant;
import ntnu.idi.bidata.IDATT2105.models.messaging.ConversationParticipantId;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for ConversationParticipant entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for the many-to-many relationship between Users and Conversations.
 */
@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, ConversationParticipantId> {
  
  /**
   * Finds all participants in a specific conversation.
   * 
   * @param conversation the conversation to find participants for
   * @return a list of participants in the specified conversation
   */
  List<ConversationParticipant> findByConversation(Conversation conversation);
  
  /**
   * Finds all conversation participations for a specific user.
   * 
   * @param user the user to find conversation participations for
   * @return a list of conversation participations for the specified user
   */
  List<ConversationParticipant> findByUser(User user);
  
  /**
   * Finds the participation record for a specific user in a specific conversation.
   * 
   * @param conversation the conversation to check
   * @param user the user to check
   * @return an Optional containing the participation record if found, or empty if not found
   */
  Optional<ConversationParticipant> findByConversationAndUser(Conversation conversation, User user);
  
  /**
   * Finds all conversation participations that are muted.
   * 
   * @return a list of muted conversation participations
   */
  List<ConversationParticipant> findByIsMutedTrue();
}