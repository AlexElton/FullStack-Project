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

const app = createApp(App)

app.component('font-awesome-icon', FontAwesomeIcon)
app.use(createPinia())
app.use(router)

app.mount('#app')
