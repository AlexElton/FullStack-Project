package ntnu.idi.bidata.IDATT2105.models.items;

import java.io.Serializable;
import java.util.Objects;

public class ItemTagId implements Serializable {
  private Long item;
  private long tag;

  /**
   * Default constructor for JPA.
   * This constructor is used by JPA to create an instance of the class when retrieving data from the database.
   */
  public ItemTagId() {
  }

  /**
   * Constructor to create an instance of ItemTagId with the specified item and tag.
   *
   * @param item the ID of the item
   * @param tag  the ID of the tag
   */
  public ItemTagId(Long item, long tag) {
    this.item = item;
    this.tag = tag;
  }


  /**
   * Compares this ItemTagId object with another object for equality.
   * Two ItemTagId instances are considered equal if their item and tag fields are equal.
   * 
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   * @see #hashCode()
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ItemTagId that = (ItemTagId) o;
    return Objects.equals(item, that.item) && Objects.equals(tag, that.tag);
  }


  /**
   * Returns the hash code value for this ItemTagId object.
   * The hash code is computed based on the item and tag fields.
   * 
   * @return the hash code value for this object
   * @see #equals(Object)
   */
  @Override
  public int hashCode() {
    return Objects.hash(item, tag);
  }
}
