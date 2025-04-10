package ntnu.idi.bidata.IDATT2105.controller;

import ntnu.idi.bidata.IDATT2105.dtos.CreateConversationDTO;
import ntnu.idi.bidata.IDATT2105.models.messaging.Conversation;
import ntnu.idi.bidata.IDATT2105.models.messaging.Message;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.services.messaging.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    // Create a new conversation
    @PostMapping
    public ResponseEntity<Conversation> createConversation(
            @RequestParam Long initiatorId,
            @RequestBody CreateConversationDTO dto) {
        Conversation createdConversation = conversationService.createConversation(initiatorId, dto);
        return ResponseEntity.ok(createdConversation);
    }

    // Get a conversation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(
            @PathVariable Long id,
            @RequestParam Long userId) {
        Conversation conversation = conversationService.getConversation(id, userId);
        return ResponseEntity.ok(conversation);
    }

    // Get all conversations for a user
    @GetMapping
    public ResponseEntity<Page<Conversation>> getUserConversations(
            @RequestParam Long userId,
            Pageable pageable) {
        Page<Conversation> conversations = conversationService.getUserConversations(userId, pageable);
        return ResponseEntity.ok(conversations);
    }

    // Get messages in a conversation
    @GetMapping("/{id}/messages")
    public ResponseEntity<Page<Message>> getConversationMessages(
            @PathVariable Long id,
            @RequestParam Long userId,
            Pageable pageable) {
        Page<Message> messages = conversationService.getConversationMessages(id, userId, pageable);
        return ResponseEntity.ok(messages);
    }

    // Get participants of a conversation
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<User>> getConversationParticipants(
            @PathVariable Long id,
            @RequestParam Long userId) {
        List<User> participants = conversationService.getConversationParticipants(id, userId);
        return ResponseEntity.ok(participants);
    }

    // Mute or unmute a conversation
    @PutMapping("/{id}/mute")
    public ResponseEntity<Void> setConversationMute(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam boolean muted) {
        conversationService.setConversationMute(id, userId, muted);
        return ResponseEntity.noContent().build();
    }

    // Leave a conversation
    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leaveConversation(
            @PathVariable Long id,
            @RequestParam Long userId) {
        conversationService.leaveConversation(id, userId);
        return ResponseEntity.noContent().build();
    }
}