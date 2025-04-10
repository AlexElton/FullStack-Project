package ntnu.idi.bidata.IDATT2105.repos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.enums.AccountStatus;
import ntnu.idi.bidata.IDATT2105.models.user.User;

/**
 * Repository interface for User entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds a User by their email address.
   * 
   * @param email the email address of the user
   * @return an Optional containing the User if found, or empty if not found
   */
  Optional<User> findByEmail(String email);

  /**
   * Finds a User by their username.
   * 
   * @param username the username of the user
   * @return an Optional containing the User if found, or empty if not found
   */
  Optional<User> findByUsername(String username);

  /**
   * Checks if a User exists by their email address.
   * 
   * @param email the email address of the user
   * @return true if the user exists, false otherwise
   */
  boolean existsByEmail(String email);

  /**
   * Checks if a User exists by their username.
   * 
   * @param username the username of the user
   * @return true if the user exists, false otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Finds a User by their ID.
   * 
   * @param id the ID of the user
   * @return an Optional containing the User if found, or empty if not found
   */
  List<User> findByStatus(AccountStatus status);

  /**
   * Finds a User by their ID and status.
   * 
   * @param id     the ID of the user
   * @param status the status of the user
   * @return an Optional containing the User if found, or empty if not found
   */
  Page<User> findByStatusOrderByCreatedAtDesc(AccountStatus status, Pageable pageable);

  /**
   * Finds a User by their ID and status.
   * 
   * @param id     the ID of the user
   * @param status the status of the user
   * @return an Optional containing the User if found, or empty if not found
   */
  List<User> findByLastLoginAtBefore(LocalDateTime date);
}
