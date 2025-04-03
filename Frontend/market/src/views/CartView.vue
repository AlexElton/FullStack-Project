<script setup>
// Sample data - will be replaced with API calls
const cartItems = [
  {
    id: 1,
    title: 'Premium Headphones',
    price: 199.99,
    image: 'https://placehold.co/600x400',
    quantity: 1,
  },
  {
    id: 2,
    title: 'Smart Watch',
    price: 299.99,
    image: 'https://placehold.co/600x400',
    quantity: 1,
  },
]

// Placeholder for API integration
const updateQuantity = (itemId, newQuantity) => {
  // TODO: Implement API call to update cart
  console.log(`Updating quantity for item ${itemId} to ${newQuantity}`)
}

const removeItem = (itemId) => {
  // TODO: Implement API call to remove item from cart
  console.log(`Removing item ${itemId} from cart`)
}

const checkout = () => {
  // TODO: Implement checkout process
  console.log('Proceeding to checkout...')
}

const subtotal = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0)
const shipping = 9.99
const total = subtotal + shipping
</script>

<template>
  <div class="cart-page">
    <h1>Shopping Cart</h1>

    <div class="cart-container">
      <div class="cart-items">
        <div v-if="cartItems.length === 0" class="empty-cart">
          <p>Your cart is empty</p>
          <button class="continue-shopping">Continue Shopping</button>
        </div>

        <div v-else class="items-list">
          <div v-for="item in cartItems" :key="item.id" class="cart-item">
            <img :src="item.image" :alt="item.title" class="item-image" />
            <div class="item-details">
              <h3>{{ item.title }}</h3>
              <div class="quantity-controls">
                <button @click="updateQuantity(item.id, item.quantity - 1)">-</button>
                <span>{{ item.quantity }}</span>
                <button @click="updateQuantity(item.id, item.quantity + 1)">+</button>
              </div>
            </div>
            <div class="item-price">${{ (item.price * item.quantity).toFixed(2) }}</div>
            <button class="remove-item" @click="removeItem(item.id)">×</button>
          </div>
        </div>
      </div>

      <div class="cart-summary">
        <h2>Order Summary</h2>
        <div class="summary-item">
          <span>Subtotal</span>
          <span>${{ subtotal.toFixed(2) }}</span>
        </div>
        <div class="summary-item">
          <span>Shipping</span>
          <span>${{ shipping.toFixed(2) }}</span>
        </div>
        <div class="summary-item total">
          <span>Total</span>
          <span>${{ total.toFixed(2) }}</span>
        </div>
        <button class="checkout-button" @click="checkout">Proceed to Checkout</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.cart-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.cart-page h1 {
  margin-bottom: 2rem;
  color: var(--text-color);
}

.cart-container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
}

.cart-items {
  background: white;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.empty-cart {
  text-align: center;
  padding: 3rem;
}

.continue-shopping {
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 1rem;
}

.cart-item {
  display: grid;
  grid-template-columns: auto 1fr auto auto;
  gap: 1rem;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #eee;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
}

.item-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.quantity-controls button {
  background: #f0f0f0;
  border: none;
  width: 30px;
  height: 30px;
  border-radius: 4px;
  cursor: pointer;
}

.remove-item {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
}

.cart-summary {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  height: fit-content;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  margin: 1rem 0;
  color: #666;
}

.summary-item.total {
  font-weight: bold;
  color: var(--text-color);
  font-size: 1.2rem;
  border-top: 1px solid #eee;
  padding-top: 1rem;
}

.checkout-button {
  width: 100%;
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1rem;
  margin-top: 1rem;
  transition: background-color 0.2s;
}

.checkout-button:hover {
  background-color: #3aa876;
}
</style>
