<script setup>
import { ref, computed } from 'vue'
import { messages } from '@/composables/messageStore'
import StripeCheckout from '@/components/StripeCheckout.vue'

// Compute the number of unread messages
const unreadCount = computed(() => {
  return messages.value.filter(message => message.unread).length
})

const newMessage = ref('')
const currentMessage = ref(messages.value[0])

const sendMessage = () => {
  if (newMessage.value.trim()) {
    // Send message logic would go here
    console.log('Sending message:', newMessage.value)
    newMessage.value = ''
  }
}

// Function to mark message as read when clicked
const selectMessage = (message) => {
  if (message.unread) {
    message.unread = false
  }
  currentMessage.value = message
}

const fileInput = ref(null)

const triggerFilePicker = () => {
  fileInput.value?.click()
}

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    console.log('Selected file:', file)
    // You can handle file upload logic here
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
          :class="{ active: message.id === currentMessage.id, unread: message.unread }"
          @click="selectMessage(message)"
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
          <StripeCheckout />
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
        <div class="input-top-row">
            <button class="attachment-button" @click="triggerFilePicker">📎</button>

            <input 
            type="text" 
            v-model="newMessage" 
            placeholder="Write a message..." 
            @keyup.enter="sendMessage"
            class="text-input"
            />

            <button v-if="newMessage" class="send-button" @click="sendMessage">➡️</button>
        </div>

        <div class="keyboard-hint">Shift+Enter for linjeskift</div>

        <input type="file" ref="fileInput" style="display: none;" @change="handleFileChange" />
        </div>




    </div>
  </div>
</template>

<style scoped>
/* General layout for messages page */
.messages-page {
  display: grid;
  grid-template-columns: 350px 1fr;
  gap: 1rem;
  height: calc(100vh - 100px);
  max-width: 100%;
  margin: 0 auto;
}

.message-list {
  border-right: 1px solid #e0e0e0;
  overflow-y: auto;
}

.message-header {
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
}

.message-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: var(--text-color);
}

.message-items {
  display: flex;
  flex-direction: column;
}

.message-item {
  display: flex;
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.message-item:hover {
  background-color: #f5f5f5;
}

.message-item.active {
  background-color: var(--card-background);
  border-left: 4px solid var(--primary-color);
}

/* Highlight unread messages */
.message-item.unread {
  background-color: rgba(30, 136, 229, 0.05);
}

.message-item-image {
  position: relative;
  width: 60px;
  height: 60px;
  margin-right: 1rem;
}

.message-item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.unread-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: #1e88e5;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
}

.message-item-content {
  flex: 1;
  overflow: hidden;
}

.message-item-title {
  font-weight: bold;
  margin-bottom: 0.25rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.message-item-preview {
  font-size: 0.9rem;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 0.5rem;
}

.message-item-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.8rem;
}

.status-badge {
  background-color: #e0e0e0;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 0.8rem;
}

.message-date {
  color: #888;
}

/* Message Detail Styles */
.message-detail {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.message-detail-header {
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
}

.item-info {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 1rem;
}

.price {
  font-weight: bold;
  margin: 0.25rem 0;
}

.sender-info {
  display: flex;
  align-items: center;
  padding: 1rem 0;
  border-top: 1px solid #e0e0e0;
  border-bottom: 1px solid #e0e0e0;
}

.sender-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 1rem;
}

.sender-details {
  flex: 1;
}

.sender-name {
  font-weight: bold;
  margin-bottom: 0.25rem;
}

.verified-badge {
  display: flex;
  align-items: center;
  font-size: 0.8rem;
  color: #4caf50;
  margin-bottom: 0.25rem;
}

.verified-badge span:first-child {
  margin-right: 0.5rem;
}

.rating {
  font-size: 0.9rem;
}

.message-content {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
  background-color: #f5f5f5;
}

.message-bubble {
  background-color: white;
  padding: 1rem;
  border-radius: 8px;
  max-width: 80%;
  margin-bottom: 1rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-time {
  font-size: 0.8rem;
  color: #888;
  margin-bottom: 0.5rem;
}

.message-text {
  margin-bottom: 0.5rem;
}

.delivery-info {
  padding: 0.5rem 0;
  border-top: 1px solid #e0e0e0;
  margin-top: 0.5rem;
  font-size: 0.9rem;
}

.action-button {
  margin-top: 1rem;
  text-align: center;
}

.status-link {
  background-color: transparent;
  color: var(--link-color);
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  font-weight: bold;
}


.message-input {
  display: flex;
  flex-direction: column;
  padding: 1rem;
  border-top: 1px solid #e0e0e0;
  background-color: #fff;
  gap: 0.5rem;
}

.input-top-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.text-input {
  flex: 1;
  padding: 0.5rem;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.keyboard-hint {
  font-size: 0.8rem;
  color: #888;
  padding-left: 2.2rem; /* aligns with input left padding including attachment icon */
}

.attachment-button {
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  color: #555;
}

.input-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.input-area input {
  width: 100%;
  padding: 0.8rem 1rem;
  border: 1px solid #ccc;
  border-radius: 30px;
  font-size: 16px;
  background-color: #f4f4f4;
}

.send-button {
  background-color: #1a73e8;
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 18px;
  width: 40px;
  height: 40px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.send-button:hover {
  background-color: #1558c0;
}

</style>