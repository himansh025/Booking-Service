# Uber Booking Service

A Java-based microservice for handling ride bookings in an Uber-like system. Built with **Spring Boot**, **JPA**, and integrates with **Location Service** and **Uber Socket Service** using **Retrofit** for async communication.

---

## Microservices

| Service          | Description                                                                 | Link                                                                      |
|------------------|-----------------------------------------------------------------------------|---------------------------------------------------------------------------|
| Entity Service   | Manages core entities like Booking, Passenger, Driver, Review, and Locations | [GitHub Repo](https://github.com/himansh025/Comman-Entity)                |
| Booking Service  | Handles ride bookings, assigns drivers, and manages booking status          | [GitHub Repo](https://github.com/himansh025/Booking-Service)              |
| Socket Service   | Handles async ride notifications and driver requests                        | [GitHub Repo](https://github.com/himansh025/Uber-SocketServer.git)        |
| Location Service | Provides nearby driver location queries                                     | [GitHub Repo](https://github.com/himansh025/Location-Service)             |
| Auth Service     | Handles passenger authentication and authorization                           | [GitHub Repo](https://github.com/himansh025/AuthService)                  |
| Review Service   | Collects and manages ride reviews                                           | [GitHub Repo](https://github.com/himansh025/ReviewServices)               |

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Integration](#integration)
- [Dependencies](#dependencies)
- [Setup](#setup)
- [License](#license)

---

## About

The **Booking Service** is responsible for:

- Creating and updating ride bookings.
- Assigning available drivers to passengers.
- Fetching nearby drivers via **Location Service**.
- Raising ride requests asynchronously via **Uber Socket Service**.

This service integrates with other microservices for a complete Uber-like experience.

---

## Features

- Create and update ride bookings.
- Assign drivers based on proximity.
- Asynchronous communication with **Location** and **Socket** services.
- Tracks booking status: `ASSIGNING_DRIVER`, `IN_PROGRESS`, `COMPLETED`, etc.
- Uses Retrofit for service-to-service REST calls.

---

## API Endpoints

| Endpoint               | Method | Description                          |
|------------------------|--------|--------------------------------------|
| `/api/v1/booking`      | POST   | Create a new booking                 |
| `/api/v1/booking/{id}` | POST   | Update booking status or assign driver |

**Create Booking Request:**

```json
{
  "passengerId": 1,
  "startLocation": {
    "latitude": 28.7041,
    "longitude": 77.1025
  },
  "endLocation": {
    "latitude": 28.5355,
    "longitude": 77.3910
  }
}


Update Booking Request

```json
{
  "driverId": 2,
  "status": "IN_PROGRESS"
}

Integration
Location Service

Fetches nearby drivers:

@POST("/api/location/nearby/drivers")
Call<DriverLocationDto[]> getNearbyDrivers(@Body NearbyDriverRequestDto requestDto);

Uber Socket Service

Raises ride requests asynchronously:
@POST("/api/socket/newride")
Call<Boolean> getNewRides(@Body RideRequestDto requestDto);

Setup

Clone the repository:

git clone <repository-url>


Configure MySQL database in application.properties.

Configure Eureka Client for service discovery if using multiple microservices.

Run the service:

./gradlew bootRun


Booking service will create bookings, fetch nearby drivers asynchronously, and raise ride requests via socket.