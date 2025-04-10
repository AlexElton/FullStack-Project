<script setup>
import { ref, computed, onMounted } from 'vue'
import { useMessageStore } from '../composables/messageStore'
import StripeCheckout from '../components/StripeCheckout.vue'

const messageStore = useMessageStore()
const currentMessage = ref(null)
const newMessage = ref('')
const fileInput = ref(null)

// Get messages from the store
const messages = computed(() => messageStore.getAllMessages())

// Get unread count from the store
const unreadCount = computed(() => messageStore.unreadCount)

// Function to select a message
const selectMessage = (message) => {
  if (message) {
    currentMessage.value = message
    messageStore.markAsRead(message.id)
  }
}

// Initialize currentMessage when component is mounted
onMounted(() => {
  if (messages.value && messages.value.length > 0) {
    currentMessage.value = messages.value[0]
  }
})

// Function to send a new message
const sendMessage = () => {
  if (newMessage.value.trim()) {
    // Send message logic would go here
    console.log('Sending message:', newMessage.value)
    newMessage.value = ''
  }
}

// Function to trigger file picker
const triggerFilePicker = () => {
  fileInput.value?.click()
}

// Function to handle file change
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    console.log('Selected file:', file)
    // You can handle file upload logic here
  }
}
</script>

<template>
  <div class="messages-container">
    <div class="messages-sidebar">
      <h2>Messages ({{ unreadCount }} unread)</h2>
      <div class="messages-list">
        <div 
          v-for="message in messages" 
          :key="message.id"
          :class="['message-item', { 
            'active': currentMessage?.id === message.id,
            'unread': !message.read 
          }]"
          @click="selectMessage(message)"
        >
          <img :src="message.sender.avatar" :alt="message.sender.name" class="avatar">
          <div class="message-preview">
            <div class="sender-name">{{ message.sender.name }}</div>
            <div class="message-title">{{ message.item.title }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="message-content">
      <div v-if="currentMessage" class="message-details">
        <div class="sender-info">
          <img :src="currentMessage.sender.avatar" :alt="currentMessage.sender.name" class="avatar">
          <div class="sender-details">
            <h3>{{ currentMessage.sender.name }}</h3>
            <div class="sender-meta">
              <span v-if="currentMessage.sender.verified" class="verified">✓ Verified</span>
              <span>Member since {{ currentMessage.sender.memberSince }}</span>
              <span>Rating: {{ currentMessage.sender.rating }} ({{ currentMessage.sender.reviews }} reviews)</span>
            </div>
          </div>
        </div>

        <div class="item-info">
          <img :src="currentMessage.item.image" :alt="currentMessage.item.title" class="item-image">
          <div class="item-details">
            <h4>{{ currentMessage.item.title }}</h4>
            <div class="item-price">{{ currentMessage.item.price }} NOK</div>
            <div class="item-status">Status: {{ currentMessage.item.status }}</div>
          </div>
        </div>

        <div class="message-text">
          {{ currentMessage.content }}
        </div>

        <div class="message-date">
          {{ currentMessage.date }}
        </div>

        <StripeCheckout 
          :item-title="currentMessage.item.title"
          :item-price="currentMessage.item.price"
          :item-image="currentMessage.item.image"
        />
      </div>
      <div v-else class="no-message">
        Select a message to view
      </div>
    </div>
  </div>
</template>

<style scoped>
.messages-container {
  display: flex;
  height: calc(100vh - 60px);
  background-color: #f5f5f5;
}

.messages-sidebar {
  width: 300px;
  border-right: 1px solid #e0e0e0;
  background-color: white;
  overflow-y: auto;
}

.messages-sidebar h2 {
  padding: 1rem;
  margin: 0;
  border-bottom: 1px solid #e0e0e0;
}

.messages-list {
  padding: 0.5rem;
}

.message-item {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.message-item:hover {
  background-color: #f5f5f5;
}

.message-item.active {
  background-color: #e8f5e9;
}

.message-item.unread {
  background-color: #f3e5f5;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 0.75rem;
}

.message-preview {
  flex: 1;
  min-width: 0;
}

.sender-name {
  font-weight: bold;
  margin-bottom: 0.25rem;
}

.message-title {
  color: #666;
  font-size: 0.9rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.message-content {
  flex: 1;
  padding: 1rem;
  overflow-y: auto;
}

.message-details {
  background-color: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.sender-info {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
}

.sender-details {
  margin-left: 1rem;
}

.sender-meta {
  color: #666;
  font-size: 0.9rem;
}

.verified {
  color: #4CAF50;
  margin-right: 0.5rem;
}

.item-info {
  display: flex;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.item-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 1rem;
}

.item-details h4 {
  margin: 0 0 0.5rem 0;
}

.item-price {
  color: #2e7d32;
  font-weight: bold;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.item-status {
  color: #666;
  font-size: 0.9rem;
}

.message-text {
  margin: 1.5rem 0;
  line-height: 1.5;
}

.message-date {
  color: #666;
  font-size: 0.9rem;
  text-align: right;
}

.no-message {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
  font-size: 1.1rem;
}
</style>