package ntnu.idi.bidata.IDATT2105.models.items;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * JPA entity representing a category in the system.
 * <p>
 * This class is used to map the categories table in the database to a Java object.
 * </p>
 */
@Entity
@Table(name = "categories", indexes = {
  @Index(name = "idx_parent", columnList = "parent_category_id"),
  @Index(name = "idx_active", columnList = "is_active")
})
public class Category {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id", nullable = false)
  private Long categoryId;
 
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;

  @JsonIgnore
  @OneToMany(mappedBy = "parentCategory")
  private List<Category> childCategories = new ArrayList<>();
  
  @JsonIgnore
  @OneToMany(mappedBy = "category")
  private List<Item> items = new ArrayList<>();

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "icon_url")
  private String iconUrl;

  @Column(name = "display_order", nullable = false)
  private Integer displayOrder = 0;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // Default constructor
  public Category() {
  }
  
  // Constructor with required name
  public Category(String name) {
    this.name = name;
  }
  
  // Constructor with parent and name
  public Category(Category parentCategory, String name) {
    this.parentCategory = parentCategory;
    this.name = name;
  }

  // Constructor with most fields
  public Category(Category parentCategory, String name, String description, 
                String iconUrl, Integer displayOrder, Boolean isActive) {
    this.parentCategory = parentCategory;
    this.name = name;
    this.description = description;
    this.iconUrl = iconUrl;
    this.displayOrder = displayOrder;
    this.isActive = isActive;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public Category getParentCategory() {
    return parentCategory;
  }

  public void setParentCategory(Category parentCategory) {
    this.parentCategory = parentCategory;
  }

  public List<Category> getChildCategories() {
    return childCategories;
  }

  public void setChildCategories(List<Category> childCategories) {
    this.childCategories = childCategories;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Compares this category to another object for equality.
   * Two categories are considered equal if they have the same categoryId.
   * 
   * @param o the object to compare to
   * @return true if the object is equal to this category, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Category category = (Category) o;
    return categoryId.equals(category.categoryId);
  }

  /**
   * Returns the hash code of this category.
   * The hash code is based on the categoryId.
   * 
   * @return the hash code of this category
   */
  @Override
  public int hashCode() {
    return categoryId.hashCode();
  }

  @Override
  public String toString() {
    return "Category{" +
           "categoryId=" + categoryId +
           ", name='" + name + '\'' +
           ", isActive=" + isActive +
           ", parentCategory=" + (parentCategory != null ? parentCategory.getCategoryId() : null) +
           '}';
  }
}