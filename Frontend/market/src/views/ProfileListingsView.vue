<script setup>
import { ref, onMounted } from 'vue'
import { useItemStore } from '@/stores/itemStore'
import { useAuthStore } from '@/stores/authStore'
import { useRouter } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'

const itemStore = useItemStore()
const authStore = useAuthStore()
const router = useRouter()
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  if (!authStore.isAuthenticated || !authStore.currentUser) {
    router.push('/login?redirect=/profile/listings')
    return
  }

  try {
    await itemStore.getItemsBySeller(authStore.currentUser.id)
    loading.value = false
  } catch (err) {
    console.error('Error fetching listings:', err)
    error.value = err.response?.data?.message || 'Failed to load your listings'
    loading.value = false
  }
})

const fetchListings = async () => {
  if (!authStore.isAuthenticated || !authStore.currentUser) {
    router.push('/login?redirect=/profile/listings')
    return
  }
  
  try {
    await itemStore.getItemsBySeller(authStore.currentUser.id)
  } catch (err) {
    console.error('Error fetching listings:', err)
    error.value = err.response?.data?.message || 'Failed to load your listings'
  }
}
</script>

<template>
  <div class="profile-listings">
    <h1>My Listings</h1>
    
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>Loading your listings...</p>
    </div>
    
    <div v-else-if="error" class="error-container">
      <p>{{ error }}</p>
      <button @click="fetchListings">Try Again</button>
    </div>
    
    <div v-else-if="itemStore.items.length === 0" class="empty-state">
      <p>You haven't listed any items yet.</p>
      <router-link to="/sell" class="cta-button">List an Item</router-link>
    </div>
    
    <div v-else class="listings-grid">
      <ProductCard
        v-for="item in itemStore.items"
        :key="item.itemId"
        :product="item"
      />
    </div>
  </div>
</template>

<style scoped>
.profile-listings {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.profile-listings h1 {
  margin-bottom: 2rem;
  color: var(--text-color);
}

.loading-container,
.error-container,
.empty-state {
  text-align: center;
  padding: 2rem;
}

.loading-spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-left-color: var(--button-background);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-container button {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.empty-state {
  background-color: var(--card-background);
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.empty-state p {
  margin-bottom: 1rem;
  color: var(--text-secondary);
}

.cta-button {
  display: inline-block;
  padding: 0.75rem 1.5rem;
  background-color: var(--button-background);
  color: var(--button-text-color);
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.cta-button:hover {
  background-color: var(--button-hover-background);
}

.listings-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
}
</style> 