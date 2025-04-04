<script setup>
import { RouterLink } from 'vue-router'
import { ref } from 'vue'

const isMenuOpen = ref(false)
</script>

<template>
  <nav class="navbar">
    <div class="nav-content">
      <div class="nav-brand">
        <RouterLink to="/" class="brand">
          <img src="@/assets/mainlogo.svg" alt="MarketPlace Logo" class="logo" />
        </RouterLink>
      </div>

      <div class="nav-links" :class="{ 'nav-open': isMenuOpen }">
        <RouterLink to="/" class="nav-link" @click="isMenuOpen = false">
          <span class="link-text">Home</span>
        </RouterLink>
        <RouterLink to="/products" class="nav-link" @click="isMenuOpen = false">
          <span class="link-text">Products</span>
        </RouterLink>
        <RouterLink to="/categories" class="nav-link" @click="isMenuOpen = false">
          <span class="link-text">Categories</span>
        </RouterLink>
        <RouterLink to="/messages" class="nav-link" @click="isMenuOpen = false">
          <span class="link-text">Messages</span>
          <span class="message-count">2</span>
        </RouterLink>
      </div>

      <div class="nav-actions" :class="{ 'nav-open': isMenuOpen }">
        <RouterLink to="/sell" class="nav-link sell-link" @click="isMenuOpen = false">
          <span class="link-text">Sell</span>
        </RouterLink>
        <RouterLink to="/login" class="nav-link" @click="isMenuOpen = false">
          <span class="link-text">Login</span>
        </RouterLink>
        <RouterLink to="/register" class="nav-link register-link" @click="isMenuOpen = false">
          <span class="link-text">Register</span>
        </RouterLink>
      </div>

      <button class="menu-toggle" @click="isMenuOpen = !isMenuOpen">
        <span class="menu-icon"></span>
        <span class="menu-icon"></span>
        <span class="menu-icon"></span>
      </button>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  background-color: var(--primary-color);
  color: var(--button-text-color);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  position: sticky;
  top: 0;
  z-index: 10000;
  width: 100%;
}

.nav-content {
  width: 100%;
  padding: 0.75rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-brand {
  margin-right: 2rem;
}

.brand {
  text-decoration: none;
  display: flex;
  align-items: center;
}

.logo {
  height: 40px;
  width: auto;
}

.nav-links,
.nav-actions {
  display: flex;
  gap: 1.5rem;
  align-items: center;
}

.nav-link {
  color: var(--button-text-color);
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: all 0.2s ease;
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  white-space: nowrap;
}

.nav-link:hover {
  background-color: var(--button-hover-background);
  color: var(--button-text-color);
}

.nav-link.router-link-active {
  color: #00841d;
  background-color: rgba(130, 240, 28, 0.1);
}

.sell-link {
  background-color: var(--button-background);
  color: var(--button-text-color);
  font-weight: 600;
}

.sell-link:hover {
  background-color: var(--button-hover-background);
  color: var(--button-text-color);
  transform: translateY(-1px);
}

.register-link {
  background-color: #ffffff;
  color: #000000;
  border: 1px solid var(--button-background);
}

.register-link:hover {
  background-color: #f8f8f8;
}

.message-count {
  background-color: var(--button-background);
  color: #000000;
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 999px;
  min-width: 20px;
  text-align: center;
  font-weight: 600;
}

.menu-toggle {
  display: none;
  flex-direction: column;
  gap: 6px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
}

.menu-icon {
  display: block;
  width: 24px;
  height: 2px;
  background-color: #ffffff;
  transition: all 0.3s ease;
}

@media (max-width: 1024px) {
  .menu-toggle {
    display: flex;
  }

  .nav-links,
  .nav-actions {
    display: none;
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background-color: #000000;
    padding: 1rem;
    flex-direction: column;
    gap: 0.5rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }

  .nav-open {
    display: flex;
  }

  .nav-link {
    width: 100%;
    justify-content: center;
  }

  .sell-link {
    margin-top: 0.5rem;
  }
}

@media (max-width: 640px) {
  .nav-content {
    padding: 0.75rem 1rem;
  }

  .logo {
    height: 32px;
  }

  .nav-links,
  .nav-actions {
    padding: 0.75rem;
  }
}
</style>
