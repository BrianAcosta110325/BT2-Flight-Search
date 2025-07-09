# Flight Search App

A web application that allows users to search for flights using the [Amadeus REST API](https://developers.amadeus.com/), without using their SDK.

## Tech Stack

* Backend: Java 17, Spring Boot 3.5.0, Gradle
* Frontend: React, TypeScript
* Docker + Docker Compose (for dev and production)

---

## Repository Structure

```
.
├── flight-search-be/        # Backend (Spring Boot)
│   ├── src/
│   ├── build.gradle.kts
│   └── Dockerfile(.dev)
├── flight-search-fe/        # Frontend (React)
│   ├── src/
│   ├── package.json
│   └── Dockerfile(.dev)
├── docker-compose.yml       # Dev environment
├── docker-compose.prod.yml  # Production environment
└── README.md
```

---

## Features

* Search flights by:

  * Origin and destination IATA code
  * Departure date
  * Number of adults
  * Currency
  * Non-stop only toggle
* Manual pagination on backend
* User-friendly frontend with filters and results
* Enriched response with:

  * Airline names
  * Stops and airports
  * Departure/arrival times and durations

---

## Prerequisites

* [Docker](https://www.docker.com/)
* [Docker Compose](https://docs.docker.com/compose/)
* [Amadeus API Key](https://developers.amadeus.com/) (`client_id` and `client_secret`)

---

## Initial Setup

### 1. Environment Variables (backend)

Create a `.env` file or set the following variables:

```bash
AMADEUS_CLIENT_ID=your_client_id
AMADEUS_CLIENT_SECRET=your_client_secret
```

Ensure your backend reads them correctly via `@Value` or `Environment`.

---

## Run in Development

```bash
docker compose up --build
```

This will start:

* 🔙 Backend: [http://localhost:8080](http://localhost:8080)
* 🌐 Frontend: [http://localhost:3000](http://localhost:3000)

Live reload works for both Java and React source code using volumes and `spring-boot-devtools` + `react-scripts`.

---

## Run in Production

```bash
docker compose -f docker-compose.prod.yml up --build
```

---

## Useful Commands

### Backend (without Docker)

```bash
cd flight-search-be
./gradlew bootRun
```

### Frontend (without Docker)

```bash
cd flight-search-fe
npm install
npm start
```

---

## API Endpoints Example

```
GET /api/flights/search
```

**Required Query Parameters:**

| Parameter               | Type       | Example    |
| ----------------------- | ---------- | ---------- |
| originLocationCode      | string     | MEX        |
| destinationLocationCode | string     | JFK        |
| departureDate           | YYYY-MM-DD | 2025-08-12 |
| adults                  | int        | 1          |
| currencyCode            | string     | USD        |
| nonStop                 | boolean    | false      |
| page                    | int        | 1          |

**Sample Response:**

```json
{
  "flights": [ ... ],
  "totalPages": 5
}
```

---

## 👨‍💻 Author

Developed by [Brian Acosta](https://github.com/BrianAcosta110325)
License: MIT