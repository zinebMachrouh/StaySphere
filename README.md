# 🏨 StaySphere — Hotel Reservation Service

A small, well-structured Spring Boot backend that implements a simple hotel reservation system. It exposes REST endpoints to manage users, rooms and bookings, includes validation, centralized exception handling, MapStruct mappers, and unit tests for core business rules.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Troubleshooting](#-troubleshooting)

## ✨ Features

- ✅ Create and list bookings
- ✅ Create or update rooms
- ✅ Create or update users and list users
- ✅ Validation using Jakarta Bean Validation 
- ✅ Global exception handling with structured `ErrorResponse`
- ✅ MapStruct mappers for DTO ↔ entity conversions
- ✅ Unit tests for the booking business logic (Mockito + JUnit 5)

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.5.8**
	- Spring Web
	- Spring Validation
- **MapStruct** (mapping DTOs and entities)
- **Lombok** (builders/getters/setters reduction)
- **Maven** (build & dependency management)
- **JUnit 5** + **Mockito** (testing)

## 📁 Project Structure

```
hotel-backend/
├── src/
│   ├── main/
│   │   ├── java/com/skypay/hotel/
│   │   │   ├── controller/      # REST controllers (Bookings, Rooms, Users)
│   │   │   ├── service/         # Business logic + impls
│   │   │   ├── repository/      # Repository interfaces (custom impls under impl/)
│   │   │   ├── dto/             # Request/Response DTOs
│   │   │   ├── entity/          # Domain objects
│   │   │   ├── mappers/         # MapStruct mappers
│   │   │   └── exception/       # Custom exceptions + GlobalExceptionHandler
│   │   └── resources/
│   │       └── application.yml
│   └── test/                    # Unit & integration tests
└── pom.xml
```

## 📋 Prerequisites

Install the following before running locally:

- **Java JDK 17+**
	```powershell
	java -version
	```
- **Maven 3.6+ (or use the included Maven Wrapper)**
	```powershell
	mvn -version
	```

## 🚀 Installation

1. Clone the repository

```bash
git clone https://github.com/zinebMachrouh/StaySphere.git
cd hotel-reservation/hotel-backend
```

2. Build the project

Windows (with wrapper):
```powershell
.\mvnw.cmd clean install
```

Or with system Maven:
```powershell
mvn clean install
```

## 🎯 Running the Application

Start the backend (Windows example using the wrapper):

```powershell
cd hotel-backend
.\mvnw.cmd spring-boot:run
```

The service listens on `http://localhost:8080` by default.

## 📡 API Documentation

Base URL: `http://localhost:8080/api`

Available endpoints (controllers):

- `POST /api/bookings` — Create a booking
	- Body (example):
		```json
		{
			"userId": 1,
			"roomNumber": 101,
			"checkIn": "2025-12-20",
			"checkOut": "2025-12-22"
		}
		```
	- Success: `201 Created` with `BookingResponse`

- `GET /api/bookings` — Get bookings and rooms
	- Success: `200 OK` with `BookingsAndRoomsResponse` (bookings + rooms)

- `POST /api/rooms` — Create or update a room
	- Body (example):
		```json
		{
			"roomNumber": 101,
			"roomType": "STANDARD_SUITE",
			"pricePerNight": 1000
		}
		```

- `POST /api/users` — Create or update a user
	- Body (example):
		```json
		{
			"userId": 1,
			"balance": 10000
		}
		```

- `GET /api/users` — List all users

### Validation & Error Responses

This project returns structured `ErrorResponse` objects from `GlobalExceptionHandler`. Example fields:

```json
{
	"timestamp": "2025-12-17T10:30:00",
	"status": 400,
	"error": "Validation Failed",
	"message": "Check-in date must be today or in the future",
	"path": "/api/bookings"
}
```

Common error cases handled:
- Validation failures (bad input)
- Resource not found (user/room)
- Conflicts (room already booked / duplicate room features)
- Insufficient balance


## 🧪 Testing

Run unit tests with the wrapper (Windows):

```powershell
.\mvnw.cmd test
```

Or with Maven:

```powershell
mvn test
```

The project includes unit tests for `BookingServiceImpl` covering validation, conflicts, and balance checks.

## 🐛 Troubleshooting

- If the server doesn't start, confirm port 8080 is free and the Maven build succeeded.
- If MapStruct generated classes are missing, ensure annotation processing runs (`mvn clean install`) and that the compiler plugin in `pom.xml` is configured.
- The Asciidoctor plugin references `spring-restdocs.version` — if your build fails during `prepare-package`, ensure the property is defined or remove the plugin dependency.


---

If you want, I can implement any of the recommendations (e.g., switch `ResourceNotFoundException` to 404, change booking id generation, or add locking around booking). Which would you like me to do next?

⭐ Star the repo if you find it useful!
