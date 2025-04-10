package ntnu.idi.bidata.IDATT2105.models.items;

import java.io.Serializable;
import java.util.Objects;

public class ItemTagId implements Serializable {
  private Long item;
  private long tag;

  public ItemTagId() {
  }

  public ItemTagId(Long item, long tag) {
    this.item = item;
    this.tag = tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ItemTagId that = (ItemTagId) o;
    return Objects.equals(item, that.item) && Objects.equals(tag, that.tag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(item, tag);
  }
}
