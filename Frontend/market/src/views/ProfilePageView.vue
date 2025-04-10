<script setup>
import { ref } from 'vue'

// User profile data
const user = ref({
  name: 'Alexander Owren Elton',
  displayName: 'Alex Elton',
  email: 'alexander.owren.elton@hotmail.com',
  password: '********',
  phone: '+4747672818',
  birthYear: '2003',
  gender: '',
  address: {
    street: '',
    postalCode: '',
    city: ''
  },
  description: ''
})

// Description character limit
const descriptionLimit = 500
const remainingChars = ref(descriptionLimit)

const updateRemainingChars = () => {
  remainingChars.value = descriptionLimit - user.value.description.length
}

// Handle description update
const handleDescriptionChange = (event) => {
  user.value.description = event.target.value
  updateRemainingChars()
}

// Handle profile update
const updateProfile = () => {
  console.log('Updating profile with data:', user.value)
  // In a real app, we would send this to an API
  alert('Profile updated successfully!')
}

// Edit field functions
const editField = (field) => {
  console.log('Editing field:', field)
  // This would open an edit dialog in a real implementation
}
</script>

<template>
    <div class="profile-page">
      <h1>Hi {{ user.displayName }}!</h1>
  
      <section class="profile-section">      
        <div class="profile-field">
          <div class="profile-avatar">
            <div class="avatar-placeholder"></div>
            <button class="edit-button">
              <span class="icon">📷</span> Edit
            </button>
          </div>
          
          <div class="profile-content">
            <h3>Tell us about yourself</h3>
            <textarea 
              v-model="user.description" 
              placeholder="Feel free to write a bit about yourself so others know who they're trading with."
              @input="handleDescriptionChange"
            ></textarea>
            <p class="character-limit">
              The description must not contain phone numbers, email addresses, or links. Maximum
              {{ descriptionLimit }} characters.
            </p>
          </div>
        </div>
      </section>
  
      <section class="profile-section">
        <h2>Schibsted Marketplaces Settings</h2>
        
        <div class="profile-field">
          <div class="field-label">Email:</div>
          <div class="field-value">{{ user.email }}</div>
          <button class="icon-button" @click="editField('email')">
            <span class="icon">✏️</span>
          </button>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Password:</div>
          <div class="field-value">{{ user.password }}</div>
          <button class="icon-button" @click="editField('password')">
            <span class="icon">✏️</span>
          </button>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Phone number:</div>
          <div class="field-value">{{ user.phone }}</div>
          <button class="icon-button" @click="editField('phone')">
            <span class="icon">✏️</span>
          </button>
        </div>
        
        <div class="profile-field section-divider">
          <div class="field-label">About you</div>
          <button class="icon-button" @click="editField('aboutMe')">
            <span class="icon">✏️</span>
          </button>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Full name:</div>
          <div class="field-value">{{ user.name }}</div>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Display name:</div>
          <div class="field-value">{{ user.displayName }}</div>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Birth year:</div>
          <div class="field-value">{{ user.birthYear }}</div>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Gender:</div>
          <div class="field-value">{{ user.gender || '-' }}</div>
        </div>
        
        <div class="profile-field section-divider">
          <div class="field-label">Address</div>
          <button class="icon-button" @click="editField('address')">
            <span class="icon">✏️</span>
          </button>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Street address:</div>
          <div class="field-value">{{ user.address.street || '-' }}</div>
        </div>
        
        <div class="profile-field">
          <div class="field-label">Postal code:</div>
          <div class="field-value">{{ user.address.postalCode || '-' }}</div>
        </div>
        
        <div class="profile-field">
          <div class="field-label">City:</div>
          <div class="field-value">{{ user.address.city || '-' }}</div>
        </div>
      </section>
    </div>
  </template>  

<style scoped>
.profile-page {
  max-width: 100%;
  margin: 0 auto;
  padding: 1rem;
  color: var(--text-color);
}

h1 {
  font-size: 2rem;
  margin-bottom: 1rem;
  color: var(--text-color);
}

.profile-intro {
  margin-bottom: 2rem;
  line-height: 1.5;
}

.link {
  color: var(--button-background);
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

.profile-section {
  background-color: var(--card-background);
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 4px rgba(123, 164, 102, 0.1);
}

h2 {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  color: var(--text-color);
}

.profile-field {
  display: flex;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid rgba(123, 164, 102, 0.1);
  position: relative;
}

.profile-field:last-child {
  border-bottom: none;
}

.field-label {
  flex: 0 0 150px;
  font-weight: bold;
}

.field-value {
  flex: 1;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.icon-button:hover {
  opacity: 1;
}

.section-divider {
  border-top: 2px solid rgba(123, 164, 102, 0.1);
  padding-top: 1.5rem;
  margin-top: 0.5rem;
}

.profile-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 2rem;
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #e0e0e0;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  color: #999;
}

.edit-button {
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.edit-button:hover {
  background-color: var(--button-hover-background);
}

.profile-content {
  flex: 1;
}

h3 {
  font-size: 1.1rem;
  margin-bottom: 0.75rem;
  color: var(--text-color);
}

textarea {
  width: 100%;
  height: 100px;
  padding: 0.8rem;
  box-shadow: 0 2px 4px rgba(123, 164, 102, 0.1);
  border-radius: 4px;
  background-color: var(--background-color);
  color: var(--text-color);
  border: 1px solid rgba(123, 164, 102, 0.2);
  resize: vertical;
}

.character-limit {
  font-size: 0.8rem;
  color: #777;
  margin-top: 0.5rem;
}

@media (max-width: 768px) {
  .profile-field {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .field-label {
    margin-bottom: 0.5rem;
  }
  
  .profile-avatar {
    margin-right: 0;
    margin-bottom: 1.5rem;
  }
}
</style>