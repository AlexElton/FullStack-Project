<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/authStore'
import { useRouter } from 'vue-router'

const username = ref('')
const email = ref('')
const firstName = ref('')
const lastName = ref('')
const password = ref('')
const confirmPassword = ref('')
const router = useRouter()

const register = async () => {
  if (password.value !== confirmPassword.value) {
    alert("Passwords do not match!")
    return
  }
  
  try {
    await useAuthStore().register({
      username: username.value,
      email: email.value,
      firstName: firstName.value,
      lastName: lastName.value,
      password: password.value
    })
    router.push('/login')
  } catch (error) {
    alert(error.response?.data?.message || 'Registration failed')
  }
}
</script>

<template>
  <div class="auth-container">
    <h2>Register</h2>
    <form @submit.prevent="register">
      <input v-model="username" placeholder="Username" type="text" required />
      <input v-model="email" placeholder="Email" type="email" required />
      <input v-model="firstName" placeholder="First Name" type="text" required />
      <input v-model="lastName" placeholder="Last Name" type="text" required />
      <input v-model="password" placeholder="Password" type="password" required />
      <input v-model="confirmPassword" placeholder="Confirm Password" type="password" required />
      <button type="submit">Register</button>
    </form>
    <p>Already have an account? <router-link to="/login">Login</router-link></p>
  </div>
</template>

<style scoped>
/* Same styles as LoginView.vue */
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
