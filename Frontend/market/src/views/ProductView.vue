<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useItemStore } from '../stores/itemStore'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import { useAuthStore } from '@/stores/authStore'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { API_URL } from '../config'

const route = useRoute()
const itemStore = useItemStore()
const currentImageIndex = ref(0)
const messageText = ref('')
const isMessageModalOpen = ref(false)
const mapContainer = ref(null)
const loading = ref(true)
const error = ref(null)
let map = null
const authStore = useAuthStore()
const router = useRouter()

// Fix for Leaflet marker icons
const fixLeafletIcon = () => {
  // Fix for the default icon
  delete L.Icon.Default.prototype._getIconUrl
  L.Icon.Default.mergeOptions({
    iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
  })
}

// Get product images from the itemStore's currentItem
const productImages = computed(() => {
  if (!itemStore.currentItem || !itemStore.currentItem.images) return []
  return itemStore.currentItem.images.map(img => img.imageUrl)
})

// Fallback product data in case API call fails
const fallbackProduct = {
  title: 'Product Not Found',
  price: 0,
  description: 'This product could not be loaded.',
  images: ['https://placehold.co/600x400'],
  category: 'N/A',
  condition: 'N/A',
  location: {
    address: 'Unknown',
    coordinates: {
      lat: 59.9139,
      lng: 10.7522,
    },
  },
  seller: {
    username: 'Unknown',
    rating: 0,
    memberSince: 'N/A',
    totalSales: 0,
    responseTime: 'N/A',
  },
  specifications: [],
}

// Get formatted product data for display
const product = computed(() => {
  if (!itemStore.currentItem) return fallbackProduct
  
  return {
    id: itemStore.currentItem.itemId,
    title: itemStore.currentItem.title || 'Untitled Product',
    price: itemStore.currentItem.price || 0,
    description: itemStore.currentItem.fullDescription || itemStore.currentItem.briefDescription || 'No description available',
    images: productImages.value.length > 0 ? productImages.value : ['https://placehold.co/600x400'],
    category: itemStore.currentItem.category?.name || 'N/A',
    condition: itemStore.currentItem.condition || 'N/A',
    location: {
      address: itemStore.currentItem.location || 'Location not specified',
      coordinates: {
        // Default to Oslo coordinates if none provided
        lat: 59.9139,
        lng: 10.7522,
      },
    },
    seller: {
      id: itemStore.currentItem.seller?.id || null,
      username: itemStore.currentItem.seller?.username || 'Unknown seller',
      rating: itemStore.currentItem.seller?.rating || 0,
      memberSince: itemStore.currentItem.seller?.createdAt 
        ? new Date(itemStore.currentItem.seller.createdAt).getFullYear().toString()
        : 'N/A',
      totalSales: itemStore.currentItem.seller?.totalSales || 0,
      responseTime: 'Usually responds within 24 hours',
    },
    specifications: [
      { label: 'Condition', value: itemStore.currentItem.condition || 'N/A' },
      { label: 'Quantity', value: itemStore.currentItem.quantity || 1 },
      { label: 'Accepts Offers', value: itemStore.currentItem.allowOffers ? 'Yes' : 'No' },
      { label: 'Accepts Vipps', value: itemStore.currentItem.acceptVipps ? 'Yes' : 'No' },
    ],
  }
})

const nextImage = () => {
  if (!product.value.images || product.value.images.length <= 1) return
  currentImageIndex.value = (currentImageIndex.value + 1) % product.value.images.length
}

const previousImage = () => {
  if (!product.value.images || product.value.images.length <= 1) return
  currentImageIndex.value =
    currentImageIndex.value === 0 ? product.value.images.length - 1 : currentImageIndex.value - 1
}

const openMessageModal = () => {
  isMessageModalOpen.value = true
}

const closeMessageModal = () => {
  isMessageModalOpen.value = false
  messageText.value = ''
}

const sendMessage = async () => {
  if (!authStore.isAuthenticated || !authStore.currentUser) {
    console.log('Not authenticated, redirecting to login')
    router.push('/login?redirect=/product')
    return
  }

  // Validate token before sending message
  const isValid = await authStore.validateToken()
  if (!isValid) {
    console.log('Token is invalid, redirecting to login')
    router.push('/login?redirect=/product')
    return
  }

  // Validate IDs
  if (!product.value.seller?.id) {
    console.error('Seller ID is missing')
    error.value = 'Unable to send message: Seller information is incomplete'
    return
  }

  // Debug authentication state
  console.log('Auth state:', {
    isAuthenticated: authStore.isAuthenticated,
    currentUser: authStore.currentUser,
    token: authStore.token,
    tokenLength: authStore.token?.length,
    tokenParts: authStore.token?.split('.')
  })

  try {
    const headers = {
      'Authorization': `Bearer ${authStore.token}`,
      'Content-Type': 'application/json'
    }
    
    console.log('Request headers:', headers)
    
    const requestBody = {
      participantIds: [authStore.currentUser.id, product.value.seller.id],
      itemId: product.value.id,
      initialMessage: messageText.value
    }
    
    console.log('Request body:', requestBody)
    
    const response = await axios.post(`${API_URL}/conversations`, requestBody, { headers })
    console.log('Message sent successfully:', response.data)
    closeMessageModal()
  } catch (err) {
    console.error('Error sending message:', err)
    console.error('Error response:', err.response)
    if (err.response?.status === 401) {
      console.log('Token might be invalid, redirecting to login')
      // Try to validate token again to be sure
      const isValid = await authStore.validateToken()
      if (!isValid) {
        console.log('Token validation failed, logging out')
        await authStore.logout()
      }
      router.push('/login?redirect=/product')
    } else {
      error.value = err.response?.data?.message || 'Failed to send message'
    }
  }
}

