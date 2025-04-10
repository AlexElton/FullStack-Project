package ntnu.idi.bidata.IDATT2105.repos.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.enums.Theme;
import ntnu.idi.bidata.IDATT2105.models.user.UserSettings;

/**
 * Repository interface for UserSettings entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods.
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

  /**
   * Finds all UserSettings with a specific theme.
   * 
   * @param theme the theme to filter by
   * @return a list of UserSettings with the specified theme
   */
  List<UserSettings> findByTheme(Theme theme);

  /**
   * Finds all UserSettings where email notifications are enabled.
   * 
   * @return a list of UserSettings with email notifications enabled
   */
  List<UserSettings> findByEmailNotificationsTrue();

  /**
   * Finds all UserSettings where push notifications are enabled.
   * 
   * @return a list of UserSettings with push notifications enabled
   */
  List<UserSettings> findByPushNotificationsTrue();

  /**
   * Finds all UserSettings with a specific language preference.
   * 
   * @param language the language code to filter by
   * @return a list of UserSettings with the specified language
   */
  List<UserSettings> findByLanguage(String language);
}