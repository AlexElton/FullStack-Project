<script setup>
import { onMounted } from 'vue'
import { useItemStore } from '@/stores/itemStore' // adjust the path if needed
import { storeToRefs } from 'pinia'

const itemStore = useItemStore()
const { items, loading, error } = storeToRefs(itemStore)

onMounted(() => {
  itemStore.getActiveItems()
})

const deleteItem = async (id) => {
  if (confirm('Are you sure you want to delete this item?')) {
    try {
      await itemStore.deleteItem(id)
      alert('Item deleted successfully.')
    } catch (err) {
      alert('Failed to delete item: ' + itemStore.error)
    }
  }
}
</script>

<template>
  <div class="admin-items-page">
    <h1>Admin - Manage Items</h1>

    <div v-if="loading">Loading items...</div>
    <div v-if="error" class="error">{{ error }}</div>

    <table v-if="!loading && items.length" class="items-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Price</th>
          <th>Category</th>
          <th>Seller</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.title }}</td>
          <td>{{ item.price }}</td>
          <td>{{ item.category?.name || 'N/A' }}</td>
          <td>{{ item.seller?.username || 'Unknown' }}</td>
          <td>
            <button @click="deleteItem(item.id)" class="delete-btn">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else-if="!loading && !items.length">No items found.</div>
  </div>
</template>

<style scoped>
.admin-items-page {
  padding: 2rem;
}

h1 {
  margin-bottom: 1rem;
}

.items-table {
  width: 100%;
  border-collapse: collapse;
}

.items-table th,
.items-table td {
  padding: 0.75rem 1rem;
  border: 1px solid #ccc;
  text-align: left;
}

.delete-btn {
  background-color: #e3342f;
  color: white;
  border: none;
  padding: 0.4rem 0.8rem;
  cursor: pointer;
  border-radius: 4px;
  font-size: 0.9rem;
}

.delete-btn:hover {
  background-color: #cc1f1a;
}

.error {
  color: red;
  margin: 1rem 0;
}
</style>
