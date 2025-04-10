import { defineStore } from 'pinia'
import Cookies from 'js-cookie'

export const useSearchStore = defineStore('search', {
  state: () => {
    // Get initial values from cookies
    const lastSearch = Cookies.get('lastSearch')
    const lastCategory = Cookies.get('lastCategory')
    
    console.log('Initializing store with cookies:', { lastSearch, lastCategory })
    
    return {
      lastSearch: lastSearch || '',
      lastCategory: lastCategory || ''
    }
  },
  
  actions: {
    setSearch(search) {
      console.log('Setting search:', search)
      this.lastSearch = search
      if (search) {
        Cookies.set('lastSearch', search, { 
          expires: 7,
          path: '/',
          sameSite: 'strict'
        })
      } else {
        Cookies.remove('lastSearch', { path: '/' })
      }
    },
    
    setCategory(category) {
      console.log('Setting category:', category)
      this.lastCategory = category
      if (category && category !== 'All') {
        Cookies.set('lastCategory', category, { 
          expires: 7,
          path: '/',
          sameSite: 'strict'
        })
      } else {
        Cookies.remove('lastCategory', { path: '/' })
      }
    },
    
    clearSearch() {
      console.log('Clearing search and category')
      this.lastSearch = ''
      this.lastCategory = ''
      Cookies.remove('lastSearch', { path: '/' })
      Cookies.remove('lastCategory', { path: '/' })
    }
  }
}) 