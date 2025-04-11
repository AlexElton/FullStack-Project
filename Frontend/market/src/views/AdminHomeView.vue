<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToUsers = () => router.push('/admin/users')
const goToItems = () => router.push('/admin/items')

// Admin-themed hero image
const heroImage = 'https://res.cloudinary.com/dvxtp0hrq/image/upload/v1743697402/cld-sample-3.jpg'
</script>

<template>
  <main class="admin-home">
    <section class="hero" :style="{ backgroundImage: `url(${heroImage})` }">
      <div class="hero-content">
        <h1>Welcome, Admin</h1>
        <p>Manage the platform and keep things running smoothly</p>
        <div class="buttons">
          <button class="cta-button" @click="goToUsers">Manage Users</button>
          <button class="cta-button" @click="goToItems">Manage Listings</button>
        </div>
      </div>
    </section>

    <section class="admin-tasks">
      <h2>Quick Actions</h2>
      <div class="tasks-grid">
        <div class="task-card" @click="goToUsers">
          <h3>Users</h3>
          <p>View and manage registered users</p>
        </div>
        <div class="task-card" @click="goToItems">
          <h3>Listings</h3>
          <p>Browse and moderate listed items</p>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
.admin-home {
  min-height: 100vh;
}

.hero {
  background-size: cover;
  background-position: center;
  height: 60vh;
  padding: 2rem 1rem;
  display: flex;
  justify-content: center;
  align-items: center;
}

.hero-content {
  background: rgba(0, 0, 0, 0.5);
  padding: 2rem;
  border-radius: 8px;
  text-align: center;
  max-width: 800px;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(4px);
}

.hero h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.hero p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}

.cta-button {
  background-color: var(--button-background);
  color: var(--button-text-color);
  font-weight: bold;
  border: none;
  padding: 0.8rem 1.5rem;
  margin: 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.cta-button:hover {
  background-color: var(--button-hover-background);
}

.admin-tasks {
  padding: 4rem 2rem;
  max-width: 1200px;
  margin: auto;
}

.admin-tasks h2 {
  text-align: center;
  font-size: 2rem;
  margin-bottom: 2rem;
  color: var(--text-color);
}

.tasks-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 2rem;
}

.task-card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.2s ease;
  cursor: pointer;
  border: 1px solid #f0f0f0;
}

.task-card:hover {
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.12);
  border-color: var(--button-hover-background);
}

.task-card h3 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  color: var(--text-color);
}

.task-card p {
  color: #666;
}
</style>
