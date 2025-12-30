# 🎟️ Tazkarti Clone - Sports Ticketing System

A robust, full-stack application for managing and booking sports match tickets. This project demonstrates a secure, scalable backend architecture coupled with a modern, responsive frontend.

> **Project Origin & Credits:**
> *   **Backend (Core Logic):** Crafted manually by **Me**, adhering to strict software engineering principles, clean architecture, and advanced security practices with Spring Boot.
> *   **Frontend (UI/UX):** Developed with the assistance of **Vibe Coding** technology, utilizing Vue.js to create a dynamic and user-friendly interface.

---

##  Features

###  Backend (Spring Boot)
*   **Security & Auth:** Implemented **JWT (JSON Web Token)** authentication with Spring Security 6.
*   **Role-Based Access Control (RBAC):** Distinct privileges for **ADMIN** (Management) and **USER** (Booking).
*   **Concurrency Handling:** utilized `PESSIMISTIC_WRITE` locks (`findByIdWithLock`) to prevent double-booking of seats during high traffic.
*   **Validation:** Robust input validation and custom exception handling for business rules.
*   **RESTful API:** Clean endpoints for managing resources.

###  Frontend (Vue.js - via Vibe Coding)
*   **Single Page Application (SPA):** Seamless navigation using **Vue Router**.
*   **Dynamic Dashboard:** Role-specific views (Admin sees Management tabs, Users see Booking tabs).
*   **Real-time Features:** Instant feedback for booking status and ticket generation.
*   **Modern UI:** Clean, "Ticket-style" design with responsive cards and layouts.
*   **My Tickets:** Dedicated section for users to view and manage their booked tickets with visuals.

---

##  Technology Stack

| Component | Technology |
|-----------|------------|
| **Backend** | Java 21, Spring Boot 4.x, Spring Data JPA, Spring Security |
| **Database**| Relational Database (PostgreSql) |
| **Frontend**| Vue.js 3, Vite, Axios, CSS3 Variables |
| **Build**   | Maven (Backend), NPM (Frontend) |

---

## 📂 Project Structure

```
tazkarti/
├── src/main/java       # Backend Source Code
│   ├── controller      # REST Controllers
│   ├── service         # Business Logic
│   ├── repository      # Data Access Layer
│   ├── model           # JPA Entities & DTOs
│   └── config          # Security & App Config
└── frontend/           # Vibe Coding Generated Client
    ├── src/components  # Vue Components (MatchManager, MyTickets, etc.)
    └── src/router      # Navigation Logic
```

## ▶️ How to Run

### 1. Backend
1. Open the project in your IDE (IntelliJ/Eclipse).
2. Configure your database settings in `application.properties`.
3. Run `TazkartiApplication.java`.
4. Server starts on `http://localhost:8080`.

### 2. Frontend
1. Navigate to the `frontend` directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```
4. Access the app at `http://localhost:5173`.

---

