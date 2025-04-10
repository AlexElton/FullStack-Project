package ntnu.idi.bidata.IDATT2105.repos.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.user.User;
import ntnu.idi.bidata.IDATT2105.models.user.UserAddress;

/**
 * Repository interface for UserAddress entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for user addresses.
 */
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
  
  /**
   * Finds all addresses for a specific user.
   * 
   * @param user the user to find addresses for
   * @return a list of addresses for the specified user
   */
  List<UserAddress> findByUser(User user);
  
  /**
   * Finds the primary address for a specific user.
   * 
   * @param user the user to find the primary address for
   * @return an Optional containing the primary address if found, or empty if not found
   */
  Optional<UserAddress> findByUserAndIsPrimaryTrue(User user);
  
  /**
   * Finds all addresses in a specific city.
   * 
   * @param city the city to find addresses in
   * @return a list of addresses in the specified city
   */
  List<UserAddress> findByCity(String city);
  
  /**
   * Finds all addresses with a specific postal code.
   * 
   * @param postalCode the postal code to find addresses for
   * @return a list of addresses with the specified postal code
   */
  List<UserAddress> findByPostalCode(String postalCode);
  
  /**
   * Finds addresses near a specified geographic location.
   * 
   * @param longitude the longitude coordinate
   * @param latitude the latitude coordinate
   * @param distanceMeters the maximum distance in meters
   * @return a list of addresses within the specified distance from the location
   */
  @Query(value = "SELECT * FROM user_addresses WHERE ST_Distance_Sphere(point(longitude, latitude), point(?1, ?2)) <= ?3", 
         nativeQuery = true)
  List<UserAddress> findAddressesNearLocation(double longitude, double latitude, double distanceMeters);
}