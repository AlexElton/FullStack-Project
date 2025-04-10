<script setup>
import { ref } from 'vue'
import ProductCard from '@/components/ProductCard.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Hero images
const heroImages = [
  'https://res.cloudinary.com/dvxtp0hrq/image/upload/v1743697402/cld-sample-4.jpg',
  'https://res.cloudinary.com/dvxtp0hrq/image/upload/v1743697402/cld-sample-2.jpg',
  'https://res.cloudinary.com/dvxtp0hrq/image/upload/v1743697402/cld-sample-3.jpg',
]
const heroImage = heroImages[Math.floor(Math.random() * heroImages.length)]

// Simulated API data (you can replace this with a real API later)
const allFeaturedProducts = [
  // Imagine this is coming from an API, sorted by created_at DESC
  { id: 1, title: 'Premium Headphones', price: 199.99, image: 'https://placehold.co/600x400', description: 'High-quality wireless headphones' },
  { id: 2, title: 'Smart Watch', price: 299.99, image: 'https://placehold.co/600x400', description: 'Smartwatch with health tracking' },
  { id: 3, title: 'Wireless Speaker', price: 149.99, image: 'https://placehold.co/600x400', description: 'Bluetooth speaker with premium sound' },
  { id: 4, title: 'Tablet Pro', price: 499.99, image: 'https://placehold.co/600x400', description: 'High-performance tablet' },
  { id: 5, title: 'Gaming Mouse', price: 89.99, image: 'https://placehold.co/600x400', description: 'Precision gaming mouse' },
  { id: 6, title: 'Laptop Stand', price: 39.99, image: 'https://placehold.co/600x400', description: 'Ergonomic stand for laptops' },
  { id: 7, title: 'Smart Lamp', price: 59.99, image: 'https://placehold.co/600x400', description: 'Voice controlled smart lamp' },
  { id: 8, title: 'Noise Cancelling Earbuds', price: 129.99, image: 'https://placehold.co/600x400', description: 'Compact earbuds with ANC' },
  { id: 9, title: '4K Monitor', price: 299.99, image: 'https://placehold.co/600x400', description: 'Crystal-clear display for work & play' },
  { id: 10, title: 'Portable SSD', price: 109.99, image: 'https://placehold.co/600x400', description: 'Fast and compact external drive' },
]

const limit = 4
const offset = ref(0)
const paginatedProducts = ref([])

const loadMore = () => {
  const next = allFeaturedProducts.slice(offset.value, offset.value + limit)
  paginatedProducts.value.push(...next)
  offset.value += limit
}

loadMore() // Load initial batch

const navigateToProducts = () => router.push('/products')
const navigateToSell = () => router.push('/sell')
</script>


<template>
  <main class="home">
    <section class="hero" :style="{ backgroundImage: `url(${heroImage})` }">
      <div class="hero-content">
        <h1>Welcome to ShopZilla</h1>
        <p>Discover amazing products at great prices</p>
        <div class="buttons">
          <button class="cta-button" @click="navigateToProducts">Shop Now</button>
          <button class="cta-button" @click="navigateToSell">Sell Now</button>
        </div>
      </div>
    </section>

    <section class="featured-products">
      <h2>Featured Products</h2>
      <div class="products-grid">
        <ProductCard
          v-for="product in paginatedProducts"
          :key="product.id"
          :title="product.title"
          :price="product.price"
          :image="product.image"
          :description="product.description"
          :id="product.id"
        />
      </div>
      <div style="text-align: center; margin-top: 2rem;">
        <button
          class="cta-button"
          v-if="offset < allFeaturedProducts.length"
          @click="loadMore"
        >
          Load More
        </button>
      </div>
    </section>
  </main>
</template>


<style scoped>
.home {
  min-height: 100vh;
}

.hero {
  background: var(--primary-color);
  color: var(--button-text-color);
  padding: 2rem 1rem;
  text-align: center;
  background-size: cover;
  background-position: center;
  height: 60vh;
  justify-content: center;
}

.hero-content {
  max-width: 800px;
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  text-align: center;
  background: rgba(0, 0, 0, 0.5);
  padding: 1rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: transform 0.3s;
  transform: scale(1);
}

.hero h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: var(--hero-text-color);
}

.hero p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  color: var(--hero-text-color);
}

.cta-button {
  background-color: var(--button-background);
  color: var(--button-text-color);
  font-weight: bold;
  border: none;
  padding: 0.8rem 1.5rem;
  margin: 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.cta-button:hover {
  background-color: var(--button-hover-background);
}

.featured-products {
  padding: 4rem 2rem;
}

.featured-products h2 {
  color: var(--text-color);
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2rem;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
</style>
