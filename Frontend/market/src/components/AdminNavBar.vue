<script setup>
import { RouterLink } from 'vue-router'
import { ref } from 'vue'

const isMenuOpen = ref(false)
</script>

<template>
  <nav class="navbar">
    <div class="nav-content">
      <div class="nav-brand">
        <RouterLink to="/admin" class="brand">
          <img src="@/assets/mainlogo.svg" alt="MarketPlace Logo" class="logo" />
        </RouterLink>
      </div>

      <div class="nav-links" :class="{ 'nav-open': isMenuOpen }">
        <RouterLink to="/admin/users" class="nav-link" @click="isMenuOpen = false">
          <span class="link-text">Administer registered users</span>
        </RouterLink>
        <RouterLink to="/admin/items" class="nav-link" @click="isMenuOpen = false">
            <span class="link-text">Administer products</span>
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
  background: white;
  color: var(--button-text-color);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  position: sticky;
  top: 0;
  z-index: 1000;
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

.nav-links {
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
  background-color: var(--button-text-color);
  transition: all 0.3s ease;
}

@media (max-width: 1024px) {
  .menu-toggle {
    display: flex;
  }

  .nav-links {
    display: none;
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background-color: #ffffff;
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
}

@media (max-width: 640px) {
  .nav-content {
    padding: 0.75rem 1rem;
  }

  .logo {
    height: 32px;
  }

  .nav-links {
    padding: 0.75rem;
  }
}
</style>
