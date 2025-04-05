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
  <div class="message-detail-header">
    <div class="item-info">
      <img :src="currentMessage.item.image" alt="Product" class="item-image" />
      <div>
        <h3>{{ currentMessage.item.title }}</h3>
        <div class="price">{{ currentMessage.item.price }} NOK</div>
        
        <!-- Only show the status badge if status is 'Sold' -->
        <span v-if="currentMessage.item.status === 'Sold'" class="status-badge">
          {{ currentMessage.item.status }}
        </span>
      </div>
    </div>

    <div class="sender-info">
      <img :src="currentMessage.sender.avatar" alt="Sender avatar" class="sender-avatar" />
      <div class="sender-details">
        <div class="sender-name">{{ currentMessage.sender.name }}</div>
        <div v-if="currentMessage.sender.verified" class="verified-badge">
          <span>Verified</span>
          <span>On FINN since {{ currentMessage.sender.memberSince }}</span>
        </div>
        <div v-if="currentMessage.sender.rating" class="rating">
          {{ currentMessage.sender.rating }}
          <span>{{ currentMessage.sender.reviews }} reviews</span>
        </div>
      </div>
      <button class="status-button">Show profile</button>
    </div>
  </div>
  
  <div class="message-content">
    <div class="message-bubble">
      <div class="message-time">{{ currentMessage.date }}</div>
      <div class="message-text">{{ currentMessage.content }}</div>
      <div class="action-button" v-if="currentMessage.item.status === 'Sold'">
        <button class="status-link">Give {{currentMessage.sender.name}} a rating</button>
      </div>
    </div>
  </div>
  <div class="message-input">
        <button class="attachment-button">📎</button>
        <input 
          type="text" 
          v-model="newMessage" 
          placeholder="Write a message..." 
          @keyup.enter="sendMessage"
        />
        <div v-if="newMessage" class="send-button">
          <button class="send-button" @click="sendMessage">➡️</button>
        </div>
        <div class="keyboard-hint">Shift+Enter for next line</div>
    </div>
</div>
  </div>
</template>

