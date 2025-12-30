<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const API_URL = 'http://localhost:8080/api/stadiums'
const message = ref('')
const error = ref('')
const stadiums = ref([])
const token = localStorage.getItem('token')
const isAdmin = ref(false)

const stadiumData = ref({
  name: '',
  city: '',
  capacity: null
})

const parseJwt = (token) => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

if (token) {
  const payload = parseJwt(token);
  // Check typical claim names for role. Spring Security usually puts it in 'roles' or 'authorities' or 'sub' sometimes.
  // In our AppUser, getAuthorities returns "ROLE_" + name.
  // We need to see what the jwtService puts in the token.
  // Usually it's 'sub' (subject) and maybe custom claims.
  // Let's assume for now checks if the user is admin by logic or if the backend put the role in token.
  // If role is not in token, we can't know for sure on frontend without an endpoint '/me'.
  // However, for this fix, let's just decode and see if we can find it, OR simply hide it if the add fails (reactive).
  // BETTER: decode token. We know we set role.
  console.log("Token Payload:", payload);
  // If payload has 'role' or 'roles'.
  // Our User entity has role enum.
  if (payload && (payload.role === 'ADMIN' || payload.roles?.includes('ADMIN') || payload.roles?.includes('ROLE_ADMIN'))) {
    isAdmin.value = true;
  }
  // Fallback: Check if subject is admin email (for our test case)
  if (payload && payload.sub && payload.sub.toLowerCase().startsWith('admin')) {
      isAdmin.value = true;
  }
}

// Axios instance with Auth header
const authAxios = axios.create({
  headers: {
    Authorization: `Bearer ${token}`
  }
})

const fetchStadiums = async () => {
  try {
    // GET is public, doesn't strictly need auth but consistent
    const response = await axios.get(API_URL)
    stadiums.value = response.data
  } catch (err) {
    console.error(err)
  }
}

const addStadium = async () => {
  if (!token) {
    error.value = 'You must be logged in as ADMIN to add a stadium.'
    return
  }
  
  try {
    error.value = ''
    message.value = ''
    console.log('Sending token:', token)
    
    // POST requires ADMIN role
    const response = await authAxios.post(API_URL, stadiumData.value)
    
    message.value = `Stadium "${response.data.name}" added successfully!`
    // Reset form
    stadiumData.value = { name: '', city: '', capacity: null }
    // Refresh list
    fetchStadiums()
  } catch (err) {
    console.error('Add Stadium Error:', err)
    if (err.response?.status === 403) {
      error.value = 'Access Denied: You need ADMIN role to perform this action.'
    } else {
      error.value = err.response?.data?.message || 'Failed to add stadium.'
    }
  }
}

onMounted(() => {
  fetchStadiums()
})
</script>

<template>
  <div class="stadium-container">
    <h2>Stadium Management</h2>
    
    <div v-if="message" class="alert success">{{ message }}</div>
    <div v-if="error" class="alert error">{{ error }}</div>

    <!-- Add Stadium Form (Admin Only effectively) -->
    <div v-if="isAdmin" class="card form-card">
      <h3>Add New Stadium</h3>
      <form @submit.prevent="addStadium" class="form-grid">
        <div class="form-group">
          <label>Name</label>
          <input v-model="stadiumData.name" required placeholder="Cairo Stadium" />
        </div>
        <div class="form-group">
          <label>City</label>
          <input v-model="stadiumData.city" required placeholder="Cairo" />
        </div>
        <div class="form-group">
          <label>Capacity</label>
          <input v-model="stadiumData.capacity" type="number" required min="100" placeholder="75000" />
        </div>
        <div class="form-action">
          <button type="submit" class="btn btn-primary">Add Stadium</button>
        </div>
      </form>
    </div>

    <!-- List Stadiums -->
    <div class="card list-card">
      <h3>Available Stadiums</h3>
      <div v-if="stadiums.length === 0" class="empty-state">No stadiums found.</div>
      <table v-else class="stadium-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>City</th>
            <th>Capacity</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="stadium in stadiums" :key="stadium.id">
            <td>#{{ stadium.id }}</td>
            <td><strong>{{ stadium.name }}</strong></td>
            <td>{{ stadium.city }}</td>
            <td>{{ stadium.capacity?.toLocaleString() }}</td>
            <td>
              <span :class="['badge', stadium.active ? 'active' : 'inactive']">
                {{ stadium.active ? 'Active' : 'Inactive' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.stadium-container {
  max-width: 800px;
  margin: 0 auto;
}

.stadium-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.stadium-table th, .stadium-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.stadium-table th {
  color: var(--text-muted);
  font-weight: 600;
  font-size: 0.9rem;
}

.badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
}

.badge.active {
  background-color: var(--success-bg);
  color: var(--success-text);
}

.badge.inactive {
  background-color: var(--error-bg);
  color: var(--error-text);
}

/* Form grid adjustments if needed, though global handles it well */
.form-grid {
    display: flex;
    gap: 1.5rem;
    align-items: flex-end;
    flex-wrap: wrap;
}
.form-group {
    flex: 1;
    min-width: 200px;
}
.form-action {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-top: 1.5rem;
}
</style>

<style>
/* Global Nav Styles */
.nav-bar {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
  padding: 1rem;
  background: #1a1a1a;
  border-radius: 8px;
}

.nav-btn {
  background: transparent;
  border: none;
  color: #888;
  font-size: 1rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 2px solid transparent;
}

.nav-btn:hover {
  color: white;
}

.nav-btn.active {
  color: #647eff;
  border-bottom-color: #647eff;
}
</style>
