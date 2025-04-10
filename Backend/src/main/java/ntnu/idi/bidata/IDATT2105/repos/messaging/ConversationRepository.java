package ntnu.idi.bidata.IDATT2105.repos.messaging;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.messaging.Conversation;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for Conversation entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for message conversations.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
  
  /**
   * Finds all conversations related to a specific item.
   * 
   * @param item the item to find conversations for
   * @return a list of conversations related to the specified item
   */
  List<Conversation> findByItem(Item item);
  
  /**
   * Finds all conversations where a specific user is a participant.
   * 
   * @param user the user to find conversations for
   * @return a list of conversations where the specified user is a participant
   */
  @Query("SELECT c FROM Conversation c JOIN c.participants cp WHERE cp.user = ?1")
  List<Conversation> findConversationsByParticipant(User user);
  
  /**
   * Finds conversations where a specific user is a participant, ordered by update time (newest first), with pagination.
   * 
   * @param user the user to find conversations for
   * @param pageable the pagination information
   * @return a paginated list of conversations for the specified user, ordered by update time
   */
  @Query("SELECT c FROM Conversation c JOIN c.participants cp WHERE cp.user = ?1 ORDER BY c.updatedAt DESC")
  Page<Conversation> findConversationsByParticipantOrderByUpdatedAtDesc(User user, Pageable pageable);
}