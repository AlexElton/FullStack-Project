import { defineStore } from 'pinia'
import axios from 'axios'
import { useAuthStore } from './authStore'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

export const useItemStore = defineStore('items', {
  state: () => ({
    items: [],
    currentItem: null,
    userListings: [],
    loading: false,
    error: null,
    totalItems: 0,
    currentPage: 0,
    pageSize: 10
  }),
  
  getters: {
    getItemById: (state) => (id) => state.items.find(item => item.id === id)
  },
  
  actions: {
    async createItem(itemData) {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        const response = await axios.post(`${API_URL}/items`, itemData, {
          headers: { Authorization: `Bearer ${authStore.token}` }
        })
        this.items.unshift(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to create item'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getItem(itemId) {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        const headers = authStore.token ? { Authorization: `Bearer ${authStore.token}` } : {}
        const response = await axios.get(`${API_URL}/items/${itemId}`, { headers })
        this.currentItem = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch item'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async updateItem(itemId, itemData) {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        const response = await axios.put(`${API_URL}/items/${itemId}`, itemData, {
          headers: { Authorization: `Bearer ${authStore.token}` }
        })
        const index = this.items.findIndex(item => item.id === itemId)
        if (index !== -1) {
          this.items[index] = response.data
        }
        if (this.currentItem?.id === itemId) {
          this.currentItem = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to update item'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async deleteItem(itemId) {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        await axios.delete(`${API_URL}/items/${itemId}`, {
          headers: { Authorization: `Bearer ${authStore.token}` }
        })
        this.items = this.items.filter(item => item.id !== itemId)
        if (this.currentItem?.id === itemId) {
          this.currentItem = null
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to delete item'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getItemsBySeller(sellerId, page = 0) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/items/seller/${sellerId}`, {
          params: {
            page,
            size: this.pageSize
          }
        })
        this.items = response.data.content
        this.totalItems = response.data.totalElements
        this.currentPage = page
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch seller items'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getItemsByCategory(categoryId, page = 0) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/items/category/${categoryId}`, {
          params: {
            page,
            size: this.pageSize
          }
        })
        this.items = response.data.content
        this.totalItems = response.data.totalElements
        this.currentPage = page
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch category items'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getActiveItems(page = 0) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/items/active`, {
          params: {
            page,
            size: this.pageSize
          }
        })
        this.items = response.data.content
        this.totalItems = response.data.totalElements
        this.currentPage = page
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch active items'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async searchItems(query, page = 0) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/items/search`, {
          params: {
            query,
            page,
            size: this.pageSize
          }
        })
        this.items = response.data.content
        this.totalItems = response.data.totalElements
        this.currentPage = page
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to search items'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getItemsByPriceRange(minPrice, maxPrice, page = 0) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/items/price-range`, {
          params: {
            minPrice,
            maxPrice,
            page,
            size: this.pageSize
          }
        })
        this.items = response.data.content
        this.totalItems = response.data.totalElements
        this.currentPage = page
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch items by price range'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getNearbyItems(longitude, latitude, distance) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/items/nearby`, {
          params: {
            longitude,
            latitude,
            distance
          }
        })
        this.items = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch nearby items'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getUserListings() {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        const response = await axios.get(`${API_URL}/items/seller/${authStore.currentUser.id}`, {
          headers: { Authorization: `Bearer ${authStore.token}` }
        })
        this.userListings = response.data.content
        return this.userListings
      } catch (error) {
        console.error('Error fetching user listings:', error)
        this.error = error.response?.data?.message || 'Failed to load your listings'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
}) 