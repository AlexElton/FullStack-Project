<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useItemStore } from '@/stores/itemStore'
import { useCategoryStore } from '@/stores/categoryStore'
import ProductCard from '@/components/ProductCard.vue'

const route = useRoute()
const itemStore = useItemStore()
const categoryStore = useCategoryStore()

const searchQuery = ref('')
const selectedCategory = ref('All')
const selectedSort = ref('newest')
const loading = ref(false)
const error = ref(null)

// Fetch products from API
const fetchProducts = async () => {
  loading.value = true
  error.value = null
  try {
    await itemStore.getActiveItems()
  } catch (err) {
    error.value = err.message
    console.error('Error fetching products:', err)
  } finally {
    loading.value = false
  }
}

// Fetch categories from API
const fetchCategories = async () => {
  try {
    await categoryStore.getAllCategories()
  } catch (err) {
    console.error('Error fetching categories:', err)
  }
}

// Computed property to filter and sort products
const filteredProducts = computed(() => {
  if (!itemStore.items) return []
  
  let result = [...itemStore.items]
  
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase().trim()
    result = result.filter(product => 
      product.title.toLowerCase().includes(query) || 
      product.fullDescription.toLowerCase().includes(query) ||
      product.category.name.toLowerCase().includes(query)
    )
  }
  
  if (selectedCategory.value !== 'All') {
    result = result.filter(product => product.category.name === selectedCategory.value)
  }
  
  switch (selectedSort.value) {
    case 'price-asc':
      return result.sort((a, b) => a.price - b.price)
    case 'price-desc':
      return result.sort((a, b) => b.price - a.price)
    case 'newest':
    default:
      return result.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  }
})

// Update the search functionality to use the search endpoint
const handleSearch = async () => {
  if (!searchQuery.value.trim()) {
    await fetchProducts()
    return
  }
  
  loading.value = true
  error.value = null
  try {
    await itemStore.searchItems(searchQuery.value)
  } catch (err) {
    error.value = err.message
    console.error('Error searching products:', err)
  } finally {
    loading.value = false
  }
}

// Update category filtering to use the category endpoint
const handleCategoryChange = async (event) => {
  selectedCategory.value = event.target.value
  
  if (selectedCategory.value === 'All') {
    await fetchProducts()
    return
  }
  
  loading.value = true
  error.value = null
  try {
    const category = categoryStore.categories.find(c => c.name === selectedCategory.value)
    if (category) {
      await itemStore.getItemsByCategory(category.categoryId)
    }
  } catch (err) {
    error.value = err.message
    console.error('Error fetching category products:', err)
  } finally {
    loading.value = false
  }
}

// Handle sort change
const handleSortChange = (event) => {
  selectedSort.value = event.target.value
}

// Watch for route changes to handle search parameters
watch(() => route.query, async (newQuery) => {
  if (newQuery.search) {
    searchQuery.value = newQuery.search
    await handleSearch()
  }
}, { immediate: true })

// Fetch products and categories on mount
onMounted(async () => {
  await Promise.all([fetchProducts(), fetchCategories()])
})
</script>

<template>
  <div class="products-page">
    <div class="filters">
      <div class="search-bar">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search products..."
          @keyup.enter="handleSearch"
        />
        <button @click="handleSearch">Search</button>
      </div>

      <div class="filter-controls">
        <select v-model="selectedCategory" @change="handleCategoryChange">
          <option value="All">All Categories</option>
          <option v-for="category in categoryStore.categories" 
                  :key="category.categoryId" 
                  :value="category.name">
            {{ category.name }}
          </option>
        </select>

        <select v-model="selectedSort" @change="handleSortChange">
          <option value="newest">Newest First</option>
          <option value="price-asc">Price: Low to High</option>
          <option value="price-desc">Price: High to Low</option>
        </select>
      </div>
    </div>

    <div v-if="error" class="error-message">
      {{ error }}
    </div>

    <div v-if="loading" class="loading-state">
      Loading products...
    </div>

    <div v-else-if="filteredProducts.length === 0" class="no-results">
      No products found.
    </div>

    <div v-else class="products-grid">
      <ProductCard
        v-for="product in filteredProducts"
        :key="product.itemId"
        :product="product"
      />
    </div>
  </div>
</template>

<style scoped>
.products-page {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.filters {
  margin-bottom: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.search-bar {
  display: flex;
  gap: 1rem;
}

.search-bar input {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
}

.search-bar button {
  padding: 0.5rem 1rem;
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.filter-controls {
  display: flex;
  gap: 1rem;
}

.filter-controls select {
  padding: 0.5rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  background-color: var(--background-color);
  color: var(--text-color);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
}

.error-message {
  background-color: var(--error-background);
  color: var(--error-text);
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 4px;
}

.loading-state {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}

.no-results {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}
</style>