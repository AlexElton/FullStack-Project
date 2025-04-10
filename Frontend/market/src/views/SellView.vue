<script setup>
import { ref, onMounted } from 'vue'
import { useItemStore } from '@/stores/itemStore'
import { useAuthStore } from '@/stores/authStore'
import { useCategoryStore } from '@/stores/categoryStore'
import { useRouter } from 'vue-router'

const router = useRouter()
const itemStore = useItemStore()
const authStore = useAuthStore()
const categoryStore = useCategoryStore()

// Form fields
const title = ref('')
const briefDescription = ref('')
const fullDescription = ref('')
const price = ref('')
const quantity = ref(1)
const condition = ref('NEW')
const categoryId = ref(null) // Changed from hardcoded value
const allowOffers = ref(true)
const acceptVipps = ref(true)
const locationAddress = ref('')
const tags = ref([])
const imageUrls = ref([])
const error = ref(null)
const loading = ref(false)

// Available options
const conditions = ['NEW', 'LIKE_NEW', 'GOOD', 'FAIR', 'POOR']

// Check authentication and fetch categories on mount
onMounted(async () => {
  if (!authStore.isAuthenticated) {
    router.push('/login?redirect=/sell')
    return
  }
  
  try {
    console.log('Fetching categories...')
    await categoryStore.getAllCategories()
    console.log('Categories fetched:', categoryStore.categories)
    if (categoryStore.categories.length > 0) {
      categoryId.value = categoryStore.categories[0].categoryId
      console.log('Selected first category:', categoryId.value)
    } else {
      console.log('No categories available')
    }
  } catch (err) {
    console.error('Error fetching categories:', err)
    error.value = 'Failed to load categories. Please try again later.'
  }
})

