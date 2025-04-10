import { defineStore } from 'pinia'
import axios from 'axios'
import Cookies from 'js-cookie'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: Cookies.get('token') || null,
    loading: false,
    error: null
  }),
  
  getters: {
    isAuthenticated: (state) => !!state.token,
    currentUser: (state) => state.user,
    userRole: (state) => state.user?.role
  },
  
  actions: {
    async register(userData) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post(`${API_URL}/auth/register`, userData)
        this.setAuthData(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Registration failed'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async login(credentials) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post(`${API_URL}/auth/login`, credentials)
        this.setAuthData(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Login failed'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async validateToken() {
      if (!this.token) return false
      
      try {
        const response = await axios.get(`${API_URL}/auth/validate`, {
          headers: { Authorization: `Bearer ${this.token}` }
        })
        return true
      } catch (error) {
        this.logout()
        return false
      }
    },
    
    async checkEmail(email) {
      try {
        const response = await axios.get(`${API_URL}/auth/check-email/${email}`)
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    async checkUsername(username) {
      try {
        const response = await axios.get(`${API_URL}/auth/check-username/${username}`)
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    setAuthData(data) {
      this.token = data.token
      this.user = {
        id: data.userId,
        username: data.username,
        role: data.role
      }
      Cookies.set('token', data.token, { expires: 7 }) // Store token for 7 days
    },
    
    logout() {
      this.user = null
      this.token = null
      this.error = null
      Cookies.remove('token')
    }
  }
}) 