import { defineStore } from 'pinia'
import axios from 'axios'
import Cookies from 'js-cookie'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

// Add axios interceptor for authentication
axios.interceptors.request.use(
  (config) => {
    const token = Cookies.get('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('Adding token to request:', token.substring(0, 10) + '...')
    } else {
      console.log('No token found in cookies')
    }
    return config
  },
  (error) => {
    console.error('Axios request interceptor error:', error)
    return Promise.reject(error)
  }
)

// Add response interceptor to handle 401 errors
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      console.log('Received 401 response, logging out')
      const authStore = useAuthStore()
      authStore.logout()
    }
    return Promise.reject(error)
  }
)

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
      if (!this.token) {
        console.log('No token available for validation')
        return false
      }
      
      try {
        console.log('Validating token...')
        const response = await axios.get(`${API_URL}/auth/validate`)
        console.log('Token validation response:', response.data)
        
        if (response.data) {
          // If we don't have user data yet, fetch it
          if (!this.user) {
            console.log('Fetching user data...')
            const userResponse = await axios.get(`${API_URL}/users/${response.data}`)
            this.user = userResponse.data
            console.log('User data fetched:', this.user)
          }
          return true
        }
        return false
      } catch (error) {
        console.error('Token validation failed:', error)
        if (error.response?.status === 401) {
          this.logout()
        }
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
      console.log('Setting auth data:', data)
      if (data.token) {
        this.token = data.token
        Cookies.set('token', data.token, { 
          expires: 7, // Store token in cookies for 7 days
          secure: true, // Only send over HTTPS
          sameSite: 'strict' // Prevent CSRF attacks
        })
        console.log('Token stored in cookies')
      }
      if (data.userId) {
        this.user = {
          id: data.userId,
          username: data.username,
          role: data.role
        }
        console.log('User data set:', this.user)
      }
    },
    
    logout() {
      console.log('Logging out...')
      this.token = null
      this.user = null
      Cookies.remove('token')
      console.log('Logged out successfully')
    }
  }
}) 