<template>
  <div class="category-view">
    <h1 class="text-3xl font-bold mb-8 text-center">Browse Categories</h1>
    <div class="category-grid">
      <router-link
        v-for="category in categories"
        :key="category.name"
        :to="{ name: 'products', query: { category: category.name }}"
        class="category-button"
        @click="selectCategory(category.name)"
      >
        <div class="category-content">
          <div class="icon-wrapper">
            <font-awesome-icon :icon="category.icon" class="category-icon" />
          </div>
          <h2 class="category-title">{{ category.name }}</h2>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { useSearchStore } from '../stores/searchStore'

const searchStore = useSearchStore()

const categories = [
  { name: 'Electronics', icon: 'laptop' },
  { name: 'Fashion', icon: 'tshirt' },
  { name: 'Home & Garden', icon: 'home' },
  { name: 'Toys & Games', icon: 'gamepad' },
  { name: 'Vehicles', icon: 'car' },
  { name: 'Books', icon: 'book' },
  { name: 'Music & Movies', icon: 'film' },
  { name: 'Sports & Outdoors', icon: 'basketball-ball' },
  { name: 'Health & Beauty', icon: 'spa' },
  { name: 'Groceries', icon: 'shopping-basket' },
  { name: 'Office Supplies', icon: 'pencil-alt' },
  { name: 'Pet Supplies', icon: 'paw' },
  { name: 'Jewelry & Accessories', icon: 'gem' },
  { name: 'Tools & Industrial', icon: 'tools' },
  { name: 'Art & Collectibles', icon: 'palette' }
]

const selectCategory = (category) => {
  searchStore.setCategory(category)
}
</script>

<style scoped>
.category-view {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.category-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  justify-content: center;
  padding: 1rem;
}

.category-button {
  flex: 1 1 200px;
  max-width: 250px;
  min-width: 180px;
  background: linear-gradient(135deg, #10B981 0%, #059669 100%);
  border-radius: 16px;
  padding: 1.5rem;
  text-align: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-decoration: none;
  color: white;
  border: none;
  position: relative;
  overflow: hidden;
}

.category-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.category-button:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 8px 20px rgba(16, 185, 129, 0.3);
}

.category-button:hover::before {
  opacity: 1;
}

.category-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  position: relative;
  z-index: 1;
}

.icon-wrapper {
  background: rgba(255, 255, 255, 0.1);
  padding: 1rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease;
}

.category-button:hover .icon-wrapper {
  transform: scale(1.1) rotate(5deg);
}

.category-icon {
  font-size: 2rem;
  color: white;
}

.category-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

@media (max-width: 640px) {
  .category-button {
    flex: 1 1 150px;
    min-width: 140px;
    padding: 1rem;
  }
  
  .category-icon {
    font-size: 1.5rem;
  }
  
  .category-title {
    font-size: 1rem;
  }
  
  .icon-wrapper {
    padding: 0.75rem;
  }
}
</style> 