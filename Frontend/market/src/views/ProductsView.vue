<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useSearchStore } from '../stores/searchStore'
import ProductCard from '@/components/ProductCard.vue'

const route = useRoute()
const searchStore = useSearchStore()

const searchQuery = ref('')
const selectedCategory = ref('All')

// Sample data - will be replaced with API calls
const products = [
  {
    id: 1,
    title: 'Premium Headphones',
    price: 199.99,
    image: 'https://placehold.co/600x400',
    description: 'High-quality wireless headphones with noise cancellation',
    category: 'Electronics',
    seller: 'TechStore',
  },
  {
    id: 2,
    title: 'Smart Watch',
    price: 299.99,
    image: 'https://placehold.co/600x400',
    description: 'Feature-rich smartwatch with health tracking',
    category: 'Electronics',
    seller: 'TechStore',
  },
  {
    id: 3,
    title: 'Wireless Speaker',
    price: 149.99,
    image: 'https://placehold.co/600x400',
    description: 'Portable Bluetooth speaker with premium sound',
    category: 'Electronics',
    seller: 'TechStore',
  },
  {
    id: 4,
    title: 'Goblin Slayer: Volume 1',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'First volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
  },
  {
    id: 5,
    title: 'Goblin Slayer: Volume 2',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'Second volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
  },
  {
    id: 6,
    title: 'Goblin Slayer: Volume 3',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'Third volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
  },
  {
    id: 7,
    title: 'Goblin Slayer: Volume 4',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'Fourth volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
  },
  {
    id: 8,
    title: 'Goblin Slayer: Volume 5',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'Fifth volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
  }
]

// State for filters and search
const selectedSort = ref('newest')

// Computed property to filter and sort products
const filteredProducts = computed(() => {
  // Step 1: Apply search filter if there's a search query
  let result = [...products]
  
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase().trim()
    result = result.filter(product => 
      product.title.toLowerCase().includes(query) || 
      product.description.toLowerCase().includes(query) ||
      product.category.toLowerCase().includes(query)
    )
  }
  
  // Step 2: Filter by category
  if (selectedCategory.value !== 'All') {
    result = result.filter(product => product.category === selectedCategory.value)
  }
  
  // Step 3: Sort based on selected option
  switch (selectedSort.value) {
    case 'price-asc':
      return result.sort((a, b) => a.price - b.price)
    case 'price-desc':
      return result.sort((a, b) => b.price - a.price)
    case 'newest':
    default:
      // Assuming id represents the order of addition (newer items have higher ids)
      return result.sort((a, b) => b.id - a.id)
  }
})

const categories = ['All', 'Electronics', 'Fashion', 'Home & Garden', 'Toys & Games', 
  'Vehicles', 'Books', 'Music & Movies', 'Sports & Outdoors', 'Health & Beauty',
  'Groceries', 'Office Supplies', 'Pet Supplies', 'Jewelry & Accessories',
  'Tools & Industrial', 'Art & Collectibles']

// Initialize search and category from store or route
onMounted(() => {
  console.log('ProductsView mounted')
  console.log('Route query:', route.query)
  console.log('Store state:', {
    lastSearch: searchStore.lastSearch,
    lastCategory: searchStore.lastCategory
  })

  // Handle category from route or store
  if (route.query.category) {
    console.log('Setting category from route:', route.query.category)
    selectedCategory.value = route.query.category
    searchStore.setCategory(route.query.category)
  } else if (searchStore.lastCategory) {
    console.log('Setting category from store:', searchStore.lastCategory)
    selectedCategory.value = searchStore.lastCategory
  }
  
  // Handle search from store
  if (searchStore.lastSearch) {
    console.log('Setting search from store:', searchStore.lastSearch)
    searchQuery.value = searchStore.lastSearch
  }
})

// Watch for search changes
watch(searchQuery, (newValue) => {
  console.log('Search query changed:', newValue)
  searchStore.setSearch(newValue)
}, { immediate: true })

