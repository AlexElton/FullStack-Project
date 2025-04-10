// src/composables/messageStore.js
import { ref, computed } from 'vue'

// Create a single instance of the store that will be shared across components
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
    read: false
  },
  {
    id: 2,
    sender: {
      name: 'Antoni',
      avatar: 'https://placehold.co/100x100',
      verified: true,
      memberSince: '2018',
      rating: 4.8,
      reviews: 23
    },
    item: {
      title: 'NorthWave Cycling Shoes',
      price: 500,
      image: 'https://placehold.co/600x400',
      status: 'Available'
    },
    date: 'Yesterday',
    content: 'I have sent the package with PostNord. You should receive it within 2-3 business days.',
    read: false
  },
  {
    id: 3,
    sender: {
      name: 'Alex',
      avatar: 'https://placehold.co/100x100',
      verified: false,
      memberSince: '2020'
    },
    item: {
      title: 'MacBook Pro | 13" - 2 Thunderbolt',
      price: 15000,
      image: 'https://placehold.co/600x400',
      status: 'Available'
    },
    date: 'Nov 12',
    content: 'You: Haha yes. I\'ll come down',
    read: false
  },
  {
    id: 4,
    sender: {
      name: 'Maria',
      avatar: 'https://placehold.co/100x100',
      verified: true,
      memberSince: '2016',
      rating: 5.0,
      reviews: 42
    },
    item: {
      title: 'iPhone 13 Pro Max',
      price: 12000,
      image: 'https://placehold.co/600x400',
      status: 'Available'
    },
    date: 'Oct 28',
    content: 'Is the phone still available? I\'m interested in buying it.',
    read: true
  },
  {
    id: 5,
    sender: {
      name: 'Erik',
      avatar: 'https://placehold.co/100x100',
      verified: true,
      memberSince: '2015',
      rating: 4.7,
      reviews: 56
    },
    item: {
      title: 'Sony WH-1000XM4 Headphones',
      price: 2800,
      image: 'https://placehold.co/600x400',
      status: 'Available'
    },
    date: 'Oct 15',
    content: 'Would you be willing to lower the price to 2500 NOK?',
    read: true
  },
  {
    id: 6,
    sender: {
      name: 'Sofia',
      avatar: 'https://placehold.co/100x100',
      verified: false,
      memberSince: '2021'
    },
    item: {
      title: 'Nintendo Switch OLED',
      price: 3500,
      image: 'https://placehold.co/600x400',
      status: 'Available'
    },
    date: 'Oct 10',
    content: 'I can pick it up tomorrow if that works for you.',
    read: false
  }
])

// Move this outside the function to create a singleton store
const unreadCount = computed(() => {
  return messages.value.filter(message => !message.read).length
})

// Function to mark message as read
const markAsRead = (messageId) => {
  const messageIndex = messages.value.findIndex(msg => msg.id === messageId)
  if (messageIndex !== -1) {
    // Create a new array to trigger reactivity
    const updatedMessages = [...messages.value]
    updatedMessages[messageIndex] = {
      ...updatedMessages[messageIndex],
      read: true
    }
    messages.value = updatedMessages
  }
}

// Create a composable function that returns the same instance to all components
export function useMessageStore() {
  return {
    messages,
    unreadCount,
    markAsRead,
    getAllMessages: () => messages.value
  }
}