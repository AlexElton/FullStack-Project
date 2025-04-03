<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  price: {
    type: Number,
    required: true,
  },
  image: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    default: '',
  },
  id: {
    type: Number,
    required: true,
  },
})

const navigateToProduct = () => {
  router.push(`/products/${props.id}`)
}
</script>

<template>
  <div class="product-card">
    <img :src="image" :alt="title" class="product-image" />
    <div class="product-info">
      <h3 class="product-title">{{ title }}</h3>
      <p class="product-description">{{ description }}</p>
      <div class="product-price">${{ price.toFixed(2) }}</div>
      <button class="more-details" @click="navigateToProduct">More details</button>
    </div>
  </div>
</template>

<style scoped>
.product-card {
  border: 1px solid var(--accent-color);
  border-radius: 8px;
  overflow: hidden;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  background: var(--card-background);
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 1rem;
}

.product-title {
  margin: 0;
  font-size: 1.1rem;
  color: var(--text-color);
}

.product-description {
  margin: 0.5rem 0;
  font-size: 0.9rem;
  color: var(--text-color);
}

.product-price {
  font-size: 1.2rem;
  font-weight: bold;
  color: var(--accent-color);
  margin: 0.5rem 0;
}

.more-details {
  width: 100%;
  padding: 0.5rem;
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.more-details:hover {
  background-color: var(--button-hover-background);
}
</style>