// Watch for category changes
watch(selectedCategory, (newValue) => {
  console.log('Category changed:', newValue)
  if (newValue !== 'All') {
    searchStore.setCategory(newValue)
  } else {
    searchStore.setCategory('')
  }
}, { immediate: true })


// Placeholder for API integration
const fetchProducts = () => {
  // TODO: Implement API call to fetch products
  console.log('Fetching products from API...')
}

// Handle category change
const handleCategoryChange = (event) => {
  selectedCategory.value = event.target.value
}

// Handle sort change
const handleSortChange = (event) => {
  selectedSort.value = event.target.value
}

// Handle search
const handleSearch = () => {
  // The search is already reactive with the v-model binding
  // But this function can be used for additional actions like tracking search history
  console.log('Searching for:', searchQuery.value)
}

// Handle Enter key press in search input
const handleSearchKeyup = (event) => {
  if (event.key === 'Enter') {
    handleSearch()
  }
}
</script>

<template>
  <div class="products-page">
    <div class="filters">
      <div class="search-bar">
        <input 
          type="text" 
          placeholder="Search products..." 
          v-model="searchQuery"
          @keyup="handleSearchKeyup"
        />
        <button @click="handleSearch">Search</button>
      </div>

      <div class="filter-options">
        <select @change="handleCategoryChange" v-model="selectedCategory">
          <option v-for="category in categories" :key="category" :value="category">
            {{ category }}
          </option>
        </select>

        <select @change="handleSortChange" v-model="selectedSort">
          <option value="price-asc">Price: Low to High</option>
          <option value="price-desc">Price: High to Low</option>
          <option value="newest">Newest First</option>
        </select>
      </div>
    </div>

    <div v-if="filteredProducts.length === 0" class="no-results">
      <p>No products found. Try a different search or filter.</p>
    </div>

    <div v-else class="products-grid">
      <ProductCard
        v-for="product in filteredProducts"
        :key="product.id"
        :title="product.title"
        :price="product.price"
        :image="product.image"
        :description="product.description"
      />
    </div>

    <div class="sell-section">
      <h2>Want to sell something?</h2>
      <p>Create an account and start selling your items today!</p>
      <router-link to="/sell" class="sell-button">Start Selling</router-link>
    </div>
  </div>
</template>

<style scoped>
.products-page {
  max-width: 100%;
  margin: 0 auto;
  padding: 1rem;
}

.filters {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 50%;
  margin: 2rem auto;
}

.search-bar {
  display: flex;
  gap: 0.5rem;
}

.search-bar input {
  flex: 1;
  padding: 0.8rem;
  box-shadow: 0 2px 4px rgba(123, 164, 102, 0.1);
  border-radius: 4px;
  background-color: var(--background-color);
  color: var(--text-color);
}

.search-bar input::placeholder {
  color: var(--accent-color);
}

.search-bar button {
  padding: 0.8rem 1.5rem;
  background-color: var(--button-background);
  color: var(--button-text-color);
  box-shadow: 0 2px 4px rgba(123, 164, 102, 0.1);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-bar button:hover {
  background-color: var(--button-hover-background);
}

.filter-options {
  display: flex;
  gap: 1rem;
}

.filter-options select {
  padding: 0.8rem;
  box-shadow: 0 2px 4px rgba(123, 164, 102, 0.1);
  border-radius: 4px;
  background-color: var(--background-color);
  color: var(--text-color);
  cursor: pointer;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.no-results {
  text-align: center;
  padding: 3rem;
  background-color: var(--card-background);
  border-radius: 8px;
  margin-bottom: 3rem;
}

.no-results p {
  color: var(--text-color);
  font-size: 1.2rem;
}

.sell-section {
  text-align: center;
  padding: 3rem;
  background-color: var(--card-background);
  border-radius: 8px;
  margin-top: 2rem;
}

.sell-section h2 {
  color: var(--text-color);
  margin-bottom: 1rem;
  font-size: 1.8rem;
}

.sell-section p {
  color: var(--text-color);
  margin-bottom: 1.5rem;
}

.sell-button {
  padding: 1rem 2rem;
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s;
}

.sell-button:hover {
  background-color: var(--button-hover-background);
}
</style>