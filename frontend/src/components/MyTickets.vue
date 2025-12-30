<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const tickets = ref([])
const loading = ref(true)
const error = ref('')

const fetchTickets = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const response = await axios.get('http://localhost:8080/api/tickets', {
      headers: { Authorization: `Bearer ${token}` }
    })
    tickets.value = response.data
  } catch (err) {
    console.error('Error fetching tickets:', err)
    error.value = 'Failed to load tickets. ' + (err.response?.data?.message || err.message)
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('en-US', {
    dateStyle: 'medium',
    timeStyle: 'short'
  })
}

onMounted(() => {
  fetchTickets()
})
</script>

<template>
  <div class="tickets-container">
    <h2>My Tickets</h2>

    <div v-if="loading" class="loading">Loading tickets...</div>
    <div v-if="error" class="alert error">{{ error }}</div>

    <div v-if="!loading && tickets.length === 0" class="empty-state">
      <p>You haven't booked any tickets yet.</p>
    </div>

    <div v-else class="tickets-grid">
      <div v-for="ticket in tickets" :key="ticket.ticketCode" class="ticket-card">
        <div class="ticket-header">
            <span class="ticket-code">#{{ ticket.ticketCode }}</span>
            <span class="seat-badge">Seat: {{ ticket.seatNumber }}</span>
        </div>
        
        <div class="match-info" v-if="ticket.match">
            <div class="team-vs">
                <div class="team">
                    <img :src="ticket.match.homeTeam?.logoUrl || '/placeholder.png'" 
                         @error="$event.target.src='https://placehold.co/60x60?text=Logo'" 
                         class="team-logo-mini" />
                    <span>{{ ticket.match.homeTeam?.name }}</span>
                </div>
                <span class="vs-text">VS</span>
                 <div class="team">
                    <img :src="ticket.match.awayTeam?.logoUrl || '/placeholder.png'" 
                         @error="$event.target.src='https://placehold.co/60x60?text=Logo'" 
                         class="team-logo-mini" />
                    <span>{{ ticket.match.awayTeam?.name }}</span>
                </div>
            </div>
            
            <div class="details">
                <p><strong>🏟️ Stadium:</strong> {{ ticket.match.stadium?.name }}</p>
                <p><strong>📅 Time:</strong> {{ formatDate(ticket.match.matchTime) }}</p>
                <p><strong>💰 Price:</strong> {{ ticket.match.ticketPrice }} EGP</p>
            </div>
        </div>
        <div class="match-info" v-else>
            <p style="text-align:center; padding: 1rem; color: orange;">
                ⚠️ Update Backend to see details <br>
                <small>{{ ticket.matchInfo || 'Invalid Data' }}</small>
            </p>
        </div>
        
        <div class="ticket-footer">
            <div class="barcode">||| || ||| ||||| ||</div>
            <span class="status">Valid Ticket</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.tickets-container {
  max-width: 900px;
  margin: 0 auto;
}

h2 {
    color: var(--text-main);
    margin-bottom: 2rem;
    text-align: center;
}

.tickets-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 1.5rem;
}

.ticket-card {
    background: white;
    border: 1px solid var(--border-color);
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s;
    position: relative;
}

.ticket-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.ticket-header {
    background: var(--primary-color);
    color: white;
    padding: 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
}

.seat-badge {
    background: rgba(255,255,255,0.2);
    padding: 0.3rem 0.8rem;
    border-radius: 20px;
    font-size: 0.9rem;
}

.match-info {
    padding: 1.5rem;
}

.team-vs {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
    padding-bottom: 1.5rem;
    border-bottom: 1px solid #eee;
}

.team {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
    text-align: center;
    font-weight: 600;
    font-size: 0.9rem;
    flex: 1;
}

.team-logo-mini {
    width: 50px;
    height: 50px;
    object-fit: contain;
}

.vs-text {
    font-weight: 900;
    color: var(--text-muted);
}

.details p {
    margin: 0.5rem 0;
    color: var(--text-main);
    font-size: 0.95rem;
}

.ticket-footer {
    background: #f8fafc;
    padding: 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px dashed var(--border-color);
}

.barcode {
    font-family: 'Courier New', monospace;
    letter-spacing: 2px;
    font-weight: bold;
    color: var(--text-muted);
}

.status {
    color: var(--primary-color);
    font-weight: bold;
    font-size: 0.85rem;
    text-transform: uppercase;
}

.loading, .alert, .empty-state {
    text-align: center;
    padding: 2rem;
    color: var(--text-muted);
}

.alert.error {
    color: var(--danger-color);
}
</style>
