package ntnu.idi.bidata.IDATT2105.models.items;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a many-to-many relationship between items and tags.
 * This class is used to store the association between an item and a tag in the database.
 * The primary key is a composite key consisting of item_id and tag_id.
 * 
 * @see Item
 * @see Tag
 */
@Entity
@Table(name = "item_tags", indexes = {
  @Index(name = "idx_tag_id", columnList = "tag_id")
})
@IdClass(ItemTagId.class)
public class ItemTag {
  
  @Id
  @ManyToOne
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @Id
  @ManyToOne
  @JoinColumn(name = "tag_id", nullable = false)
  private Tag tag;

  public ItemTag() {
    // Default constructor
  }

  public ItemTag(Item item, Tag tag) {
    this.item = item;
    this.tag = tag;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }
}
