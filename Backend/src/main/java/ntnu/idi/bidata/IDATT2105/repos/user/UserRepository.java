package ntnu.idi.bidata.IDATT2105.repos.user;

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
   * Finds all Users with a particular account status.
   * 
   * @param status the account status to filter by
   * @return a list of Users with the specified status
   */
  List<User> findByStatus(AccountStatus status);

  /**
   * Finds Users with a specific status and returns them as a paginated result ordered by creation date (descending).
   * 
   * @param status the account status to filter by
   * @param pageable the pagination information
   * @return a Page of Users with the specified status
   */
  Page<User> findByStatusOrderByCreatedAtDesc(AccountStatus status, Pageable pageable);

  /**
   * Finds Users who haven't logged in since a specific date.
   * 
   * @param date the date threshold for last login
   * @return a list of Users who haven't logged in since the specified date
   */
  List<User> findByLastLoginAtBefore(LocalDateTime date);
}