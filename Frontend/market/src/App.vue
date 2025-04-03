<script setup>
import { RouterView } from 'vue-router'
import NavBar from './components/NavBar.vue'
</script>

<template>
  <div class="app">
    <NavBar />
    <main class="main-content">
      <RouterView />
    </main>
    <footer class="footer">
      <p>&copy; 2024 MarketPlace. All rights reserved.</p>
    </footer>
  </div>
</template>

<style>
:root {
  --primary-color: #f0931c;
  --text-color: #ffffff;
  --background-color: #000000;
  --card-background: #1a1a1a;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  color: var(--text-color);
  background-color: var(--background-color);
  line-height: 1.6;
}

.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding: 2rem;
}

.footer {
  background-color: var(--card-background);
  padding: 1rem;
  text-align: center;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.3);
}
</style>
