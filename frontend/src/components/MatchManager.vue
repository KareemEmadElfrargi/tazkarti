<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const API_URL_MATCHES = 'http://localhost:8080/api/matches'
const API_URL_TEAMS = 'http://localhost:8080/api/teams'
const API_URL_STADIUMS = 'http://localhost:8080/api/stadiums'

const message = ref('')
const error = ref('')
const matches = ref([])
const teams = ref([])
const stadiums = ref([])

const token = localStorage.getItem('token')
const isAdmin = ref(false)

const matchData = ref({
  homeTeamId: null,
  awayTeamId: null,
  stadiumId: null,
  matchTime: '',
  ticketPrice: 50
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

const fetchData = async () => {
    try {
        const [mRes, tRes, sRes] = await Promise.all([
            axios.get(API_URL_MATCHES),
            axios.get(API_URL_TEAMS),
            axios.get(API_URL_STADIUMS)
        ])
        matches.value = mRes.data
        teams.value = tRes.data
        stadiums.value = sRes.data
    } catch (err) {
        console.error("Error fetching data", err)
    }
}

const getTeamName = (id) => teams.value.find(t => t.id === id)?.name || 'Unknown'
const getTeamLogo = (id) => teams.value.find(t => t.id === id)?.logoUrl || 'https://via.placeholder.com/60?text=Points'
const getStadiumName = (id) => stadiums.value.find(s => s.id === id)?.name || 'Unknown'

const addMatch = async () => {
  if (!token) {
    error.value = 'You must be logged in as ADMIN to add a match.'
    return
  }
  
  if (matchData.value.homeTeamId === matchData.value.awayTeamId) {
      error.value = "Home and Away teams must be different!"
      return
  }

  try {
    error.value = ''
    message.value = ''
    
    // Ensure datetime format matches LocalDateTime default expectation (ISO-8601 without Z sometimes or simply ISO)
    // input type="datetime-local" return YYYY-MM-DDTHH:mm
    // Backend Java LocalDateTime expects '2023-12-30T10:00:00'
    const payload = { ...matchData.value }
    // Add seconds if missing? usually localdatetime parser handles it.
    
    const response = await authAxios.post(API_URL_MATCHES, payload)
    
    message.value = `Match added successfully!`
    matchData.value = { homeTeamId: null, awayTeamId: null, stadiumId: null, matchTime: '', ticketPrice: 50 }
    fetchData()
  } catch (err) {
    console.error('Add Match Error:', err)
    error.value = err.response?.data?.message || 'Failed to add match.'
  }
}

const bookTicket = async (matchId) => {
    if (!token) {
        error.value = 'You must be logged in to book a ticket.'
        return
    }

    try {
        message.value = ''
        error.value = ''
        // API requires BookingRequest { matchId: number }
        const response = await authAxios.post('http://localhost:8080/api/tickets', { matchId })
        message.value = `Ticket Booked Successfully! Code: ${response.data.ticketCode}`
    } catch (err) {
        console.error("Booking Error", err)
        error.value = err.response?.data?.message || 'Booking failed'
    }
}

// Format date for display
const formatDate = (dateStr) => {
    if (!dateStr) return ''
    return new Date(dateStr).toLocaleString()
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="match-container">
    <h2>Match Center</h2>
    
    <div v-if="message" class="alert success">{{ message }}</div>
    <div v-if="error" class="alert error">{{ error }}</div>

    <div v-if="isAdmin" class="card form-card">
      <h3>Schedule New Match</h3>
      <form @submit.prevent="addMatch" class="form-grid">
        <div class="form-group">
          <select v-model="matchData.homeTeamId" required>
              <option disabled value="null">Select Home Team</option>
              <option v-for="team in teams" :key="team.id" :value="team.id">
                  {{ team.name }}
              </option>
          </select>
        </div>

        <div class="vs-badge-form">VS</div>

        <div class="form-group">
          <select v-model="matchData.awayTeamId" required>
              <option disabled value="null">Select Away Team</option>
              <option v-for="team in teams" :key="team.id" :value="team.id">
                  {{ team.name }}
              </option>
          </select>
        </div>

        <div class="form-group">
          <select v-model="matchData.stadiumId" required>
              <option disabled value="null">Select Stadium</option>
              <option v-for="stadium in stadiums" :key="stadium.id" :value="stadium.id">
                  {{ stadium.name }} ({{ stadium.city }})
              </option>
          </select>
        </div>

        <div class="form-group">
          <label>Date & Time</label>
          <input v-model="matchData.matchTime" type="datetime-local" required />
        </div>

        <div class="form-group">
          <label>Ticket Price (EGP)</label>
          <input v-model="matchData.ticketPrice" type="number" min="0" required />
        </div>

        <div class="form-action">
          <button type="submit" class="btn btn-primary btn-schedule">Schedule Match</button>
        </div>
      </form>
    </div>

    <!-- Match List -->
    <div class="matches-list">
        <h3>Upcoming Matches</h3>
        <div v-if="matches.length === 0" class="empty-state">No matches scheduled.</div>
        <div v-else class="match-cards">
            <div v-for="match in matches" :key="match.id" class="match-card">
                <div class="match-header">
                    <span class="match-stadium">🏟️ {{ match.stadium.name }}</span>
                    <span class="match-date">📅 {{ formatDate(match.matchTime) }}</span>
                </div>
                <div class="match-teams">
                    <div class="team home">
                        <img :src="match.homeTeam.logoUrl || '/placeholder.png'" @error="$event.target.src='https://placehold.co/100x100?text=No+Logo'" alt="Home Logo" class="team-logo-small" />
                        <span>{{ match.homeTeam.name }}</span>
                    </div>
                    <div class="versus">VS</div>
                    <div class="team away">
                        <img :src="match.awayTeam.logoUrl || '/placeholder.png'" @error="$event.target.src='https://placehold.co/100x100?text=No+Logo'" alt="Away Logo" class="team-logo-small" />
                        <span>{{ match.awayTeam.name }}</span>
                    </div>
                </div>
                <div class="match-footer">
                    <span class="price-tag">{{ match.ticketPrice }} EGP</span>
                    <button class="btn-book" @click="bookTicket(match.id)">Book Ticket</button>
                </div>
            </div>
        </div>
    </div>
  </div>
</template>

<style scoped>
.match-container {
  max-width: 1000px;
  margin: 0 auto;
}

.matches-list h3 {
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  color: var(--secondary-color);
}

.match-cards {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 2rem;
}

.match-card {
    background: white;
    border: 1px solid var(--border-color);
    border-radius: 16px;
    overflow: hidden;
    transition: all 0.3s ease;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
}

.match-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
    border-color: var(--primary-color);
}

.match-header {
    background: #f8fafc;
    padding: 1rem;
    border-bottom: 1px solid var(--border-color);
    display: flex;
    justify-content: space-between;
    font-size: 0.85rem;
    font-weight: 500;
    color: var(--text-muted);
}


.match-teams {
    padding: 1.5rem 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 0.5rem;
}

.team {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
    font-size: 1rem;
    font-weight: 700;
    color: var(--secondary-color);
}

.team-logo-small {
    width: 60px;
    height: 60px;
    object-fit: contain;
    background: #fff;
    border-radius: 50%;
    padding: 2px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.versus {
    flex: 0 0 auto;
    font-weight: 900;
    color: white;
    background: var(--primary-color);
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    font-size: 0.85rem;
    box-shadow: 0 2px 4px rgba(0, 166, 81, 0.3);
}


.match-footer {
    padding: 1.5rem;
    background: white;
    border-top: 1px solid #f1f5f9;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.price-tag {
    font-size: 1.25rem;
    font-weight: 800;
    color: var(--secondary-color);
    /* No icon styling needed as emoji is removed */
}

.btn-book {
    background-color: var(--primary-color);
    color: white;
    border: none;
    padding: 0.75rem 1.5rem;
    border-radius: 8px;
    font-weight: 600;
    font-size: 1rem;
    cursor: pointer;
    transition: background 0.2s;
}

.btn-book:hover {
    background-color: var(--primary-hover);
    box-shadow: 0 4px 12px rgba(0, 166, 81, 0.2);
}

.form-grid {
    display: flex;
    gap: 1.5rem;
    align-items: flex-end; /* Align to bottom so button lines up with inputs */
    flex-wrap: wrap;
    justify-content: center;
}

.form-group {
    flex: 1;
    min-width: 200px;
}

.vs-badge-form {
    font-weight: 900;
    color: var(--primary-color);
    font-size: 1.5rem;
    padding: 0 1rem;
    align-self: center; /* Keep vertically centered relative to neighbor inputs */
    /* margin-top removed to avoid pushing it down too much */
}

.btn-schedule {
    min-width: 200px;
    height: 48px; /* Match input height roughly */
    font-size: 1rem;
}
</style>
```
