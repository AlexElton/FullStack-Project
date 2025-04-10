package ntnu.idi.bidata.IDATT2105.models.items;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * JPA entity representing an item tag in the system.
 */
@Entity
public class Tag {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tag_id", nullable = false)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @OneToMany(mappedBy = "tag")
  private Set<ItemTag> itemTags;

  public Tag() {
    // Default constructor
  }

  /**
   * Constructor for creating a Tag with a name.
   *
   * @param name the tag name
   */
  public Tag(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Set<ItemTag> getItemTags() {
    return itemTags;
  }

  public void setItemTags(Set<ItemTag> itemTags) {
    this.itemTags = itemTags;
  }

  /**
   * Overrides the equals method to compare Tag objects based on their id and name.
   *
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   * 
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tag)) return false;

    Tag tag = (Tag) o;

    if (!id.equals(tag.id)) return false;
    return name.equals(tag.name);
  }

  /**
   * Overrides the hashCode method to generate a hash code based on the id and name.
   * 
   * @return the hash code of the Tag object
   */
  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Tag{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
