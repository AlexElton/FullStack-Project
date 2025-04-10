package ntnu.idi.bidata.IDATT2105.dtos;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for search results.
 */
public class SearchResultDTO {

    private List<ItemDTO> items;
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private Map<String, Long> facets; // For faceted search (e.g., count by category, condition)
    
    // Default constructor
    public SearchResultDTO() {
    }
    
    // Constructor with fields
    public SearchResultDTO(List<ItemDTO> items, long totalItems, int totalPages, int currentPage) {
        this.items = items;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    // Getters and setters
    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Map<String, Long> getFacets() {
        return facets;
    }

    public void setFacets(Map<String, Long> facets) {
        this.facets = facets;
    }
}