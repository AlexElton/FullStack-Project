import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { 
  faLaptop, 
  faTshirt, 
  faHome, 
  faGamepad, 
  faCar, 
  faBook, 
  faFilm, 
  faBasketballBall, 
  faSpa, 
  faShoppingBasket, 
  faPencilAlt, 
  faPaw, 
  faGem, 
  faTools, 
  faPalette 
} from '@fortawesome/free-solid-svg-icons'
import { loadStripe } from '@stripe/stripe-js'
import VueStripe from '@vue-stripe/vue-stripe'

import App from './App.vue'
import router from './router'

// Add icons to the library
library.add(
  faLaptop, 
  faTshirt, 
  faHome, 
  faGamepad, 
  faCar, 
  faBook, 
  faFilm, 
  faBasketballBall, 
  faSpa, 
  faShoppingBasket, 
  faPencilAlt, 
  faPaw, 
  faGem, 
  faTools, 
  faPalette
)

// Initialize Stripe
const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLIC_KEY)

const app = createApp(App)

app.component('font-awesome-icon', FontAwesomeIcon)
app.use(createPinia())
app.use(router)
app.use(VueStripe, { stripePromise })

app.mount('#app')
