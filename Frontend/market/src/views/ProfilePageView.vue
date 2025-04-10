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

// File input reference
const fileInput = ref(null)

// Edit mode state
const editMode = ref(false)

// Temporary user data for editing
const editedUser = ref({...user.value})

// Description character limit
const descriptionLimit = 500
const remainingChars = ref(descriptionLimit)

const updateRemainingChars = () => {
  remainingChars.value = descriptionLimit - editedUser.value.description.length
}

// Handle description update
const handleDescriptionChange = (event) => {
  editedUser.value.description = event.target.value
  updateRemainingChars()
}

// Toggle edit mode
const toggleEditMode = () => {
  if (editMode.value) {
    // Save changes
    user.value = {...editedUser.value}
    console.log('Profile updated with data:', user.value)
    // In a real app, we would send this to an API
    alert('Profile updated successfully!')
  } else {
    // Enter edit mode - create a copy of user data for editing
    editedUser.value = JSON.parse(JSON.stringify(user.value))
  }
  
  editMode.value = !editMode.value
}

// Handle profile image change
const triggerFileInput = () => {
  fileInput.value.click()
}

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    // In a real app, we would upload this file to a server
    // For now, we'll just log it
    console.log('Selected file:', file.name)
    alert(`Image '${file.name}' selected. In a real app, this would be uploaded.`)
  }
}
</script>

<template>
  <div class="profile-page">
    <h1>Hello {{ user.displayName }}!</h1>
    
    <p class="profile-intro">
      Your profile on our marketplace is compiled from information gathered from your accounts. 
      The more information you add here, the better we can customize the experience for you. 
    </p>

    <div class="edit-panel">
      <button 
        @click="toggleEditMode" 
        class="edit-button"
      >
        {{ editMode ? 'Save Changes' : 'Edit Profile' }}
      </button>
    </div>

    <section class="profile-section">
      <h2>Profile Information</h2>
      
      <div class="profile-field">
        <div class="profile-avatar">
          <div class="avatar-placeholder"></div>
          <input 
            type="file" 
            ref="fileInput" 
            style="display:none" 
            accept="image/*" 
            @change="handleFileChange"
          />
          <button class="change-image-button" @click="triggerFileInput">
            <span class="icon">📷</span> Change Image
          </button>
        </div>
        
        <div class="profile-content">
          <h3>About Yourself</h3>
          <textarea 
            v-if="editMode"
            v-model="editedUser.description" 
            placeholder="Write a bit about yourself, so others know who they're dealing with."
            @input="handleDescriptionChange"
          ></textarea>
          <p v-else class="description-text">
            {{ user.description || 'No description added yet.' }}
          </p>
          <p v-if="editMode" class="character-limit">
            The description cannot contain phone numbers, email addresses, or links. Maximum
            {{ descriptionLimit }} characters.
          </p>
        </div>
      </div>
    </section>

    <section class="profile-section">  
      <div class="profile-field">
        <div class="field-label">Email:</div>
        <div class="field-value" v-if="!editMode">{{ user.email }}</div>
        <input v-else type="email" v-model="editedUser.email" class="field-input" />
      </div>
      
      <div class="profile-field">
        <div class="field-label">Password:</div>
        <div class="field-value" v-if="!editMode">{{ user.password }}</div>
        <input v-else type="password" v-model="editedUser.password" class="field-input" />
      </div>
      
      <div class="profile-field">
        <div class="field-label">Mobile Number:</div>
        <div class="field-value" v-if="!editMode">{{ user.phone }}</div>
        <input v-else type="tel" v-model="editedUser.phone" class="field-input" />
      </div>
      
      <div class="profile-field section-divider">
        <div class="field-label">About You</div>
      </div>
      
      <div class="profile-field">
        <div class="field-label">Name:</div>
        <div class="field-value" v-if="!editMode">{{ user.name }}</div>
        <input v-else type="text" v-model="editedUser.name" class="field-input" />
      </div>
      
      <div class="profile-field">
        <div class="field-label">Display Name:</div>
        <div class="field-value" v-if="!editMode">{{ user.displayName }}</div>
        <input v-else type="text" v-model="editedUser.displayName" class="field-input" />
      </div>
      
      <div class="profile-field">
        <div class="field-label">Birth Year:</div>
        <div class="field-value" v-if="!editMode">{{ user.birthYear }}</div>
        <input v-else type="text" v-model="editedUser.birthYear" class="field-input" />
      </div>
      
      <div class="profile-field">
        <div class="field-label">Gender:</div>
        <div class="field-value" v-if="!editMode">{{ user.gender || '-' }}</div>
        <select v-else v-model="editedUser.gender" class="field-input">
          <option value="">Not specified</option>
          <option value="male">Male</option>
          <option value="female">Female</option>
          <option value="other">Other</option>
        </select>
      </div>
      
      <div class="profile-field section-divider">
        <div class="field-label">Address</div>
      </div>
      
      <div class="profile-field">
        <div class="field-label">Street Address:</div>
        <div class="field-value" v-if="!editMode">{{ user.address.street || '-' }}</div>
        <input v-else type="text" v-model="editedUser.address.street" class="field-input" />
      </div>
      
      <div class="profile-field">
        <div class="field-label">Postal Code:</div>
        <div class="field-value" v-if="!editMode">{{ user.address.postalCode || '-' }}</div>
        <input v-else type="text" v-model="editedUser.address.postalCode" class="field-input" />
      </div>
      
      <div class="profile-field">
        <div class="field-label">City:</div>
        <div class="field-value" v-if="!editMode">{{ user.address.city || '-' }}</div>
        <input v-else type="text" v-model="editedUser.address.city" class="field-input" />
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
  margin-bottom: 1rem;
  line-height: 1.5;
}

.link {
  color: var(--button-background);
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

.edit-panel {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 1rem;
}

.edit-button {
  background-color: var(--button-background);
  color: var(--button-text-color);
  border: none;
  border-radius: 4px;
  padding: 0.8rem 1.5rem;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s;
}

.edit-button:hover {
  background-color: var(--button-hover-background);
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

.field-input {
  flex: 1;
  padding: 0.6rem;
  border-radius: 4px;
  border: 1px solid rgba(123, 164, 102, 0.3);
  background-color: var(--background-color);
  color: var(--text-color);
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

.change-image-button {
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

.change-image-button:hover {
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

.description-text {
  padding: 0.8rem;
  background-color: var(--background-color);
  border-radius: 4px;
  min-height: 60px;
  color: var(--text-color);
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
  
  .field-input {
    width: 100%;
  }
}
</style>