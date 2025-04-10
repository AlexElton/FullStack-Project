<script setup>
import { ref } from 'vue'
import { useStripe } from '@vue-stripe/vue-stripe'

const stripe = useStripe()
const loading = ref(false)

const handleCheckout = async () => {
  try {
    loading.value = true
    console.log('Creating checkout session...')
    
    // In a real application, you would make an API call to your backend
    // to create a checkout session and get the session ID
    // For this example, we'll simulate it with a timeout
    const sessionId = await new Promise(resolve => {
      setTimeout(() => {
        // This is a placeholder. In a real app, you would get this from your backend
        resolve('cs_test_example_session_id')
      }, 500)
    })
    
    // Redirect to Stripe Checkout
    const { error } = await stripe.redirectToCheckout({
      sessionId: sessionId
    })
    
    if (error) {
      console.error('Error redirecting to checkout:', error)
    }
  } catch (error) {
    console.error('Error handling checkout:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="stripe-checkout">
    <button 
      class="checkout-button" 
      @click="handleCheckout" 
      :disabled="loading"
    >
      {{ loading ? 'Processing...' : 'Buy Now' }}
    </button>
  </div>
</template>

<style scoped>
.stripe-checkout {
  margin: 1rem 0;
}

.checkout-button {
  padding: 0.75rem 1.5rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s;
  font-size: 1rem;
}

.checkout-button:hover {
  background-color: #45a049;
}

.checkout-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style> 