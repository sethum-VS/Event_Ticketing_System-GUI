# Ticket Simulation System - Backend

This is the backend service for the **Ticket Simulation System**, built using **Spring Boot** to handle ticket simulations, manage multi-threaded producer-consumer operations, and interact with a MongoDB database for data persistence.

## Features
- **Simulation Management:**
  - Multi-threaded producer-consumer logic for vendors and customers.
  - Configurable simulation parameters such as total tickets, ticket release rate, customer retrieval rate, and max ticket capacity.
- **Real-Time Log Streaming:**
  - Provides real-time simulation logs to the frontend via Server-Sent Events (SSE).
- **Database Integration:**
  - MongoDB for persistent storage of logs and simulation activities.
  - Save and retrieve configuration settings for reusability.
- **RESTful APIs:**
  - Exposes endpoints for starting simulations, fetching logs, and managing configurations.

## Technologies
- **Spring Boot**: For building REST APIs and managing backend logic.
- **MongoDB**: As the database for logs, configurations, and activities.
- **ReentrantLock**: Ensures thread-safe operations for ticket pool management.
- **Server-Sent Events (SSE)**: Streams real-time logs to the frontend.

## Setup
1. Clone the repository.
2. Configure MongoDB:
   - Update the `application.properties` file with your MongoDB connection string.
3. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