// Fetch product data
const fetchProduct = async () => {
  loading.value = true
  error.value = null
  try {
    const productId = route.params.id
    if (!productId) {
      error.value = 'No product ID provided'
      return
    }
    
    await itemStore.getItem(productId)
    
    if (!itemStore.currentItem) {
      error.value = 'Product not found'
    } else {
      // Debug seller information
      console.log('Seller object:', itemStore.currentItem.seller)
      console.log('Seller ID:', itemStore.currentItem.seller?.id)
    }
  } catch (err) {
    console.error('Error fetching product:', err)
    if (err.response) {
      // Handle specific API error responses
      switch (err.response.status) {
        case 404:
          error.value = 'Product not found'
          break
        case 403:
          error.value = 'You do not have permission to view this product'
          break
        default:
          error.value = `Failed to load product details: ${err.response.data?.message || err.message}`
      }
    } else if (err.request) {
      // Handle network errors
      error.value = 'Network error. Please check your connection and try again.'
    } else {
      error.value = `An unexpected error occurred: ${err.message}`
    }
  } finally {
    loading.value = false
  }
}

// Initialize the map
const initMap = () => {
  if (!mapContainer.value) return
  
  try {
    // Fix Leaflet icon issue
    fixLeafletIcon()
    
    // Create the map instance
    map = L.map(mapContainer.value).setView(
      [product.value.location.coordinates.lat, product.value.location.coordinates.lng], 
      15
    )
    
    // Add the OpenStreetMap tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map)
    
    // Add a marker for the product location
    L.marker([product.value.location.coordinates.lat, product.value.location.coordinates.lng])
      .addTo(map)
      .bindPopup(product.value.location.address)
      .openPopup()
  } catch (err) {
    console.error('Error initializing map:', err)
  }
}

// Initialize map when component is mounted
onMounted(async () => {
  await fetchProduct()
  
  // Only initialize map if loading is complete and there's no error
  if (!loading.value && !error.value) {
    // Small delay to ensure the DOM is fully rendered
    setTimeout(() => {
      initMap()
    }, 100)
  }
})

// Clean up map when component is unmounted
onUnmounted(() => {
  if (map) {
    map.remove()
  }
})
</script>

<template>
  <div class="product-page">
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>Loading product details...</p>
    </div>
    
    <div v-else-if="error" class="error-container">
      <p>{{ error }}</p>
      <button @click="fetchProduct">Try Again</button>
    </div>
    
    <div v-else class="product-container">
      <!-- Image Gallery Section -->
      <div class="image-gallery">
        <div class="main-image">
          <img :src="product.images[currentImageIndex]" :alt="product.title" />
          <button v-if="product.images.length > 1" class="nav-button prev" @click="previousImage">❮</button>
          <button v-if="product.images.length > 1" class="nav-button next" @click="nextImage">❯</button>
        </div>
        <div v-if="product.images.length > 1" class="thumbnail-list">
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
        <div class="price">{{ product.price.toFixed(2) }} {{ itemStore.currentItem?.currency || 'NOK' }}</div>

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
          <div class="map-container" ref="mapContainer"></div>
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

.loading-container, .error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
}

.loading-spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top: 4px solid var(--accent-color);
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-container button {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: var(--accent-color);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
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
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  padding: 1rem 0.75rem;
  cursor: pointer;
  font-size: 1rem;
}

.nav-button.prev {
  left: 0;
  border-radius: 0 4px 4px 0;
}

.nav-button.next {
  right: 0;
  border-radius: 4px 0 0 4px;
}

.thumbnail-list {
  display: flex;
  gap: 0.5rem;
  overflow-x: auto;
  padding-bottom: 0.5rem;
}

.thumbnail {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.thumbnail.active {
  opacity: 1;
  border: 2px solid var(--accent-color);
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

.product-info h1 {
  margin: 0;
  font-size: 2rem;
  line-height: 1.2;
}

.price {
  font-size: 1.75rem;
  font-weight: bold;
  color: var(--accent-color);
}

.seller-info, .product-details, .location {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 1.5rem;
}

.seller-info h3, .product-details h3, .location h3 {
  margin-top: 0;
  margin-bottom: 1rem;
}

.seller-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.seller-name {
  font-weight: bold;
  font-size: 1.1rem;
}

.seller-stats {
  display: flex;
  gap: 1rem;
}

.rating {
  color: #ff9800;
}

.description {
  line-height: 1.6;
}

.specifications {
  margin-top: 1rem;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 0.75rem;
}

.spec-item {
  display: flex;
  align-items: baseline;
}

.spec-label {
  font-weight: bold;
  margin-right: 0.5rem;
}

.map-container {
  height: 300px;
  border-radius: 8px;
  margin-top: 1rem;
}

.actions {
  margin-top: 2rem;
}

.contact-seller {
  padding: 0.75rem 1.5rem;
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.contact-seller:hover {
  background-color: var(--button-hover-background);
}

/* Modal styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}

.modal-content h2 {
  margin-top: 0;
}

.product-reference {
  color: #666;
  margin-bottom: 1rem;
}

textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.cancel-button, .send-button {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-button {
  background: none;
  border: 1px solid #ddd;
}

.send-button {
  background-color: var(--accent-color);
  color: white;
  border: none;
}

.send-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .product-container {
    grid-template-columns: 1fr;
  }
}
</style>
