package ntnu.idi.bidata.IDATT2105.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntnu.idi.bidata.IDATT2105.exceptions.ApiException;
import ntnu.idi.bidata.IDATT2105.models.enums.AccountStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.enums.Role;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;
import ntnu.idi.bidata.IDATT2105.repos.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Service for administrative operations.
 */
@Service
public class AdminService {

  private static final Logger logger = Logger.getLogger(AdminService.class.getName());

  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

  @Autowired
  public AdminService(UserRepository userRepository, ItemRepository itemRepository) {
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
  }

  /**
   * Sets a user's role
   * 
   * @param adminId the ID of the admin making the change
   * @param userId  the ID of the user to change
   * @param newRole the new role
   * @return the updated user
   */
  @Transactional
  public User setUserRole(Long adminId, Long userId, Role newRole) {
    // Verify admin permissions
    User admin = userRepository.findById(adminId)
        .orElseThrow(() -> new ApiException("Admin not found", HttpStatus.NOT_FOUND));

    if (admin.getRole() != Role.ADMIN) {
      throw new ApiException("Only administrators can change user roles", HttpStatus.FORBIDDEN);
    }

    // Get target user
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

    // Don't allow changing other admins
    if (user.getRole() == Role.ADMIN && !user.getId().equals(adminId)) {
      throw new ApiException("Cannot modify another admin's role", HttpStatus.FORBIDDEN);
    }

    user.setRole(newRole);
    return userRepository.save(user);
  }

  /**
   * Sets a user's account status
   * 
   * @param adminId the ID of the admin making the change
   * @param userId  the ID of the user to change
   * @param status  the new status
   * @return the updated user
   */
  @Transactional
  public User setUserStatus(Long adminId, Long userId, AccountStatus status) {
    // Verify admin permissions
    User admin = userRepository.findById(adminId)
        .orElseThrow(() -> new ApiException("Admin not found", HttpStatus.NOT_FOUND));

    if (admin.getRole() != Role.ADMIN) {
      throw new ApiException("Only administrators and moderators can change user status", HttpStatus.FORBIDDEN);
    }

    // Get target user
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

    // Don't allow changing admins
    if (user.getRole() == Role.ADMIN) {
      throw new ApiException("Cannot modify an admin's status", HttpStatus.FORBIDDEN);
    }

    // If status is BANNED or DELETED, also remove their listings
    if (status == AccountStatus.SUSPENDED || status == AccountStatus.DELETED) {
      removeUserListings(userId);
    }

    user.setStatus(status);
    return userRepository.save(user);
  }

  /**
   * Removes all listings for a user
   * 
   * @param userId the user ID
   * @return count of removed listings
   */
  @Transactional
  public int removeUserListings(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

    List<Item> activeItems = itemRepository.findBySellerAndStatus(user, ItemStatus.ACTIVE);
    int count = activeItems.size();

    for (Item item : activeItems) {
      item.setStatus(ItemStatus.DELETED);
      itemRepository.save(item);
    }

    logger.info("Removed " + count + " listings for user ID: " + userId);
    return count;
  }

  /**
   * Gets system statistics
   * 
   * @return map with system statistics
   */
  public Map<String, Object> getSystemStats() {
    Map<String, Object> stats = new HashMap<>();

    stats.put("totalUsers", userRepository.count());
    stats.put("totalItems", itemRepository.count());
    stats.put("activeItems", itemRepository.countByStatus(ItemStatus.ACTIVE));
    stats.put("soldItems", itemRepository.countByStatus(ItemStatus.SOLD));

    return stats;
  }
}