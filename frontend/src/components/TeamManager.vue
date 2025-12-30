<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const API_URL = 'http://localhost:8080/api/teams'
const message = ref('')
const error = ref('')
const teams = ref([])
const token = localStorage.getItem('token')
const isAdmin = ref(false)

const teamData = ref({
  name: '',
  logoUrl: '',
  description: ''
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
  if (payload && (payload.role === 'ADMIN' || payload.roles?.includes('ADMIN') || payload.roles?.includes('ROLE_ADMIN'))) {
    isAdmin.value = true;
  }
  if (payload && payload.sub && payload.sub.toLowerCase().startsWith('admin')) {
      isAdmin.value = true;
  }
}

const authAxios = axios.create({
  headers: {
    Authorization: `Bearer ${token}`
  }
})

const fetchTeams = async () => {
  try {
    const response = await axios.get(API_URL)
    teams.value = response.data
  } catch (err) {
    console.error(err)
  }
}

const addTeam = async () => {
  if (!token) {
    error.value = 'You must be logged in as ADMIN to add a team.'
    return
  }
  
  try {
    error.value = ''
    message.value = ''
    
    const response = await authAxios.post(API_URL, teamData.value)
    
    message.value = `Team "${response.data.name}" added successfully!`
    teamData.value = { name: '', logoUrl: '', description: '' }
    fetchTeams()
  } catch (err) {
    console.error('Add Team Error:', err)
    if (err.response?.status === 403) {
      error.value = 'Access Denied: You need ADMIN role to perform this action.'
    } else {
      error.value = err.response?.data?.message || 'Failed to add team.'
    }
  }
}

onMounted(() => {
  fetchTeams()
})
</script>

<template>
  <div class="team-container">
    <h2>Team Management</h2>
    
    <div v-if="message" class="alert success">{{ message }}</div>
    <div v-if="error" class="alert error">{{ error }}</div>

    <div v-if="isAdmin" class="card form-card">
      <h3>Add New Team</h3>
      <form @submit.prevent="addTeam" class="form-grid">
        <div class="form-group">
          <label>Name</label>
          <input v-model="teamData.name" required placeholder="Al Ahly SC" />
        </div>
        <div class="form-group">
          <label>Logo URL</label>
          <input v-model="teamData.logoUrl" required placeholder="https://example.com/logo.png" />
        </div>
        <div class="form-group">
          <label>Description</label>
          <input v-model="teamData.description" placeholder="The Red Devils" />
        </div>
        <div class="form-action">
          <button type="submit" class="btn btn-primary">Add Team</button>
        </div>
      </form>
    </div>

    <div class="card list-card">
      <h3>Available Teams</h3>
      <div v-if="teams.length === 0" class="empty-state">No teams found.</div>
      <div v-else class="teams-grid">
        <div class="team-card" v-for="team in teams" :key="team.id">
            <div class="team-logo-wrapper">
                <img :src="team.logoUrl || '/placeholder.png'" 
                     @error="$event.target.src='https://placehold.co/100x100?text=No+Logo'" 
                     class="team-logo" 
                     alt="Team Logo" />
            </div>
            <h3>{{ team.name }}</h3>
            <p>{{ team.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.team-container {
  max-width: 900px;
  margin: 0 auto;
}

.teams-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 1.5rem;
  margin-top: 1rem;
}

.team-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  text-align: center;
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid var(--border-color);
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.team-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  border-color: var(--primary-color);
}

.team-logo {
  width: 100px;
  height: 100px;
  object-fit: contain;
  margin-bottom: 1rem;
  background: white;
  border-radius: 50%; /* If you want circular logos */
  padding: 5px;
  /* Optional: Add a subtle border to logo container */
  /* border: 1px solid var(--border-color); */
}

.team-info h4 {
  margin: 0.5rem 0;
  color: var(--secondary-color);
  font-size: 1.2rem;
  font-weight: 700;
}

.team-info p {
  margin: 0;
  color: var(--text-muted);
  font-size: 0.9rem;
  line-height: 1.4;
}

/* Ensure form-grid matches global if defined there,*/
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
