// src/composables/messageStore.js
import { ref, computed } from 'vue'

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
    unread: false
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
    content: 'You: Haha yes. I\'ll come down',
    unread: true
  },
  
])

// Compute the number of unread messages
const unreadCount = computed(() => {
  return messages.value.filter(message => message.unread).length
})

// Function to mark message as read
const markAsRead = (messageId) => {
  const message = messages.value.find(msg => msg.id === messageId)
  if (message && message.unread) {
    message.unread = false
  }
}

// Function to get all messages
const getAllMessages = () => {
  return messages.value
}

export { messages, unreadCount, markAsRead, getAllMessages }