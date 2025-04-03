<script setup>
import { ref } from 'vue'

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const register = () => {
    if (password.value !== confirmPassword.value) {
    alert("Passwords do not match!")
    return
  }
  alert('Registering:', name.value, email.value, password.value)
}
</script>

<template>
  <div class="auth-container">
    <h2>Register</h2>
    <form @submit.prevent="register">
      <input v-model="name" placeholder="Name "type="text" required />

      <input v-model="email" placeholder="Email" type="email" required />

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
