import { useAuthStore } from '@/stores/authStore'
import { storeToRefs } from 'pinia'

import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '../views/ProductsView.vue'
import CartView from '../views/CartView.vue'
import SellView from '../views/SellView.vue'
import ProductView from '../views/ProductView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import MessagesView from '../views/MessagesView.vue'
import ProfilePageView from '../views/ProfilePageView.vue'
import CategoryView from '../views/CategoryView.vue'

import AdminItemView from '../views/AdminItemView.vue'
import AdminUserView from '../views/AdminUserView.vue'
import AdminHomeView from '../views/AdminHomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/categories',
      name: 'categories',
      component: CategoryView,
    },
    {
      path: '/products',
      name: 'products',
      component: ProductsView,
    },
    {
      path: '/products/:id',
      name: 'product',
      component: ProductView,
    },
    {
      path: '/cart',
      name: 'cart',
      component: CartView,
    },
    {
      path: '/messages',
      name: 'messages',
      component: MessagesView,
    },
    {
      path: '/sell',
      name: 'sell',
      component: SellView,
    },
    { 
      path: '/login', 
      name: 'login', 
      component: LoginView, 
    },
    { 
      path: '/register', 
      name: 'register', 
      component: RegisterView, 
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfilePageView,
    },
    {
      path: '/admin/items',
      name: 'admin-items',
      component: AdminItemView,
      meta: { requiresAdmin: true }
    },
    {
      path: '/admin/users',
      name: 'admin-users',
      component: AdminUserView,
      meta: { requiresAdmin: true }
    },
    {
    path: '/admin',
    name: 'admin-home',
    component: AdminHomeView,
    meta: { requiresAdmin: true }
  }
  ],
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const { currentUser } = storeToRefs(authStore)

  // If route requires admin and user is not admin
  if (to.meta.requiresAdmin) {
    if (!currentUser.value || currentUser.value.role !== 'admin') {
      return next({ name: 'home' })  // or redirect to a "Not Authorized" page
    }
  }

  next()
})

export default router
