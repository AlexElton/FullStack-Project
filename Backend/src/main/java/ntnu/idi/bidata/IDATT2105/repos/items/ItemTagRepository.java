package ntnu.idi.bidata.IDATT2105.repos.items;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.ItemTag;
import ntnu.idi.bidata.IDATT2105.models.items.ItemTagId;
import ntnu.idi.bidata.IDATT2105.models.items.Tag;

/**
 * Repository interface for ItemTag entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods for the many-to-many relationship between Items and Tags.
 */
@Repository
public interface ItemTagRepository extends JpaRepository<ItemTag, ItemTagId> {
  
  /**
   * Finds all ItemTags for a specific item.
   * 
   * @param item the item to find tags for
   * @return a list of ItemTags associated with the specified item
   */
  List<ItemTag> findByItem(Item item);
  
  /**
   * Finds all ItemTags for a specific tag.
   * 
   * @param tag the tag to find associated items for
   * @return a list of ItemTags associated with the specified tag
   */
  List<ItemTag> findByTag(Tag tag);
  
  /**
   * Deletes all ItemTags for a specific item.
   * 
   * @param item the item to delete tags for
   */
  void deleteByItem(Item item);
  
  /**
   * Deletes all ItemTags for a specific tag.
   * 
   * @param tag the tag to delete associations for
   */
  void deleteByTag(Tag tag);
}