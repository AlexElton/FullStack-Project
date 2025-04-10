<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const currentImageIndex = ref(0)
const messageText = ref('')
const isMessageModalOpen = ref(false)

// Sample data - will be replaced with API calls
const product = {
  id: 1,
  title: 'Premium Headphones',
  price: 199.99,
  description:
    'High-quality wireless headphones with noise cancellation. Perfect for music enthusiasts and professionals. Features include 30-hour battery life, active noise cancellation, and premium sound quality.',
  images: [
    'https://placehold.co/600x400',
    'https://placehold.co/500x400',
    'https://placehold.co/600x400',
    'https://placehold.co/600x400',
  ],
  category: 'Electronics',
  condition: 'Like New',
  location: {
    address: '123 Market Street, Oslo, Norway',
    coordinates: {
      lat: 59.9139,
      lng: 10.7522,
    },
  },
  seller: {
    username: 'TechEnthusiast',
    rating: 4.8,
    memberSince: '2023',
    totalSales: 45,
    responseTime: 'Usually responds within 24 hours',
  },
  specifications: [
    { label: 'Brand', value: 'Premium Audio' },
    { label: 'Model', value: 'PA-1000' },
    { label: 'Color', value: 'Black' },
    { label: 'Warranty', value: '1 Year' },
  ],
}

const nextImage = () => {
  currentImageIndex.value = (currentImageIndex.value + 1) % product.images.length
}

const previousImage = () => {
  currentImageIndex.value =
    currentImageIndex.value === 0 ? product.images.length - 1 : currentImageIndex.value - 1
}

const openMessageModal = () => {
  isMessageModalOpen.value = true
}

const closeMessageModal = () => {
  isMessageModalOpen.value = false
  messageText.value = ''
}

const sendMessage = () => {
  // TODO: Implement message sending functionality
  console.log('Sending message to seller:', {
    seller: product.seller.username,
    message: messageText.value,
    productId: product.id,
  })
  closeMessageModal()
}
</script>

<template>
  <div class="product-page">
    <div class="product-container">
      <!-- Image Gallery Section -->
      <div class="image-gallery">
        <div class="main-image">
          <img :src="product.images[currentImageIndex]" :alt="product.title" />
          <button class="nav-button prev" @click="previousImage">❮</button>
          <button class="nav-button next" @click="nextImage">❯</button>
        </div>
        <div class="thumbnail-list">
          <div
            v-for="(image, index) in product.images"
            :key="index"
            class="thumbnail"
            :class="{ active: currentImageIndex === index }"
            @click="currentImageIndex = index"
          >
            <img :src="image" :alt="`${product.title} - Image ${index + 1}`" />
          </div>
        </div>
      </div>

      <!-- Product Information Section -->
      <div class="product-info">
        <h1>{{ product.title }}</h1>
        <div class="price">${{ product.price.toFixed(2) }}</div>

        <div class="seller-info">
          <h3>Seller Information</h3>
          <div class="seller-details">
            <div class="seller-name">{{ product.seller.username }}</div>
            <div class="seller-stats">
              <span class="rating">★ {{ product.seller.rating }}</span>
              <span class="sales">{{ product.seller.totalSales }} sales</span>
            </div>
            <div class="response-time">{{ product.seller.responseTime }}</div>
          </div>
        </div>

        <div class="product-details">
          <h3>Product Details</h3>
          <p class="description">{{ product.description }}</p>

          <div class="specifications">
            <div v-for="spec in product.specifications" :key="spec.label" class="spec-item">
              <span class="spec-label">{{ spec.label }}:</span>
              <span class="spec-value">{{ spec.value }}</span>
            </div>
          </div>
        </div>

        <div class="location">
          <h3>Location</h3>
          <p>{{ product.location.address }}</p>
          <div class="map-container">
            <!-- TODO: Implement actual map integration -->
            <div class="map-placeholder">
              Map showing location at {{ product.location.coordinates.lat }},
              {{ product.location.coordinates.lng }}
            </div>
          </div>
        </div>

        <div class="actions">
          <button class="contact-seller" @click="openMessageModal">Message Seller</button>
        </div>
      </div>
    </div>

    <!-- Message Modal -->
    <div v-if="isMessageModalOpen" class="modal-overlay" @click="closeMessageModal">
      <div class="modal-content" @click.stop>
        <h2>Message {{ product.seller.username }}</h2>
        <p class="product-reference">Re: {{ product.title }}</p>
        <textarea
          v-model="messageText"
          placeholder="Write your message here..."
          rows="4"
        ></textarea>
        <div class="modal-actions">
          <button class="cancel-button" @click="closeMessageModal">Cancel</button>
          <button class="send-button" @click="sendMessage" :disabled="!messageText.trim()">
            Send Message
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.product-page {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.product-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.image-gallery {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.main-image {
  position: relative;
  aspect-ratio: 4/3;
  border-radius: 8px;
  overflow: hidden;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.6);
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  transition: all 0.2s;
  color: #ffffff;
}

.nav-button:hover {
  background: rgba(0, 0, 0, 0.8);
  color: var(--primary-color);
}

.nav-button.prev {
  left: 1rem;
}

.nav-button.next {
  right: 1rem;
}

.thumbnail-list {
  display: flex;
  gap: 0.5rem;
  overflow-x: auto;
  padding: 0.5rem 0;
}

.thumbnail {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border-color 0.2s;
}

.thumbnail.active {
  border-color: var(--primary-color);
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

h1 {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.price {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--button-background);
}

.seller-info,
.product-details,
.location {
  background: #ffffff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h3 {
  color: #000000;
  margin-bottom: 1rem;
}

.seller-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.seller-name {
  font-weight: 600;
  color: #000000;
}

.seller-stats {
  display: flex;
  gap: 1rem;
  color: #666;
}

.rating {
  color: var(--primary-color);
  font-weight: 600;
}

.response-time {
  color: var(--primary-color);
  font-size: 0.9rem;
}

.description {
  color: #666;
  line-height: 1.6;
  margin-bottom: 1rem;
}

.specifications {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

.spec-item {
  display: flex;
  gap: 0.5rem;
}

.spec-label {
  font-weight: 500;
  color: #000000;
}

.spec-value {
  color: #666;
}

.map-container {
  height: 200px;
  background: #f8f8f8;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1rem;
  border: 1px solid #e0e0e0;
}

.map-placeholder {
  color: #666;
  text-align: center;
}

.actions {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.contact-seller {
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
  background-color: var(--primary-color);
  color: #000000;
}

.contact-seller:hover {
  background-color: #e88a1a;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.modal-content h2 {
  color: #000000;
  margin-bottom: 0.5rem;
}

.product-reference {
  color: #666;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.modal-content textarea {
  width: 100%;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 1rem;
  resize: vertical;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.cancel-button,
.send-button {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

.cancel-button {
  background-color: #f8f8f8;
  color: #666;
  border: 1px solid #ddd;
}

.cancel-button:hover {
  background-color: #f0f0f0;
}

.send-button {
  background-color: var(--primary-color);
  color: #000000;
  border: none;
}

.send-button:hover:not(:disabled) {
  background-color: var(--button-background);
}

.send-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

@media (max-width: 1024px) {
  .product-container {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .product-page {
    padding: 1rem;
  }

  .specifications {
    grid-template-columns: 1fr;
  }

  .actions {
    flex-direction: column;
  }
}
</style>
