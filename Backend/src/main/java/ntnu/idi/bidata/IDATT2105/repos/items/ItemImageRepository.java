package ntnu.idi.bidata.IDATT2105.repos.items;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.ItemImage;

/**
 * Repository interface for ItemImage entity.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods.
 */
@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

  /**
   * Finds all images for a specific item.
   * 
   * @param item the item to find images for
   * @return a list of images for the specified item
   */
  List<ItemImage> findByItem(Item item);

  /**
   * Finds all images for a specific item, ordered by display order.
   * 
   * @param item the item to find images for
   * @return a list of ordered images for the specified item
   */
  List<ItemImage> findByItemOrderByDisplayOrderAsc(Item item);

  /**
   * Finds the primary image for a specific item.
   * 
   * @param item the item to find the primary image for
   * @return an Optional containing the primary image if found, or empty if not
   *         found
   */
  Optional<ItemImage> findByItemAndIsPrimaryTrue(Item item);

  /**
   * Deletes all images for a specific item.
   * 
   * @param item the item to delete images for
   */
  void deleteByItem(Item item);
}