<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const isLogin = ref(true)
const message = ref('')
const error = ref('')
const token = ref(localStorage.getItem('token') || '')

// Login Data
const loginData = ref({
  email: '',
  password: ''
})

// Register Data
const registerData = ref({
  username: '',
  email: '',
  fanId: '',
  password: ''
})

const API_URL = 'http://localhost:8080/api/users'

const toggleMode = () => {
  isLogin.value = !isLogin.value
  message.value = ''
  error.value = ''
}

const handleRegister = async () => {
  try {
    error.value = ''
    message.value = ''
    const response = await axios.post(`${API_URL}/register`, registerData.value)
    message.value = 'Registration successful! Please login.'
    isLogin.value = true // Switch to login
  } catch (err) {
    console.error('Full Error:', err)
    error.value = err.response?.data?.message || err.message || 'Registration failed'
  }
}

const emit = defineEmits(['login-success', 'logout'])

const handleLogin = async () => {
  try {
    error.value = ''
    message.value = ''
    const response = await axios.post(`${API_URL}/login`, loginData.value)
    
    const data = response.data
    const receivedToken = data.token || (typeof data === 'string' ? data : null) || ''
    
    if (receivedToken) {
        token.value = receivedToken
        localStorage.setItem('token', receivedToken)
        message.value = 'Login successful!'
        
        // Notify parent if needed (though router logic handles state)
        emit('login-success')
        
        // Force reload to update App.vue state (simplest way to sync isLoggedIn in App.vue)
        // Or better: Since we are SPA, simply router.push and let App.vue update.
        // However, App.vue 'isLoggedIn' ref is initialized on mount. It won't update automatically unless we watch route or use a store.
        // For now, let's redirect. Ideally App.vue should watch for route changes or use a simple event bus/store.
        
        // Quick fix for App.vue state sync: 
        window.location.href = '/matches' 
    } else {
        throw new Error("No token received")
    }
  } catch (err) {
    console.error(err)
    error.value = err.response?.data?.message || 'Login failed'
  }
}

const logout = () => {
  token.value = ''
  localStorage.removeItem('token')
  message.value = 'Logged out.'
  emit('logout')
}

// Redirect if already logged in when visiting this route
onMounted(() => {
    if (localStorage.getItem('token')) {
        router.push('/matches')
    }
})
</script>

<template>
  <div class="auth-container">
    <!-- Removed 'logged-in-view' loop. If logged in, we redirect. -->
    
    <div class="auth-forms">
      <div class="tabs">
        <button 
          :class="{ active: isLogin }" 
          @click="toggleMode">Login</button>
        <button 
          :class="{ active: !isLogin }" 
          @click="toggleMode">Register</button>
      </div>

      <div v-if="message" class="alert success">{{ message }}</div>
      <div v-if="error" class="alert error">{{ error }}</div>

      <!-- Login Form -->
      <form v-if="isLogin" @submit.prevent="handleLogin" class="form">
        <div class="form-group">
          <label>Email</label>
          <input v-model="loginData.email" type="email" required placeholder="admin@example.com" />
        </div>
        <div class="form-group">
          <label>Password</label>
          <input v-model="loginData.password" type="password" required placeholder="********" />
        </div>
        <button type="submit" class="btn btn-primary">Login</button>
      </form>

      <!-- Register Form -->
      <form v-else @submit.prevent="handleRegister" class="form">
        <div class="form-group">
          <label>Username</label>
          <input v-model="registerData.username" type="text" required placeholder="JohnDoe" />
        </div>
        <div class="form-group">
          <label>Email</label>
          <input v-model="registerData.email" type="email" required placeholder="john@example.com" />
        </div>
        <div class="form-group">
          <label>Fan ID</label>
          <input v-model="registerData.fanId" type="text" required placeholder="FAN-12345" />
        </div>
        <div class="form-group">
          <label>Password</label>
          <input v-model="registerData.password" type="password" required placeholder="********" />
        </div>
        <button type="submit" class="btn btn-primary">Register</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  max-width: 450px;
  margin: 4rem auto;
  padding: 2.5rem;
  background: var(--card-bg);
  border-radius: 12px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  border: 1px solid var(--border-color);
}

.tabs {
  display: flex;
  margin-bottom: 2rem;
  border-bottom: 2px solid var(--border-color);
}

.tabs button {
  flex: 1;
  background: none;
  border: none;
  padding: 1rem;
  color: var(--text-muted);
  cursor: pointer;
  font-size: 1rem;
  font-weight: 600;
  transition: all 0.2s;
}

.tabs button.active {
  color: var(--primary-color);
  border-bottom: 2px solid var(--primary-color);
  margin-bottom: -2px;
}

.form-group {
  margin-bottom: 1.5rem;
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--text-main);
  font-weight: 500;
}

.form-group input {
  width: 100%;
}

.btn {
  width: 100%;
}

.token-display pre {
  background: #f3f4f6;
  padding: 1rem;
  border-radius: 4px;
  overflow-x: auto;
  font-size: 0.85rem;
  color: var(--text-muted);
  border: 1px solid var(--border-color);
}

.logged-in-view {
  text-align: center;
}
</style>
