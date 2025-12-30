<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import UserAuth from './components/UserAuth.vue'

const router = useRouter()
const route = useRoute()
const isLoggedIn = ref(!!localStorage.getItem('token'))
const isAdmin = ref(false)

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

const checkAuth = () => {
  const token = localStorage.getItem('token')
  isLoggedIn.value = !!token
  isAdmin.value = false
  
  if (token) {
    const payload = parseJwt(token)
    if (payload && (payload.role === 'ADMIN' || payload.roles?.includes('ADMIN') || payload.roles?.includes('ROLE_ADMIN') || (payload.sub && payload.sub.toLowerCase().startsWith('admin')))) {
      isAdmin.value = true
    }
  }
}

const handleLoginSuccess = () => {
  checkAuth()
  router.push('/matches')
}

const handleLogout = () => {
  localStorage.removeItem('token')
  checkAuth()
  router.push('/login')
}

// Check auth on mount
onMounted(() => {
  checkAuth()
  if (!isLoggedIn.value) {
    router.push('/login')
  }
})

// Watch route to re-check auth (e.g. if token expired or cleared elsewhere, or mostly just to keep state sync)
watch(route, () => {
    checkAuth()
})
</script>

<template>
  <div class="app-layout">
    <!-- Navbar -->
    <header class="navbar" v-if="isLoggedIn">
      <div class="navbar-container">
        <div class="brand">
          <span class="logo-icon">🎟️</span>
          <span class="brand-name">Tazkarti</span>
        </div>
        <div class="user-actions">
           <button @click="handleLogout" class="btn-logout">Logout</button>
        </div>
      </div>
    </header>
    
    <main>
      <!-- Show Login Component if on Login Route (Manual handling for layout) OR if not logged in -->
      <!-- But with Router, we usually let RouterView handle everything. -->
      <!-- However, UserAuth was a component. We made it a route too. -->
      
      <div v-if="isLoggedIn" class="dashboard-container">
        <!-- Navigation Tabs (using router-link for "Coute" system) -->
        <nav class="nav-tabs">
          <router-link to="/matches" class="tab-btn" active-class="active">
             Matches
          </router-link>
          <router-link to="/my-tickets" class="tab-btn" active-class="active">
             My Tickets
          </router-link>
          
          <!-- Admin Only Tabs -->
          <router-link v-if="isAdmin" to="/teams" class="tab-btn" active-class="active">
             Teams
          </router-link>
          <router-link v-if="isAdmin" to="/stadiums" class="tab-btn" active-class="active">
             Stadiums
          </router-link>
        </nav>

        <!-- Dynamic Content from Router -->
        <div class="content-area">
           <router-view />
        </div>
      </div>

      <!-- If not logged in, Router View will show UserAuth component anyway via /login route -->
      <div v-else class="login-container">
         <router-view /> 
      </div>
    </main>
    
    <footer>
      <p>&copy; 2024 Tazkarti Clone. All rights reserved.</p>
    </footer>
  </div>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Navbar */
.navbar {
  background-color: #fff;
  border-bottom: 1px solid var(--border-color);
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--primary-color);
}

.logo-icon {
  font-size: 1.8rem;
}

.user-actions {
    display: flex;
    gap: 1rem;
    align-items: center;
}

.btn-link {
    background: transparent;
    border: none;
    color: var(--primary-color);
    font-weight: 600;
    cursor: pointer;
    font-size: 1rem;
}

.btn-link:hover {
    text-decoration: underline;
}

.btn-logout {
    background: transparent;
    border: 1px solid var(--danger-color);
    color: var(--danger-color);
    padding: 0.5rem 1rem;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 600;
}

.btn-logout:hover {
    background: var(--danger-color);
    color: white;
}

/* Main Content */
main {
  flex: 1;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

/* Tabs */
.nav-tabs {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin: 2rem 0;
  background: white;
  padding: 0.5rem;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  display: inline-flex; /* Shrink to fit */
  border: 1px solid var(--border-color);
}

.dashboard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content-area {
  width: 100%;
}

.tab-btn {
  background: transparent;
  border: none;
  font-size: 1.1rem; /* Kept your requested size */
  font-weight: 700;
  color: var(--text-muted);
  padding: 1rem 2rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none !important; /* Force remove underline */
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.tab-btn:hover {
  background-color: #f3f4f6;
  color: var(--text-main);
}

.tab-btn.active {
  background-color: var(--primary-color);
  color: white;
  box-shadow: 0 2px 4px rgba(0, 166, 81, 0.2);
}

footer {
  text-align: center;
  padding: 2rem;
  color: var(--text-muted);
  border-top: 1px solid var(--border-color);
  background: white;
  font-size: 0.9rem;
}
</style>
