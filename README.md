# Journal App — Backend REST API

A production-ready backend for a personal journaling application, built with Spring Boot. Features JWT authentication, Google OAuth2 login, sentiment analysis on journal entries, weekly email reports via Apache Kafka, and Redis caching.

**Live API:** https://journalapp-1-y9bm.onrender.com  
**Swagger UI:** https://journalapp-1-y9bm.onrender.com/docs

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.4 |
| Database | MongoDB Atlas |
| Cache | Redis |
| Messaging | Apache Kafka |
| Authentication | JWT + Google OAuth2 |
| Containerization | Docker |
| Deployment | Render |
| Documentation | Swagger / OpenAPI 3 |
| Testing | JUnit 5 + Mockito |

---

## Features

**Authentication**
- Username/password login with JWT tokens
- Google OAuth2 login — no password required
- BCrypt password hashing
- Role-based access control (USER / ADMIN)
- Token validation on every request via JWT filter

**Journal Entries**
- Create, read, update, delete personal journal entries
- Each entry has a title, content, date, and optional sentiment tag
- Entries are private — users can only access their own
- Sentiment options: `HAPPY`, `SAD`, `ANGRY`, `ANXIOUS`

**Sentiment Analysis & Weekly Reports**
- Every Sunday at midnight, a scheduled job runs automatically
- Finds all users with email + `analysis=true`
- Calculates the most frequent sentiment from the past 7 days
- Sends a weekly summary email via Apache Kafka (async)
- Kafka decouples email sending from the scheduler logic

**Redis Caching**
- Weather data cached for 5 minutes per city
- App configuration cached in memory, refreshed every 10 minutes
- Reduces external API calls significantly

**Code Quality**
- Global exception handler — every error returns consistent JSON
- Input validation on all request bodies
- 19 unit tests across JwtUtil, UserService, JournalEntryService
- Custom `AppException` with HTTP status codes

---

## API Endpoints

### Public — no authentication required

| Method | Endpoint | Description |
|---|---|---|
| GET | `/public/health-Check` | Server health check |
| POST | `/public/create-user` | Register new user |
| POST | `/public/login` | Login, returns JWT token |
| GET | `/oauth2/authorization/google` | Login with Google |

### Journal — requires Bearer token

| Method | Endpoint | Description |
|---|---|---|
| GET | `/journal` | Get all entries for logged-in user |
| POST | `/journal` | Create new journal entry |
| GET | `/journal/id/{id}` | Get entry by ID |
| PUT | `/journal/id/{id}` | Update entry by ID |
| DELETE | `/journal/id/{id}` | Delete entry by ID |

### User — requires Bearer token

| Method | Endpoint | Description |
|---|---|---|
| GET | `/user` | Get greeting + current weather |
| PUT | `/user` | Update profile (username, password, email) |
| DELETE | `/user` | Delete own account |

### Admin — requires ADMIN role

| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/all-users` | Get all users |
| POST | `/admin/create-admin-user` | Create admin user |
| GET | `/admin/clear-app-cache` | Force refresh app cache |

---

## Authentication Flow

### Username/Password Login
```
POST /public/login
Body: { "userName": "rohan", "password": "yourpassword" }

Response: "eyJhbGciOiJ..."  ← JWT token

Use token on all protected endpoints:
Header: Authorization: Bearer eyJhbGciOiJ...
```

### Google OAuth2 Login
```
1. Open in browser: GET /oauth2/authorization/google
2. Login with your Google account
3. Receive JSON response:
   {
     "token": "eyJhbGciOiJ...",
     "email": "you@gmail.com",
     "name": "Your Name"
   }
4. Use token exactly like username/password JWT
```

---

## Request & Response Examples

### Register a new user
```json
POST /public/create-user
{
  "userName": "rohan",
  "password": "securepass123"
}
→ 201 Created: "User created successfully"
```

### Create a journal entry
```json
POST /journal
Authorization: Bearer eyJ...
{
  "title": "Good day today",
  "content": "Had a productive morning and finished my project.",
  "sentiment": "HAPPY"
}
→ 201 Created: { entry object }
```

### Error response format (all errors)
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Journal entry not found",
  "timestamp": "2026-05-24T10:30:00"
}
```

### Validation error response
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "title: Title cannot be empty, content: Content cannot exceed 5000 characters",
  "timestamp": "2026-05-24T10:30:00"
}
```

---

## Architecture Overview

```
Client (Postman / Browser / Frontend)
        │
        ▼
   JwtFilter  ──────────────────────────────────────────────────────
   (validates token on every request)                              │
        │                                                          │
        ▼                                                     Invalid token
   SecurityConfig                                            → 401 JSON
   (route authorization)
        │
        ▼
   Controllers (REST layer)
   ├── PublicController    → registration, login
   ├── JournalController   → CRUD for journal entries
   ├── UserController      → profile management
   └── AdminController     → admin operations
        │
        ▼
   Services (business logic)
   ├── JournalEntryService → save/delete with MongoDB transactions
   ├── UserService         → user management, BCrypt encoding
   ├── WeatherService      → external API + Redis cache
   └── EmailService        → SMTP email sending
        │
        ▼
   Data Layer
   ├── MongoDB Atlas       → users_entries, journal_entries collections
   └── Redis               → weather cache, app config cache
        
   Background Jobs (every Sunday midnight)
   └── UserScheduler → finds users → calculates sentiment → Kafka → Email
