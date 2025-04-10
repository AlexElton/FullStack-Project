package ntnu.idi.bidata.IDATT2105.models.user;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key class for Favorite entity.
 */
public class FavoriteId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long user;
    private Long item;
    
    public FavoriteId() {
    }
    
    public FavoriteId(Long userId, Long itemId) {
        this.user = userId;
        this.item = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(user, that.user) && 
               Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, item);
    }
}
