package ntnu.idi.bidata.IDATT2105.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ntnu.idi.bidata.IDATT2105.dtos.CreateItemDTO;
import ntnu.idi.bidata.IDATT2105.dtos.UpdateItemDTO;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.services.item.ItemService;
import ntnu.idi.bidata.IDATT2105.services.auth.AuthService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {
    
    private final ItemService itemService;
    private final AuthService authService;
    
    @Autowired
    public ItemController(ItemService itemService, AuthService authService) {
        this.itemService = itemService;
        this.authService = authService;
    }
    
    @PostMapping
    public ResponseEntity<Item> createItem(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateItemDTO itemDTO) {
        Long userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        Item item = itemService.createItem(userId, itemDTO);
        return ResponseEntity.ok(item);
    }
    
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(
            @PathVariable Long itemId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        }
        Item item = itemService.viewItem(itemId, userId);
        return ResponseEntity.ok(item);
    }
    
    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(
            @PathVariable Long itemId,
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UpdateItemDTO itemDTO) {
        Long userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        Item item = itemService.updateItem(itemId, userId, itemDTO);
        return ResponseEntity.ok(item);
    }
    
    @PutMapping("/{itemId}/status")
    public ResponseEntity<Item> changeItemStatus(
            @PathVariable Long itemId,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam ItemStatus status) {
        Long userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        Item item = itemService.changeItemStatus(itemId, userId, status);
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long itemId,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = authService.validateTokenAndGetUserId(authHeader.substring(7));
        itemService.deleteItem(itemId, userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<Item>> getItemsBySeller(
            @PathVariable Long sellerId,
            Pageable pageable) {
        Page<Item> items = itemService.getItemsBySeller(sellerId, pageable);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Item>> getItemsByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        Page<Item> items = itemService.getItemsByCategory(categoryId, pageable);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/active")
    public ResponseEntity<Page<Item>> getActiveItems(Pageable pageable) {
        Page<Item> items = itemService.getActiveItems(pageable);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Item>> searchItems(
            @RequestParam String query,
            Pageable pageable) {
        Page<Item> items = itemService.searchItemsByTitle(query, pageable);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<Page<Item>> getItemsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {
        Page<Item> items = itemService.getItemsByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/nearby")
    public ResponseEntity<List<Item>> getNearbyItems(
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam Double distance) {
        List<Item> items = itemService.getItemsNearLocation(longitude, latitude, distance);
        return ResponseEntity.ok(items);
    }
} 