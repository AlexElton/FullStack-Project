<script setup>
import { ref } from 'vue'

// Sample message data - will be replaced with API calls
const messages = ref([
  {
    id: 1,
    sender: {
      name: 'Ingvild',
      avatar: 'https://placehold.co/100x100',
      verified: true,
      memberSince: '2014',
      rating: 9.9,
      reviews: 81
    },
    item: {
      title: 'Garmin HRM-Tri',
      price: 750,
      image: 'https://placehold.co/600x400',
      status: 'Sold'
    },
    date: 'March 26, 22:11',
    content: 'Hooray, the item is yours! 🎉',
  },
  {
    id: 2,
    sender: {
      name: 'Antoni',
      avatar: 'https://placehold.co/100x100'
    },
    item: {
      title: 'NorthWave Cycling Shoes',
      price:  500,
      image: 'https://placehold.co/600x400',
      status: ''
    },
    date: 'Yesterday',
    content: 'I have sent the package with...',
    unread: true
  },
  {
    id: 3,
    sender: {
      name: 'Alex',
      avatar: 'https://placehold.co/100x100'
    },
    item: {
      title: 'MacBook Pro | 13" - 2 Thunderbolt',
      price: 15000,
      image: 'https://placehold.co/600x400',
      status: ''
    },
    date: 'Nov 12',
    content: 'You: Haha yes. I\'ll come down'
  }
])

const newMessage = ref('')
const currentMessage = ref(messages.value[0])

const sendMessage = () => {
  if (newMessage.value.trim()) {
    // Send message logic would go here
    console.log('Sending message:', newMessage.value)
    newMessage.value = ''
  }
}
</script>

<template>
  <div class="messages-page">
    <!-- Message List Panel -->
    <div class="message-list">
      <div class="message-header">
        <h2>Messages</h2>
      </div>

      <div class="message-items">
        <div
          v-for="message in messages"
          :key="message.id"
          class="message-item"
          :class="{ active: message.id === currentMessage.id }"
          @click="currentMessage = message"
        >
          <div class="message-item-image">
            <img :src="message.item.image" alt="Product thumbnail" />
            <span v-if="message.unread" class="unread-badge">1</span>
          </div>
          <div class="message-item-content">
            <div class="message-item-title">{{ message.item.title }}</div>
            <div class="message-item-preview">
              {{ message.content }}
            </div>
            <div class="message-item-meta">
              <span class="status-badge">{{ message.item.status }}</span>
              <span class="message-date">{{ message.date }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Message Detail Panel -->
<div class="message-detail">
  

   
  
 
  
</div>
  </div>
</template>

