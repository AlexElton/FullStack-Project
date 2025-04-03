<script setup>
import ProductCard from '@/components/ProductCard.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Sample data - in a real app, this would come from an API
const featuredProducts = [
  {
    id: 1,
    title: 'Premium Headphones',
    price: 199.99,
    image: 'https://placehold.co/600x400',
    description: 'High-quality wireless headphones with noise cancellation',
  },
  {
    id: 2,
    title: 'Smart Watch',
    price: 299.99,
    image: 'https://placehold.co/600x400',
    description: 'Feature-rich smartwatch with health tracking',
  },
  {
    id: 3,
    title: 'Wireless Speaker',
    price: 149.99,
    image: 'https://placehold.co/600x400',
    description: 'Portable Bluetooth speaker with premium sound',
  },
]

const navigateToProducts = () => {
  router.push('/products')
}

const navigateToSell = () => {
  router.push('/sell')
}
</script>

<template>
  <main class="home">
    <section class="hero">
      <div class="hero-content">
        <h1>Welcome to HaggleHub</h1>
        <p>Discover amazing products at great prices</p>
        <button class="cta-button" @click="navigateToProducts">Shop Now</button>
        <button class="cta-button" @click="navigateToSell">Sell Now</button>
      </div>
    </section>

    <section class="featured-products">
      <h2>Featured Products</h2>
      <div class="products-grid">
        <ProductCard
          v-for="product in featuredProducts"
          :key="product.id"
          :title="product.title"
          :price="product.price"
          :image="product.image"
          :description="product.description"
          :id="product.id"
        />
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
  padding: 4rem 2rem;
  text-align: center;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: var(--button-text-color);
}

.hero p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  color: var(--button-text-color);
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
