import { defineStore } from 'pinia'
import axios from 'axios'
import { useAuthStore } from './authStore'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

export const useCategoryStore = defineStore('categories', {
  state: () => ({
    categories: [],
    currentCategory: null,
    topLevelCategories: [],
    loading: false,
    error: null,
    totalCategories: 0,
    currentPage: 0,
    pageSize: 10
  }),
  
  getters: {
    getCategoryById: (state) => (id) => state.categories.find(category => category.categoryId === id)
  },
  
  actions: {
    async createCategory(categoryData) {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        const response = await axios.post(`${API_URL}/categories`, null, {
          headers: { Authorization: `Bearer ${authStore.token}` },
          params: categoryData
        })
        this.categories.unshift(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to create category'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async updateCategory(categoryId, categoryData) {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        const response = await axios.put(`${API_URL}/categories/${categoryId}`, null, {
          headers: { Authorization: `Bearer ${authStore.token}` },
          params: categoryData
        })
        const index = this.categories.findIndex(category => category.id === categoryId)
        if (index !== -1) {
          this.categories[index] = response.data
        }
        if (this.currentCategory?.id === categoryId) {
          this.currentCategory = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to update category'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async deleteCategory(categoryId) {
      this.loading = true
      this.error = null
      const authStore = useAuthStore()
      
      try {
        await axios.delete(`${API_URL}/categories/${categoryId}`, {
          headers: { Authorization: `Bearer ${authStore.token}` }
        })
        this.categories = this.categories.filter(category => category.id !== categoryId)
        if (this.currentCategory?.id === categoryId) {
          this.currentCategory = null
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to delete category'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getCategory(categoryId) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/categories/${categoryId}`)
        this.currentCategory = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch category'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getTopLevelCategories() {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/categories/top-level`)
        this.topLevelCategories = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch top-level categories'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getAllCategories(page = 0) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/categories`, {
          params: {
            page,
            size: this.pageSize
          }
        })
        this.categories = response.data.content
        this.totalCategories = response.data.totalElements
        this.currentPage = page
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch categories'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getSubcategories(parentId) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/categories/${parentId}/subcategories`)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch subcategories'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async searchCategories(query) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/categories/search`, {
          params: { query }
        })
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to search categories'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getCategoryPath(categoryId) {
      this.loading = true
      this.error = null
      
      try {
        const response = await axios.get(`${API_URL}/categories/${categoryId}/path`)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch category path'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
}) 