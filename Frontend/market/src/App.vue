<script setup>
import { provide, computed } from 'vue';
import { useMessageStore } from '@/composables/messageStore';
import { RouterView } from 'vue-router';
import NavBar from './components/NavBar.vue';

// Use the message store composable
const messageStore = useMessageStore();

// Provide the unreadCount from the message store
provide('unreadCount', messageStore.unreadCount);
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
/* Remove the CSS variable overrides that might be causing issues */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
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
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  color: var(--text-color);
}
</style>
