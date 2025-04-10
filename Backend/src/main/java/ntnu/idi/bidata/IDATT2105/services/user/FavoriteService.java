package ntnu.idi.bidata.IDATT2105.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.notification.Notification;
import ntnu.idi.bidata.IDATT2105.models.user.Favorite;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;
import ntnu.idi.bidata.IDATT2105.repos.notification.NotificationRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.FavoriteRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;
import ntnu.idi.bidata.IDATT2105.models.enums.NotificationType;

import java.util.List;

@Service
public class FavoriteService {
    
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final NotificationRepository notificationRepository;
    
    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, 
                          UserRepository userRepository,
                          ItemRepository itemRepository,
                          NotificationRepository notificationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.notificationRepository = notificationRepository;
    }
    
    /**
     * Adds an item to a user's favorites
     * 
     * @param userId the user ID
     * @param itemId the item ID
     * @return the created favorite
     */
    @Transactional
    public Favorite addFavorite(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
        
        // Check if item is active
        if (item.getStatus() != ItemStatus.ACTIVE) {
            throw new ApiException("Cannot favorite an inactive item", HttpStatus.BAD_REQUEST);
        }
        
        // Check if already favorited
        if (favoriteRepository.existsByUserAndItem(user, item)) {
            throw new ApiException("Item already in favorites", HttpStatus.CONFLICT);
        }
        
        // Create favorite
        Favorite favorite = new Favorite(user, item);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        
        // Notify item seller (optional)
        User seller = item.getSeller();
        if (!seller.getId().equals(userId)) {
            Notification notification = new Notification(
                seller,
                NotificationType.SYSTEM,
                itemId,
                "New favorite",
                "Your item \"" + item.getTitle() + "\" was added to someone's favorites!"
            );
            notificationRepository.save(notification);
        }
        
        return savedFavorite;
    }
    
    /**
     * Removes an item from a user's favorites
     * 
     * @param userId the user ID
     * @param itemId the item ID
     */
    @Transactional
    public void removeFavorite(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
        
        // Check if in favorites
        if (!favoriteRepository.existsByUserAndItem(user, item)) {
            throw new ApiException("Item not in favorites", HttpStatus.NOT_FOUND);
        }
        
        // Remove favorite
        favoriteRepository.deleteByUserAndItem(user, item);
    }
    
    /**
     * Gets all favorites for a user with pagination
     * 
     * @param userId the user ID
     * @param pageable pagination parameters
     * @return page of user's favorites
     */
    public Page<Favorite> getUserFavorites(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }
    
    /**
     * Checks if a user has favorited an item
     * 
     * @param userId the user ID
     * @param itemId the item ID
     * @return true if the user has favorited the item, false otherwise
     */
    public boolean isFavorite(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
        
        return favoriteRepository.existsByUserAndItem(user, item);
    }
    
    /**
     * Gets the count of favorites for an item
     * 
     * @param itemId the item ID
     * @return the number of users who have favorited the item
     */
    public long getFavoriteCount(Long itemId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
        
        return favoriteRepository.countByItem(item);
    }
    
    /**
     * Gets users who have favorited an item
     * 
     * @param itemId the item ID
     * @param sellerId the seller ID (for verification)
     * @return list of favorites for the item
     */
    public List<Favorite> getItemFavorites(Long itemId, Long sellerId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
        
        // Verify ownership
        if (!item.getSeller().getId().equals(sellerId)) {
            throw new ApiException("You can only view favorites for your own items", HttpStatus.FORBIDDEN);
        }
        
        return favoriteRepository.findByItem(item);
    }
}