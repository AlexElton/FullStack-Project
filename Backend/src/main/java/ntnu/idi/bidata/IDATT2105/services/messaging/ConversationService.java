package ntnu.idi.bidata.IDATT2105.services.messaging;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.dtos.CreateConversationDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.messaging.Conversation;
import ntnu.idi.bidata.IDATT2105.models.messaging.ConversationParticipant;
import ntnu.idi.bidata.IDATT2105.models.messaging.Message;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;
import ntnu.idi.bidata.IDATT2105.repos.messaging.ConversationParticipantRepository;
import ntnu.idi.bidata.IDATT2105.repos.messaging.ConversationRepository;
import ntnu.idi.bidata.IDATT2105.repos.messaging.MessageRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.services.notification.NotificationService;

@Service
public class ConversationService {
    
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final NotificationService notificationService;
    
    @Autowired
    public ConversationService(ConversationRepository conversationRepository,
                              ConversationParticipantRepository participantRepository,
                              MessageRepository messageRepository,
                              UserRepository userRepository,
                              ItemRepository itemRepository,
                              NotificationService notificationService) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.notificationService = notificationService;
    }
    
    /**
     * Creates a new conversation
     * 
     * @param initiatorId the user ID of the conversation initiator
     * @param dto the conversation data
     * @return the created conversation
     */
    @Transactional
    public Conversation createConversation(Long initiatorId, CreateConversationDTO dto) {
        User initiator = userRepository.findById(initiatorId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        // Get participant users
        List<User> participants = new ArrayList<>();
        participants.add(initiator);
        
        for (Long participantId : dto.getParticipantIds()) {
            if (!participantId.equals(initiatorId)) {
                User participant = userRepository.findById(participantId)
                    .orElseThrow(() -> new ApiException("Participant not found: " + participantId, HttpStatus.NOT_FOUND));
                participants.add(participant);
            }
        }
        
        // Create conversation
        Conversation conversation = new Conversation();
        
        if (dto.getItemId() != null) {
            Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
            conversation.setItem(item);
            conversation.setTitle("About: " + item.getTitle());
        } else if (dto.getTitle() != null) {
            conversation.setTitle(dto.getTitle());
        } else {
            conversation.setTitle("Conversation");
        }
        
        Conversation savedConversation = conversationRepository.save(conversation);
        
        // Add participants
        for (User participant : participants) {
            ConversationParticipant cp = new ConversationParticipant();
            cp.setConversation(savedConversation);
            cp.setUser(participant);
            participantRepository.save(cp);
        }
        
        // Add initial message if provided
        if (dto.getInitialMessage() != null && !dto.getInitialMessage().trim().isEmpty()) {
            Message message = new Message();
            message.setConversation(savedConversation);
            message.setSender(initiator);
            // Set the receiver to the other participant
            User receiver = participants.stream()
                .filter(p -> !p.getId().equals(initiatorId))
                .findFirst()
                .orElseThrow(() -> new ApiException("No receiver found for message", HttpStatus.BAD_REQUEST));
            message.setReceiver(receiver);
            message.setMessageText(dto.getInitialMessage().trim());
            messageRepository.save(message);
            
            // Send notifications to other participants
            for (User participant : participants) {
                if (!participant.getId().equals(initiatorId)) {
                    notificationService.createNotification(
                        participant.getId(),
                        NotificationType.MESSAGE,
                        savedConversation.getId(),
                        "New message",
                        initiator.getUsername() + " sent you a message"
                    );
                }
            }
        }
        
        return savedConversation;
    }
    
    /**
     * Gets a conversation by ID
     * 
     * @param conversationId the conversation ID
     * @param userId the user ID (for permission check)
     * @return the conversation
     */
    public Conversation getConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ApiException("Conversation not found", HttpStatus.NOT_FOUND));
        
        // Verify permission
        if (!isParticipant(conversationId, userId)) {
            throw new ApiException("You don't have access to this conversation", HttpStatus.FORBIDDEN);
        }
        
        return conversation;
    }
    
    /**
     * Gets all conversations for a user with pagination
     * 
     * @param userId the user ID
     * @param pageable pagination parameters
     * @return page of conversations for the user
     */
    public Page<Conversation> getUserConversations(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return conversationRepository.findConversationsByParticipantOrderByUpdatedAtDesc(user, pageable);
    }
    
    /**
     * Gets messages in a conversation with pagination
     * 
     * @param conversationId the conversation ID
     * @param userId the user ID (for permission check)
     * @param pageable pagination parameters
     * @return page of messages in the conversation
     */
    public Page<Message> getConversationMessages(Long conversationId, Long userId, Pageable pageable) {
        Conversation conversation = getConversation(conversationId, userId);
        
        // Update last read time
        ConversationParticipant participant = participantRepository.findByConversationAndUser(conversation, 
                userRepository.getReferenceById(userId))
            .orElseThrow(() -> new ApiException("Participant not found", HttpStatus.NOT_FOUND));
        
        participant.setLastReadAt(LocalDateTime.now());
        participantRepository.save(participant);
        
        return messageRepository.findByConversationOrderByCreatedAtDesc(conversation, pageable);
    }
    
    /**
     * Gets the participants of a conversation
     * 
     * @param conversationId the conversation ID
     * @param userId the user ID (for permission check)
     * @return list of conversation participants
     */
    public List<User> getConversationParticipants(Long conversationId, Long userId) {
        Conversation conversation = getConversation(conversationId, userId);
        
        List<ConversationParticipant> participants = participantRepository.findByConversation(conversation);
        return participants.stream()
            .map(ConversationParticipant::getUser)
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if a user is a participant in a conversation
     * 
     * @param conversationId the conversation ID
     * @param userId the user ID
     * @return true if the user is a participant, false otherwise
     */
    public boolean isParticipant(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ApiException("Conversation not found", HttpStatus.NOT_FOUND));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return participantRepository.findByConversationAndUser(conversation, user).isPresent();
    }
    
    /**
     * Sets the mute status of a conversation for a user
     * 
     * @param conversationId the conversation ID
     * @param userId the user ID
     * @param muted true to mute, false to unmute
     */
    @Transactional
    public void setConversationMute(Long conversationId, Long userId, boolean muted) {
        Conversation conversation = getConversation(conversationId, userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        ConversationParticipant participant = participantRepository.findByConversationAndUser(conversation, user)
            .orElseThrow(() -> new ApiException("Participant not found", HttpStatus.NOT_FOUND));
        
        participant.setIsMuted(muted);
        participantRepository.save(participant);
    }
    
    /**
     * Leaves a conversation
     * 
     * @param conversationId the conversation ID
     * @param userId the user ID
     */
    @Transactional
    public void leaveConversation(Long conversationId, Long userId) {
        Conversation conversation = getConversation(conversationId, userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        ConversationParticipant participant = participantRepository.findByConversationAndUser(conversation, user)
            .orElseThrow(() -> new ApiException("Participant not found", HttpStatus.NOT_FOUND));
        
        participantRepository.delete(participant);
        
        // If no participants left, delete the conversation
        List<ConversationParticipant> remainingParticipants = participantRepository.findByConversation(conversation);
        if (remainingParticipants.isEmpty()) {
            conversationRepository.delete(conversation);
        }
    }
}