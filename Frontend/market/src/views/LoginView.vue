<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/authStore'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const router = useRouter()

const login = () => {
  useAuthStore().login({
    username: username.value,
    password: password.value
  })
    .then(() => {
      router.push('/')
      alert('Login successful')
    })
    .catch((error) => {
      alert(error.response?.data?.message || 'Login failed')
    })
}
</script>

<template>
  <div class="auth-container">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <!-- Bind to username instead of email -->
      <input v-model="username" placeholder="Username" type="text" required />
      <input v-model="password" placeholder="Password" type="password" required />
      <button type="submit">Login</button>
    </form>
    <p>Don't have an account? <router-link to="/register">Register</router-link></p>
  </div>
</template>


<style scoped>
.auth-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  background: var(--card-background);
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;

  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

input {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid var(--primary-color);
  border-radius: 4px;
}

button {
  width: 100%;
  margin: 15px 0px;
}
</style>
