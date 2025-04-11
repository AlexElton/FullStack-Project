<script setup>
import { ref, onMounted } from 'vue'

const users = ref([])
const loading = ref(false)
const error = ref(null)

const fetchUsers = async () => {
  loading.value = true
  error.value = null
  try {
    users.value = [
      { id: 1, username: 'admin', email: 'admin@example.com', role: 'ADMIN' },
      { id: 2, username: 'johndoe', email: 'john@example.com', role: 'USER' },
      { id: 3, username: 'janedoe', email: 'jane@example.com', role: 'USER' }
    ]
  } catch (err) {
    error.value = 'Failed to load users.'
  } finally {
    loading.value = false
  }
}

onMounted(fetchUsers)

const deleteUser = async (id) => {
  if (confirm(`Are you sure you want to delete user ID ${id}?`)) {
    users.value = users.value.filter(user => user.id !== id)
    alert(`User ID ${id} deleted (mocked).`)
  }
}
</script>

<template>
  <div class="admin-users-page">
    <h1>Admin - Manage Users</h1>

    <div v-if="loading">Loading users...</div>
    <div v-if="error" class="error">{{ error }}</div>

    <table v-if="!loading && users.length" class="users-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>Role</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td>
            <button @click="deleteUser(user.id)" class="delete-btn">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else-if="!loading && !users.length">No users found.</div>
  </div>
</template>

<style scoped>
.admin-users-page {
  padding: 2rem;
}

h1 {
  margin-bottom: 1rem;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
}

.users-table th,
.users-table td {
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
