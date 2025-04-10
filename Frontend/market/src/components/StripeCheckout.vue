<script setup>
import { ref, onMounted, defineProps } from 'vue'
import { loadStripe } from '@stripe/stripe-js'

const props = defineProps({
  itemTitle: {
    type: String,
    required: true
  },
  itemPrice: {
    type: Number,
    required: true
  },
  itemImage: {
    type: String,
    default: ''
  }
})

const loading = ref(false)
const error = ref(null)
const stripeLoaded = ref(false)
const checkoutSuccess = ref(false)
let stripe = null

onMounted(async () => {
  console.log('StripeCheckout component mounted')
  try {
    // Initialize Stripe directly
    stripe = await loadStripe(import.meta.env.VITE_STRIPE_PUBLIC_KEY)
    if (stripe) {
      console.log('Stripe instance is available')
      stripeLoaded.value = true
    } else {
      console.error('Stripe instance is not available')
      error.value = 'Stripe is not properly initialized'
    }
  } catch (err) {
    console.error('Error initializing Stripe:', err)
    error.value = 'Failed to initialize Stripe'
  }
})

const handleCheckout = async () => {
  try {
    loading.value = true
    error.value = null
    console.log('Creating checkout session for:', props.itemTitle)
    
    if (!stripe) {
      throw new Error('Stripe is not initialized')
    }
    
    // In a real application, you would make an API call to your backend
    // to create a checkout session and get the session ID
    // For this example, we'll simulate a successful checkout
    
    // Simulate API call delay
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // Simulate successful checkout
    checkoutSuccess.value = true
    console.log('Checkout successful for:', props.itemTitle)
    
    // In a real application, you would redirect to a success page
    // or update the UI to show the purchase was successful
    
  } catch (err) {
    console.error('Error handling checkout:', err)
    error.value = err.message || 'An unknown error occurred'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="stripe-checkout">
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
    
    <div v-if="checkoutSuccess" class="success-message">
      <div class="success-icon">✓</div>
      <div class="success-text">
        <h3>Purchase Successful!</h3>
        <p>Thank you for your purchase of {{ itemTitle }}.</p>
        <p>You will receive a confirmation email shortly.</p>
      </div>
    </div>
    
    <div v-else>
      <div class="item-details">
        <div class="item-title">{{ itemTitle }}</div>
        <div class="item-price">{{ itemPrice }} NOK</div>
      </div>
      
      <button 
        class="checkout-button" 
        @click="handleCheckout" 
        :disabled="loading || !stripeLoaded"
      >
        {{ loading ? 'Processing...' : 'Buy Now' }}
      </button>
      
      <div v-if="!stripeLoaded" class="stripe-not-loaded">
        Stripe is not properly initialized
      </div>
    </div>
  </div>
</template>

<style scoped>
.stripe-checkout {
  margin-top: 1.5rem;
  padding: 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.error-message {
  color: #d32f2f;
  margin-bottom: 1rem;
  padding: 0.5rem;
  background-color: #ffebee;
  border-radius: 4px;
}

.success-message {
  display: flex;
  align-items: center;
  padding: 1rem;
  background-color: #e8f5e9;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.success-icon {
  font-size: 2rem;
  color: #4CAF50;
  margin-right: 1rem;
}

.success-text h3 {
  margin: 0 0 0.5rem 0;
  color: #2e7d32;
}

.success-text p {
  margin: 0;
  color: #333;
}

.item-details {
  margin-bottom: 1rem;
}

.item-title {
  font-weight: bold;
  margin-bottom: 0.25rem;
}

.item-price {
  color: #2e7d32;
  font-weight: bold;
}

.checkout-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s;
  width: 100%;
}

.checkout-button:hover {
  background-color: #388E3C;
}

.checkout-button:disabled {
  background-color: #9E9E9E;
  cursor: not-allowed;
}

.stripe-not-loaded {
  color: #d32f2f;
  margin-top: 0.5rem;
  font-size: 0.9rem;
}
</style> 