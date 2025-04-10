<script setup>
import { useRouter } from 'vue-router'
import { computed } from 'vue'

const router = useRouter()

const props = defineProps({
  // Support for product object (new way)
  product: {
    type: Object,
    default: null,
  },
  // Support for individual props (old way)
  title: {
    type: String,
    default: '',
  },
  price: {
    type: Number,
    default: 0,
  },
  image: {
    type: String,
    default: '',
  },
  description: {
    type: String,
    default: '',
  },
  id: {
    type: Number,
    default: null,
  },
})

// Determine if we're using the product object or individual props
const isUsingProductObject = computed(() => !!props.product)

// Handle navigation for both prop types
const navigateToProduct = () => {
  const productId = isUsingProductObject.value ? props.product.itemId : props.id
  router.push(`/products/${productId}`)
}

// Handle image for both prop types
const getProductImage = () => {
  if (isUsingProductObject.value) {
    if (props.product.images && props.product.images.length > 0) {
      const primaryImage = props.product.images.find(img => img.isPrimary === true)
      if (primaryImage) return primaryImage.imageUrl
      return props.product.images[0].imageUrl
    }
    return 'https://placehold.co/600x400'
  }
  return props.image || 'https://placehold.co/600x400'
}

// Get title for both prop types
const getTitle = computed(() => {
  return isUsingProductObject.value ? props.product.title : props.title
})

// Get description for both prop types
const getDescription = computed(() => {
  return isUsingProductObject.value ? props.product.briefDescription : props.description
})

// Get price for both prop types
const getPrice = computed(() => {
  if (isUsingProductObject.value) {
    const price = props.product.price
    return price ? price.toFixed(2) : '0.00'
  }
  return props.price ? props.price.toFixed(2) : '0.00'
})

// Get currency for both prop types
const getCurrency = computed(() => {
  return isUsingProductObject.value && props.product.currency ? props.product.currency : '$'
})
</script>

<template>
  <div class="product-card">
    <img :src="getProductImage()" :alt="getTitle" class="product-image" />
    <div class="product-info">
      <h3 class="product-title">{{ getTitle }}</h3>
      <p class="product-description">{{ getDescription }}</p>
      <div class="product-price">{{ getPrice }} {{ getCurrency }}</div>
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
