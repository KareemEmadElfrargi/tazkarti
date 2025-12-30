import { createRouter, createWebHistory } from 'vue-router'
import MatchManager from './components/MatchManager.vue'
import TeamManager from './components/TeamManager.vue'
import StadiumManager from './components/StadiumManager.vue'
import UserAuth from './components/UserAuth.vue'

// Define routes
const routes = [
    {
        path: '/',
        redirect: '/matches'
    },
    {
        path: '/matches',
        name: 'matches',
        component: MatchManager,
        meta: { requiresAuth: true }
    },
    {
        path: '/teams',
        name: 'teams',
        component: TeamManager,
        meta: { requiresAuth: true }
    },
    {
        path: '/stadiums',
        name: 'stadiums',
        component: StadiumManager,
        meta: { requiresAuth: true }
    },
    {
        path: '/my-tickets',
        name: 'my-tickets',
        component: () => import('./components/MyTickets.vue'), // Lazy load
        meta: { requiresAuth: true }
    },
    {
        path: '/login',
        name: 'login',
        component: UserAuth
    }
]

// Create router instance
const router = createRouter({
    history: createWebHistory(),
    routes
})

// Navigation Guard
router.beforeEach((to, from, next) => {
    const isAuthenticated = !!localStorage.getItem('token')

    if (to.meta.requiresAuth && !isAuthenticated) {
        next('/login')
    } else {
        next()
    }
})

export default router
