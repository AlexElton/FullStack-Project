<script setup>
import ProductCard from '@/components/ProductCard.vue'

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
    id: 4,
    title: 'Goblin Slayer: Volume 1',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'First volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
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
    id: 4,
    title: 'Goblin Slayer: Volume 1',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'First volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
  },
  {
    id: 4,
    title: 'Goblin Slayer: Volume 1',
    price: 19.99,
    image: 'https://placehold.co/600x400',
    description: 'First volume of the popular manga series "Goblin Slayer"',
    category: 'Books',
    seller: 'BookStore',
  }
]

// Placeholder for API integration
const fetchProducts = () => {
  // TODO: Implement API call to fetch products
  console.log('Fetching products from API...')
}

const categories = ['All', 'Electronics', 'Clothing', 'Books', 'Home & Garden']
</script>

<template>
  <div class="products-page">
    <div class="filters">
      <div class="search-bar">
        <input type="text" placeholder="Search products..." />
        <button>Search</button>
      </div>

      <div class="filter-options">
        <select>
          <option v-for="category in categories" :key="category" :value="category">
            {{ category }}
          </option>
        </select>

        <select>
          <option value="price-asc">Price: Low to High</option>
          <option value="price-desc">Price: High to Low</option>
          <option value="newest">Newest First</option>
        </select>
      </div>
    </div>

    <div class="products-grid">
      <ProductCard
        v-for="product in products"
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
      <button class="sell-button">Start Selling</button>
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
