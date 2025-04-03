<script setup>
import { ref } from 'vue'

const categories = ['Electronics', 'Clothing', 'Books', 'Home & Garden', 'Sports', 'Other']
const selectedCategory = ref('')
const title = ref('')
const price = ref('')
const description = ref('')
const images = ref([])

// Placeholder for API integration
const handleSubmit = () => {
  // TODO: Implement API call to create new listing
  console.log('Creating new listing:', {
    title: title.value,
    price: price.value,
    description: description.value,
    category: selectedCategory.value,
    images: images.value,
  })
}

const handleImageUpload = (event) => {
  // TODO: Implement image upload functionality
  console.log('Image upload:', event.target.files)
}
</script>

<template>
  <div class="sell-page">
    <h1>List an Item for Sale</h1>

    <form class="listing-form" @submit.prevent="handleSubmit">
      <div class="form-section">
        <h2>Basic Information</h2>

        <div class="form-group">
          <label for="title">Title</label>
          <input
            id="title"
            v-model="title"
            type="text"
            required
            placeholder="What are you selling?"
          />
        </div>

        <div class="form-group">
          <label for="category">Category</label>
          <select id="category" v-model="selectedCategory" required>
            <option value="">Select a category</option>
            <option v-for="category in categories" :key="category" :value="category">
              {{ category }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label for="price">Price</label>
          <div class="price-input">
            <span class="currency">$</span>
            <input
              id="price"
              v-model="price"
              type="number"
              step="0.01"
              required
              placeholder="0.00"
            />
          </div>
        </div>
      </div>

      <div class="form-section">
        <h2>Description</h2>
        <div class="form-group">
          <label for="description">Item Description</label>
          <textarea
            id="description"
            v-model="description"
            required
            rows="4"
            placeholder="Describe your item in detail..."
          ></textarea>
        </div>
      </div>

      <div class="form-section">
        <h2>Images</h2>
        <div class="form-group">
          <label for="images">Upload Images</label>
          <input id="images" type="file" multiple accept="image/*" @change="handleImageUpload" />
          <p class="help-text">You can upload up to 5 images</p>
        </div>
      </div>

      <div class="form-actions">
        <button type="submit" class="submit-button">List Item for Sale</button>
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
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 2rem;
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
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.price-input {
  position: relative;
  display: flex;
  align-items: center;
}

.currency {
  position: absolute;
  left: 0.75rem;
  color: #666;
}

.price-input input {
  padding-left: 1.5rem;
}

.help-text {
  font-size: 0.9rem;
  color: #666;
  margin-top: 0.5rem;
}

.form-actions {
  margin-top: 2rem;
  text-align: right;
}

.submit-button {
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background-color 0.2s;
}

.submit-button:hover {
  background-color: #3aa876;
}
</style>
