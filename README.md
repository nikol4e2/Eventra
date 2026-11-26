#  Eventra

> A full-stack event management and ticketing platform where organizers publish events and sell tickets, and attendees purchase them and enter with QR codes.

![Java](https://img.shields.io/badge/Java-Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

---

##  About

Eventra is a full-stack web application with a **Spring Boot REST API** and a **React + TypeScript single-page frontend**. It supports two types of users:

- **Organizers** create events, define ticket types and prices, publish events, and validate tickets at the door.
- **Attendees** browse published events, buy tickets, and receive a **QR code** for each ticket.

I built it to practice designing a layered backend, securing an API with JWT-based authentication, and connecting it to a modern typed frontend.

##  Features

-  **Secure authentication** – Keycloak (OpenID Connect) login with JWT-secured API, role-based access (organizer / attendee) and automatic user provisioning on first login
-  **Event management** – create, update, publish and list events with status tracking
-  **Ticket types** – multiple ticket categories per event, with sold-out handling
-  **Ticket purchasing** – attendees can purchase tickets and view them in their dashboard
-  **QR code tickets** – a unique QR code is generated for every ticket
-  **Ticket validation** – organizers can validate tickets at entry (QR scan or manual) and every validation is recorded
-  **Public event browsing** – published events with pagination
-  **Centralized error handling** – custom exceptions and a global exception handler returning consistent error responses


##  Tech Stack

| Layer | Technologies |
|---|---|
| **Backend** | Java 23, Spring Boot 3.4, Spring Security (OAuth2 resource server / JWT), Spring Data JPA, Hibernate, Bean Validation |
| **Mapping & utilities** | MapStruct (entity ↔ DTO mapping), Lombok, ZXing (QR code generation) |
| **Frontend** | React, TypeScript |
| **Database** | PostgreSQL (H2 available for local development) |
| **Authentication** | Keycloak  |
| **Tooling** | Maven, Docker & Docker Compose, Adminer, ESLint, Prettier, Git & GitHub |

## Architecture

The backend follows a clean layered architecture:

```
Controller  →  Service (interface + impl)  →  Repository  →  Database
     ↑                     ↑
    DTOs              Domain models
 (MapStruct mappers convert between them)
```

```
Eventra/
├── src/main/java/com/nikola/eventra/
│   ├── config/          # Security, JPA, JWT converter, QR code config
│   ├── controllers/     # REST controllers + global exception handler
│   ├── domain/          # Models, DTOs, enums, request objects
│   ├── exceptions/      # Custom exceptions
│   ├── filters/         # User provisioning filter
│   ├── mappers/         # MapStruct mappers
│   ├── repositories/    # Spring Data JPA repositories
│   ├── services/        # Business logic (interfaces + implementations)
│   └── util/            # JWT utilities
├── frontend/            # React + TypeScript (Vite) client
│   └── src/
│       ├── components/  # Reusable components and shadcn/ui primitives
│       ├── pages/       # Landing, dashboard, purchase, QR validation pages...
│       ├── hooks/       # Custom hooks (role handling)
│       └── lib/         # API client and utilities
├── docker-compose.yml
└── pom.xml
```

### Core domain model

`User` · `Event` · `TicketType` · `Ticket` · `QrCode` · `TicketValidation`




##  Getting Started

### Prerequisites

- Java 23 and Maven (or use the included `mvnw`)
- Node.js 18+ and npm
- Docker & Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/nikol4e2/Eventra.git
cd Eventra
```

### 2. Start the infrastructure

Docker Compose starts PostgreSQL, Adminer (database UI) and Keycloak (authentication server):

```bash
docker-compose up -d
```

| Service | URL |
|---|---|
| PostgreSQL | `localhost:5432` |
| Adminer (DB UI) | http://localhost:8888 |
| Keycloak | http://localhost:9090 |

> Set your own `POSTGRES_PASSWORD`, `KEYCLOAK_ADMIN` and `KEYCLOAK_ADMIN_PASSWORD` values in `docker-compose.yml` before starting.

### 3. Configure Keycloak

1. Open http://localhost:9090 and log in to the admin console.
2. Create a realm named `[your-realm-name]`.
3. Create a client for the frontend `[your-client-id]` (public client, with `http://localhost:5173/*` as a valid redirect URI).
4. Create the roles `[e.g. ORGANIZER, ATTENDEE]` and a few test users.
5. Make sure the issuer URI in `src/main/resources/application.properties` matches your realm.

### 4. Run the backend

```bash
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080` (configure in `src/main/resources/application.properties`).

### 5. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

The app runs at `http://localhost:5173`.

