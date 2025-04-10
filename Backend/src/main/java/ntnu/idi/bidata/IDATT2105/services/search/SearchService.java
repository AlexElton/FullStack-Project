package ntnu.idi.bidata.IDATT2105.services.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import ntnu.idi.bidata.IDATT2105.dtos.SearchCriteriaDTO;
import ntnu.idi.bidata.IDATT2105.dtos.SearchResultDTO;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemCondition;
import ntnu.idi.bidata.IDATT2105.models.enums.ItemStatus;
import ntnu.idi.bidata.IDATT2105.models.items.Category;
import ntnu.idi.bidata.IDATT2105.models.items.Item;
import ntnu.idi.bidata.IDATT2105.models.items.Tag;
import ntnu.idi.bidata.IDATT2105.repos.item.CategoryRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.ItemRepository;
import ntnu.idi.bidata.IDATT2105.repos.item.TagRepository;

/**
 * Service for advanced searching of items with multiple criteria.
 */
@Service
public class SearchService {
    
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    
    @Autowired
    public SearchService(ItemRepository itemRepository, 
                        CategoryRepository categoryRepository,
                        TagRepository tagRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }
    
    /**
     * Performs a search with multiple criteria.
     * 
     * @param criteria the search criteria
     * @param pageable pagination parameters
     * @return search results
     */
    public Page<Item> searchItems(SearchCriteriaDTO criteria, Pageable pageable) {
        // Build a specification based on criteria
        Specification<Item> spec = Specification.where(null);
        
        // Always filter for active items by default
        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), ItemStatus.ACTIVE));
        
        // Search by keyword (in title and description)
        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), keyword),
                cb.like(cb.lower(root.get("briefDescription")), keyword),
                cb.like(cb.lower(root.get("fullDescription")), keyword)
            ));
        }
        
        // Filter by category
        if (criteria.getCategoryId() != null) {
            // Include subcategories if requested
            if (criteria.isIncludeSubcategories()) {
                List<Long> categoryIds = getAllSubcategoryIds(criteria.getCategoryId());
                categoryIds.add(criteria.getCategoryId());
                spec = spec.and((root, query, cb) -> root.get("category").get("categoryId").in(categoryIds));
            } else {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("categoryId"), criteria.getCategoryId()));
            }
        }
        
        // Filter by price range
        if (criteria.getMinPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
        }
        if (criteria.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
        }
        
        // Filter by condition
        if (criteria.getConditions() != null && !criteria.getConditions().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("condition").in(criteria.getConditions()));
        }
        
        // Filter by location (distance)
        if (criteria.getLatitude() != null && criteria.getLongitude() != null && criteria.getDistance() != null) {
            // We need a native query for this, so we'll handle it separately
            List<Long> nearbyItemIds = itemRepository.findItemIdsNearLocation(
                criteria.getLongitude().doubleValue(),
                criteria.getLatitude().doubleValue(),
                criteria.getDistance()
            );
            if (!nearbyItemIds.isEmpty()) {
                spec = spec.and((root, query, cb) -> root.get("itemId").in(nearbyItemIds));
            } else {
                // If no items found nearby, return empty result
                return Page.empty(pageable);
            }
        }
        
        // Filter by tags
        if (criteria.getTags() != null && !criteria.getTags().isEmpty()) {
            // This is complex with JPA Specification, so we'll get tag item IDs first
            List<Long> taggedItemIds = itemRepository.findItemIdsByTagNames(criteria.getTags());
            if (!taggedItemIds.isEmpty()) {
                spec = spec.and((root, query, cb) -> root.get("itemId").in(taggedItemIds));
            } else {
                // If no items found with these tags, return empty result
                return Page.empty(pageable);
            }
        }
        
        // Execute the specification query
        return itemRepository.findAll(spec, pageable);
    }
    
    /**
     * Gets all subcategory IDs of a category (recursive).
     * 
     * @param categoryId the parent category ID
     * @return list of all subcategory IDs
     */
    private List<Long> getAllSubcategoryIds(Long categoryId) {
        List<Long> result = new ArrayList<>();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return result;
        }
        
        List<Category> children = categoryRepository.findByParentCategory(category);
        for (Category child : children) {
            result.add(child.getCategoryId());
            result.addAll(getAllSubcategoryIds(child.getCategoryId()));
        }
        
        return result;
    }
    
    /**
     * Gets popular items based on view count.
     * 
     * @param limit the maximum number of results
     * @return list of popular items
     */
    public List<Item> getPopularItems(int limit) {
        return itemRepository.findByStatusOrderByViewsCountDesc(ItemStatus.ACTIVE, limit);
    }
    
    /**
     * Gets recently listed items.
     * 
     * @param limit the maximum number of results
     * @return list of recently listed items
     */
    public List<Item> getRecentlyListedItems(int limit) {
        return itemRepository.findByStatusOrderByViewsCountDesc(ItemStatus.ACTIVE, limit);
    }
}