package ntnu.idi.bidata.IDATT2105.services.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.dtos.CreateItemDTO;
import ntnu.idi.bidata.IDATT2105.dtos.UpdateItemDTO;
import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Category;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.ItemImage;
import ntnu.idi.bidata.IDATT2105.models.items.ItemTag;
import ntnu.idi.bidata.IDATT2105.models.items.Tag;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.models.user.UserBrowsingHistory;
import ntnu.idi.bidata.IDATT2105.repos.item.CategoryRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemImageRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemTagRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.TagRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserBrowsingHistoryRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemService {
    
    private final ItemRepository itemRepository;
    private final ItemImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ItemTagRepository itemTagRepository;
    private final UserBrowsingHistoryRepository browsingHistoryRepository;
    
    @Autowired
    public ItemService(ItemRepository itemRepository, 
                      ItemImageRepository imageRepository,
                      CategoryRepository categoryRepository,
                      UserRepository userRepository,
                      TagRepository tagRepository,
                      ItemTagRepository itemTagRepository,
                      UserBrowsingHistoryRepository browsingHistoryRepository) {
        this.itemRepository = itemRepository;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.itemTagRepository = itemTagRepository;
        this.browsingHistoryRepository = browsingHistoryRepository;
    }
    
    /**
     * Creates a new item listing
     * 
     * @param sellerId the seller user ID
     * @param itemDTO the item data
     * @return the created item
     */
    @Transactional
    public Item createItem(Long sellerId, CreateItemDTO itemDTO) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new ApiException("Seller not found", HttpStatus.NOT_FOUND));
        
        Category category = categoryRepository.findById(itemDTO.getCategoryId())
            .orElseThrow(() -> new ApiException("Category not found", HttpStatus.NOT_FOUND));
        
        // Create new item
        Item item = new Item();
        item.setSeller(seller);
        item.setCategory(category);
        item.setTitle(itemDTO.getTitle());
        item.setBriefDescription(itemDTO.getBriefDescription());
        item.setFullDescription(itemDTO.getFullDescription());
        item.setPrice(itemDTO.getPrice());
        item.setCurrency(itemDTO.getCurrency());
        item.setCondition(itemDTO.getCondition());
        item.setQuantity(itemDTO.getQuantity());
        item.setAllowOffers(itemDTO.getAllowOffers());
        item.setAcceptVipps(itemDTO.getAcceptVipps());
        
        if (itemDTO.getLocationAddress() != null) {
            item.setLocationAddress(itemDTO.getLocationAddress());
        }
        
        if (itemDTO.getLatitude() != null && itemDTO.getLongitude() != null) {
            item.setLatitude(itemDTO.getLatitude());
            item.setLongitude(itemDTO.getLongitude());
        }
        
        // Set expiration date (default to 30 days if not specified)
        LocalDateTime expiresAt = itemDTO.getExpiresAt() != null ? 
            itemDTO.getExpiresAt() : LocalDateTime.now().plusDays(30);
        item.setExpiresAt(expiresAt);
        
        // Save the item
        Item savedItem = itemRepository.save(item);
        
        // Process images if provided
        if (itemDTO.getImageUrls() != null && !itemDTO.getImageUrls().isEmpty()) {
            for (int i = 0; i < itemDTO.getImageUrls().size(); i++) {
                String imageUrl = itemDTO.getImageUrls().get(i);
                ItemImage image = new ItemImage();
                image.setItem(savedItem);
                image.setImageUrl(imageUrl);
                image.setDisplayOrder(i);
                image.setIsPrimary(i == 0); // First image is primary
                imageRepository.save(image);
            }
        }
        
        // Process tags if provided
        if (itemDTO.getTags() != null && !itemDTO.getTags().isEmpty()) {
            processTags(savedItem, itemDTO.getTags());
        }
        
        return savedItem;
    }
    
    /**
     * Gets an item by its ID
     * 
     * @param itemId the item ID
     * @return the item
     */
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
    }
    
    /**
     * Views an item and updates view count and browsing history
     * 
     * @param itemId the item ID
     * @param userId the user ID (can be null for anonymous views)
     * @return the item
     */
    @Transactional
    public Item viewItem(Long itemId, Long userId) {
        Item item = getItemById(itemId);
        
        // Increment view count
        itemRepository.incrementViewsCount(itemId);
        item.setViewsCount(item.getViewsCount() + 1);
        
        // Record in browsing history if user is logged in
        if (userId != null) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            
            Optional<UserBrowsingHistory> existingHistory = 
                browsingHistoryRepository.findByUserAndItem(user, item);
            
            if (existingHistory.isPresent()) {
                UserBrowsingHistory history = existingHistory.get();
                history.incrementViewCount();
                browsingHistoryRepository.save(history);
            } else {
                UserBrowsingHistory history = new UserBrowsingHistory(user, item);
                browsingHistoryRepository.save(history);
            }
        }
        
        return item;
    }
    
    /**
     * Updates an item listing
     * 
     * @param itemId the item ID
     * @param sellerId the seller user ID
     * @param itemDTO the updated item data
     * @return the updated item
     */
    @Transactional
    public Item updateItem(Long itemId, Long sellerId, UpdateItemDTO itemDTO) {
        Item item = getItemById(itemId);
        
        // Verify ownership
        if (!item.getSeller().getId().equals(sellerId)) {
            throw new ApiException("You can only update your own items", HttpStatus.FORBIDDEN);
        }
        
        // Update category if changed
        if (itemDTO.getCategoryId() != null && !item.getCategory().getCategoryId().equals(itemDTO.getCategoryId())) {
            Category category = categoryRepository.findById(itemDTO.getCategoryId())
                .orElseThrow(() -> new ApiException("Category not found", HttpStatus.NOT_FOUND));
            item.setCategory(category);
        }
        
        // Update fields
        if (itemDTO.getTitle() != null) {
            item.setTitle(itemDTO.getTitle());
        }
        
        if (itemDTO.getBriefDescription() != null) {
            item.setBriefDescription(itemDTO.getBriefDescription());
        }
        
        if (itemDTO.getFullDescription() != null) {
            item.setFullDescription(itemDTO.getFullDescription());
        }
        
        if (itemDTO.getPrice() != null) {
            item.setPrice(itemDTO.getPrice());
        }
        
        if (itemDTO.getQuantity() != null) {
            item.setQuantity(itemDTO.getQuantity());
        }
        
        if (itemDTO.getAllowOffers() != null) {
            item.setAllowOffers(itemDTO.getAllowOffers());
        }
        
        if (itemDTO.getAcceptVipps() != null) {
            item.setAcceptVipps(itemDTO.getAcceptVipps());
        }
        
        if (itemDTO.getStatus() != null) {
            item.setStatus(itemDTO.getStatus());
        }
        
        if (itemDTO.getLocationAddress() != null) {
            item.setLocationAddress(itemDTO.getLocationAddress());
        }
        
        if (itemDTO.getLatitude() != null && itemDTO.getLongitude() != null) {
            item.setLatitude(itemDTO.getLatitude());
            item.setLongitude(itemDTO.getLongitude());
        }
        
        if (itemDTO.getExpiresAt() != null) {
            item.setExpiresAt(itemDTO.getExpiresAt());
        }
        
        // Save updated item
        Item updatedItem = itemRepository.save(item);
        
        // Update tags if provided
        if (itemDTO.getTags() != null) {
            itemTagRepository.deleteByItem(item);
            processTags(updatedItem, itemDTO.getTags());
        }
        
        return updatedItem;
    }
    
    /**
     * Changes the status of an item
     * 
     * @param itemId the item ID
     * @param sellerId the seller user ID
     * @param newStatus the new status
     * @return the updated item
     */
    @Transactional
    public Item changeItemStatus(Long itemId, Long sellerId, ItemStatus newStatus) {
        Item item = getItemById(itemId);
        
        // Verify ownership
        if (!item.getSeller().getId().equals(sellerId)) {
            throw new ApiException("You can only update your own items", HttpStatus.FORBIDDEN);
        }
        
        item.setStatus(newStatus);
        return itemRepository.save(item);
    }
    
    /**
     * Deletes an item listing
     * 
     * @param itemId the item ID
     * @param sellerId the seller user ID
     */
    @Transactional
    public void deleteItem(Long itemId, Long sellerId) {
        Item item = getItemById(itemId);
        
        // Verify ownership
        if (!item.getSeller().getId().equals(sellerId)) {
            throw new ApiException("You can only delete your own items", HttpStatus.FORBIDDEN);
        }
        
        // Mark as deleted (soft delete)
        item.setStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }
    
    /**
     * Gets items by seller ID with pagination
     * 
     * @param sellerId the seller ID
     * @param pageable pagination parameters
     * @return page of items by the seller
     */
    public Page<Item> getItemsBySeller(Long sellerId, Pageable pageable) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new ApiException("Seller not found", HttpStatus.NOT_FOUND));
        
        return itemRepository.findBySeller(seller, pageable);
    }
    
    /**
     * Gets items by category ID with pagination
     * 
     * @param categoryId the category ID
     * @param pageable pagination parameters
     * @return page of items in the category
     */
    public Page<Item> getItemsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ApiException("Category not found", HttpStatus.NOT_FOUND));
        
        return itemRepository.findByCategory(category, pageable);
    }
    
    /**
     * Gets active items with pagination
     * 
     * @param pageable pagination parameters
     * @return page of active items
     */
    public Page<Item> getActiveItems(Pageable pageable) {
        return itemRepository.findByStatus(ItemStatus.ACTIVE, pageable);
    }
    
    /**
     * Search items by title with pagination
     * 
     * @param query the search query
     * @param pageable pagination parameters
     * @return page of matching items
     */
    public Page<Item> searchItemsByTitle(String query, Pageable pageable) {
        return itemRepository.findByTitleContainingIgnoreCase(query, pageable);
    }
    
    /**
     * Gets items within a price range with pagination
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @param pageable pagination parameters
     * @return page of items within the price range
     */
    public Page<Item> getItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return itemRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }
    
    /**
     * Gets items near a location
     * 
     * @param longitude the longitude coordinate
     * @param latitude the latitude coordinate
     * @param distance the search radius in meters
     * @return list of nearby items
     */
    public List<Item> getItemsNearLocation(Double longitude, Double latitude, Double distance) {
        return itemRepository.findItemsNearLocation(longitude, latitude, distance);
    }
    
    /**
     * Process and associate tags with an item
     * 
     * @param item the item to associate tags with
     * @param tagNames the names of the tags
     */
    private void processTags(Item item, List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName.toLowerCase().trim())
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName.toLowerCase().trim());
                    return tagRepository.save(newTag);
                });
            tags.add(tag);
        }
        
        // Associate tags with the item
        tags.forEach(tag -> {
            ItemTag itemTag = new ItemTag(item, tag);
            itemTagRepository.save(itemTag);
        });
    }
}