const handleSubmit = async () => {
  try {
    if (!authStore.isAuthenticated) {
      throw new Error('You must be logged in to sell items')
    }

    loading.value = true
    error.value = null

    const itemData = {
      title: title.value,
      briefDescription: briefDescription.value,
      fullDescription: fullDescription.value,
      price: parseFloat(price.value),
      quantity: parseInt(quantity.value),
      condition: condition.value,
      categoryId: parseInt(categoryId.value),
      allowOffers: allowOffers.value,
      acceptVipps: acceptVipps.value,
      locationAddress: locationAddress.value || null,
      tags: tags.value,
      imageUrls: imageUrls.value,
      currency: 'NOK' // Default currency as required by backend
    }

    console.log('Submitting item data:', itemData) // Debug log
    await itemStore.createItem(itemData)
    router.push('/profile/listings') // Redirect to listings after success
  } catch (err) {
    console.error('Error creating item:', err)
    error.value = err.response?.data?.message || err.message || 'Failed to create item'
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  title.value = ''
  briefDescription.value = ''
  fullDescription.value = ''
  price.value = ''
  quantity.value = 1
  condition.value = 'NEW'
  categoryId.value = null
  allowOffers.value = true
  acceptVipps.value = true
  locationAddress.value = ''
  tags.value = []
  imageUrls.value = []
}

const handleImageUpload = async (event) => {
  const files = event.target.files
  if (!files || files.length === 0) return

  try {
    // TODO: Implement actual image upload to your storage service
    // For now, we'll just simulate getting URLs
    const uploadedUrls = await Promise.all(
      Array.from(files).map(async (file) => {
        // Simulate upload delay
        await new Promise(resolve => setTimeout(resolve, 500))
        return URL.createObjectURL(file)
      })
    )
    
    imageUrls.value = [...imageUrls.value, ...uploadedUrls]
  } catch (error) {
    console.error('Error uploading images:', error)
  }
}

const removeImage = (index) => {
  imageUrls.value.splice(index, 1)
}

const addTag = (event) => {
  const tag = event.target.value.trim()
  if (tag && !tags.value.includes(tag)) {
    tags.value.push(tag)
    event.target.value = ''
  }
}

const removeTag = (index) => {
  tags.value.splice(index, 1)
}
</script>

<template>
  <div class="sell-page">
    <h1>List an Item for Sale</h1>

    <!-- Show error message if present -->
    <div v-if="error" class="error-message">
      {{ error }}
    </div>

    <!-- Show loading state -->
    <div v-if="loading" class="loading-state">
      Creating your listing...
    </div>

    <form class="listing-form" @submit.prevent="handleSubmit" :disabled="loading">
      <div class="form-section">
        <h2>Basic Information</h2>

        <div class="form-group">
          <label for="title">Title</label>
          <input
            id="title"
            v-model="title"
            type="text"
            required
            minlength="3"
            maxlength="100"
            placeholder="What are you selling?"
          />
        </div>

        <div class="form-group">
          <label for="category">Category</label>
          <select
            id="category"
            v-model="categoryId"
            required
          >
            <option v-for="category in categoryStore.categories" 
                    :key="category.categoryId" 
                    :value="category.categoryId">
              {{ category.name }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label for="price">Price (NOK)</label>
          <div class="price-input">
            <input
              id="price"
              v-model="price"
              type="number"
              step="0.01"
              min="0.01"
              required
              placeholder="0.00"
            />
          </div>
        </div>

        <div class="form-group">
          <label for="quantity">Quantity</label>
          <input
            id="quantity"
            v-model="quantity"
            type="number"
            min="1"
            required
          />
        </div>

        <div class="form-group">
          <label for="condition">Condition</label>
          <select id="condition" v-model="condition" required>
            <option v-for="cond in conditions" :key="cond" :value="cond">
              {{ cond.replace('_', ' ') }}
            </option>
          </select>
        </div>
      </div>

      <div class="form-section">
        <h2>Description</h2>
        <div class="form-group">
          <label for="briefDescription">Brief Description</label>
          <textarea
            id="briefDescription"
            v-model="briefDescription"
            required
            maxlength="255"
            rows="2"
            placeholder="A short description of your item..."
          ></textarea>
        </div>

        <div class="form-group">
          <label for="fullDescription">Full Description</label>
          <textarea
            id="fullDescription"
            v-model="fullDescription"
            required
            rows="4"
            placeholder="Describe your item in detail..."
          ></textarea>
        </div>
      </div>

      <div class="form-section">
        <h2>Location</h2>
        <div class="form-group">
          <label for="location">Location Address</label>
          <input
            id="location"
            v-model="locationAddress"
            type="text"
            placeholder="Where is the item located?"
          />
        </div>
      </div>

      <div class="form-section">
        <h2>Tags</h2>
        <div class="form-group">
          <label for="tags">Add Tags</label>
          <input
            id="tags"
            type="text"
            @keyup.enter="addTag"
            placeholder="Press Enter to add a tag"
          />
          <div class="tags-container">
            <span v-for="(tag, index) in tags" :key="index" class="tag">
              {{ tag }}
              <button type="button" @click="removeTag(index)" class="remove-tag">×</button>
            </span>
          </div>
        </div>
      </div>

      <div class="form-section">
        <h2>Images</h2>
        <div class="form-group">
          <label for="images">Upload Images</label>
          <input
            id="images"
            type="file"
            multiple
            accept="image/*"
            @change="handleImageUpload"
          />
          <p class="help-text">You can upload up to 5 images</p>
          
          <div class="image-preview-container">
            <div v-for="(url, index) in imageUrls" :key="index" class="image-preview">
              <img :src="url" :alt="'Preview ' + (index + 1)" />
              <button type="button" @click="removeImage(index)" class="remove-image">×</button>
            </div>
          </div>
        </div>
      </div>

      <div class="form-section">
        <h2>Additional Options</h2>
        <div class="form-group checkbox-group">
          <label>
            <input type="checkbox" v-model="allowOffers" />
            Allow offers
          </label>
          <label>
            <input type="checkbox" v-model="acceptVipps" />
            Accept Vipps payments
          </label>
        </div>
      </div>

      <div class="form-actions">
        <button 
          type="submit" 
          class="submit-button"
          :disabled="loading"
        >
          {{ loading ? 'Creating...' : 'List Item for Sale' }}
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.sell-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.sell-page h1 {
  margin-bottom: 2rem;
  color: var(--text-color);
}

.listing-form {
  background: var(--card-background);
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.form-section:last-child {
  border-bottom: none;
}

.form-section h2 {
  color: var(--text-color);
  margin-bottom: 1rem;
  font-size: 1.2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--text-color);
  font-weight: 500;
}

.form-group input[type='text'],
.form-group input[type='number'],
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  font-size: 1rem;
  background-color: var(--background-color);
  color: var(--text-color);
}

.price-input {
  position: relative;
  display: flex;
  align-items: center;
}

.help-text {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin-top: 0.5rem;
}

.form-actions {
  margin-top: 2rem;
  text-align: right;
}

.submit-button {
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  padding: 1rem 2rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background-color 0.2s;
}

.submit-button:hover {
  background-color: var(--button-hover-background);
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.tag {
  background-color: var(--tag-background);
  color: var(--tag-text);
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.remove-tag {
  background: none;
  border: none;
  color: var(--tag-text);
  cursor: pointer;
  padding: 0 0.25rem;
  font-size: 1.2rem;
}

.image-preview-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}

.image-preview {
  position: relative;
  aspect-ratio: 1;
  border-radius: 4px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.checkbox-group label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.checkbox-group input[type="checkbox"] {
  width: auto;
}

.error-message {
  background-color: var(--error-background);
  color: var(--error-text);
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 4px;
  border: 1px solid var(--error-border);
}

.loading-state {
  text-align: center;
  padding: 1rem;
  color: var(--text-secondary);
}

.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  font-size: 1rem;
  background-color: var(--background-color);
  color: var(--text-color);
  cursor: pointer;
}
</style>