```

---

## Data Models

### User
```json
{
  "userName": "rohan",
  "email": "rohan@gmail.com",
  "password": "$2a$10$...",
  "roles": ["USER"],
  "analysis": true,
  "jEntries": []
}
```

### Journal Entry
```json
{
  "id": "507f1f77bcf86cd799439011",
  "title": "My day",
  "content": "Today was great.",
  "date": "2026-05-24T10:30:00",
  "sentiment": "HAPPY"
}
```

---

## Running Locally

### Prerequisites
- Java 17
- Docker Desktop
- MongoDB Atlas account (or local MongoDB)
- Redis instance
- Google OAuth2 credentials (optional)

### Option 1 — Run with Docker

```bash
# Clone the repo
git clone https://github.com/Rohan200w/journalApp.git
cd journalApp

# Create a .env file with your values (see Environment Variables below)

# Build and run
docker build -t journal-app .
docker run -p 8080:8080 --env-file .env journal-app
```

### Option 2 — Run with Maven

```bash
# Clone the repo
git clone https://github.com/Rohan200w/journalApp.git
cd journalApp

# Set environment variables (or update application.properties directly)
export SPRING_DATA_MONGODB_URI=your_mongodb_uri
export JWT_SECRET=your_secret_key
# ... other vars

# Run
mvn spring-boot:run
```

### Run tests

```bash
mvn test
# Expected: Tests run: 25, Failures: 0, Errors: 0, Skipped: 6
```

---

## Environment Variables

| Variable | Description |
|---|---|
| `SPRING_DATA_MONGODB_URI` | MongoDB connection string |
| `SPRING_DATA_MONGODB_DATABASE` | Database name |
| `SPRING_DATA_REDIS_HOST` | Redis host |
| `SPRING_DATA_REDIS_PORT` | Redis port |
| `SPRING_DATA_REDIS_USERNAME` | Redis username |
| `SPRING_DATA_REDIS_PASSWORD` | Redis password |
| `JWT_SECRET` | Secret key for JWT signing (min 32 chars) |
| `WEATHER_API_KEY` | Weatherstack API key |
| `SPRING_MAIL_HOST` | SMTP host (e.g. smtp.gmail.com) |
| `SPRING_MAIL_PORT` | SMTP port (e.g. 587) |
| `SPRING_MAIL_USERNAME` | Email address for sending |
| `SPRING_MAIL_PASSWORD` | Email app password |
| `GOOGLE_CLIENT_ID` | Google OAuth2 client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 client secret |

---

## Project Structure

```
src/main/java/net/engineer/journalApp/
├── Cache/              → AppCache (in-memory config store)
├── Schedular/          → UserScheduler (weekly sentiment job)
├── api/response/       → WeatherResponse (external API model)
├── config/             → SecurityConfig, SwaggerConfig, GlobalExceptionHandler
├── controller/         → REST controllers
├── entity/             → MongoDB documents (User, JournalEntry)
├── enums/              → Sentiment enum
├── exception/          → AppException (custom exception with HTTP status)
├── filter/             → JwtFilter, OAuth2SuccessHandler
├── model/              → SentimentData (Kafka message model)
├── repository/         → MongoDB repositories
├── services/           → Business logic layer
└── utils/              → JwtUtil

src/test/java/net/engineer/journalApp/
├── service/
│   ├── JwtUtilTest.java           → 6 tests, pure Java
│   ├── UserServiceMockTest.java   → 7 tests, Mockito
│   └── JournalEntryServiceTest.java → 6 tests, Mockito
└── (integration tests — @Disabled, run manually)
```

---

## Design Decisions

**Why MongoDB?**
Journal entries are document-like by nature — variable length content, optional fields, no complex joins needed. MongoDB's flexible schema fits this perfectly.

**Why Kafka for emails?**
Decouples the weekly scheduler from the email sending logic. If the email service is slow or fails, it doesn't block the scheduler. The scheduler just produces a message — email sending happens independently.

**Why Redis?**
Weather data from the external API doesn't change frequently. Caching it for 5 minutes reduces API calls and speeds up the `/user` endpoint significantly.

**Why both JWT and OAuth2?**
JWT for API clients (Postman, mobile apps) that need programmatic access. OAuth2 for browser-based login — no password management required on your end. Both produce the same JWT at the end, so all downstream logic is identical.

---

## Author

Built by Rohan Saha  
GitHub: [Rohan200w](https://github.com/Rohan200w)
