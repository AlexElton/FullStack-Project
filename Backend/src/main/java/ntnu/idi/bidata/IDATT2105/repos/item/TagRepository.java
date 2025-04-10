package ntnu.idi.bidata.IDATT2105.repos.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.items.Tag;

/**
 * Repository interface for Tag entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  
  /**
   * Finds a tag by its name.
   * 
   * @param name the name of the tag
   * @return an Optional containing the Tag if found, or empty if not found
   */
  Optional<Tag> findByName(String name);
  
  /**
   * Finds all tags whose name contains the specified text (case-insensitive).
   * 
   * @param namePart the text to search for in tag names
   * @return a list of tags matching the search criteria
   */
  List<Tag> findByNameContainingIgnoreCase(String namePart);
  
  /**
   * Finds the most used tags, ordered by usage count.
   * 
   * @param limit the maximum number of tags to return
   * @return a list of the most frequently used tags
   */
  @Query("SELECT t FROM Tag t JOIN t.itemTags it GROUP BY t ORDER BY COUNT(it) DESC")
  List<Tag> findMostUsedTags(@Param("limit") int limit);

  List<Tag> findByNameInIgnoreCase(List<String> tags);
}