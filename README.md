# Secure Systems Development
### Assignment 3, Option A: Bitcoin blockchain viewer
### Weight: 30%
### Student Info
- Felipe Silva de Mello
-  D23125661


# Bitcoin Blockchain Viewer

## Overview

This API is a tool for viewing the Bitcoin blockchain allowing users to see newly mined blocks.
It presents information such, as the timestamp of block addition contained transactions, nonce value and difficulty level. 
By connecting to the Bitcoin network it receives updates on blocks and checks the authenticity of each one.

## Features

- Display blocks and their details (hash, previous hash, nonce, difficulty, timestamp).
- List transactions within each block along with their inputs and outputs.
- Continuously listen to the Bitcoin network for new blocks.
- Robust connection management with retry logic.
- Swagger UI for API documentation and testing.
- Actuator endpoints for health checks and metrics.

## Technologies Used

- **Java 21**: The primary programming language utilized for the application is Java 21.
- **Spring Boot**:  is employed as a framework to construct the REST API and oversee the application lifecycle.
- **PostgreSQL**: serves as the database for storing data.
- **Hibernate**: functions as an ORM tool for interacting with the database.
- **Flyway**: is utilized as a database migration tool.
- **Docker**: is used as a containerization tool for packaging the application.
- **Maven**: serves as a build automation tool to manage dependencies and project building.
- **Swagger**: is employed as an API documentation and testing tool.
- **Spring Boot Actuator**:  aids, in monitoring and managing the application.

## Setup

### Prerequisites

- Java 21 or higher
- Maven
- PostgreSQL
- Docker (optional, for containerized deployment)

### Clone the Repository

```bash
git clone https://github.com/felipeSilvaDeMelloStudentAccount/blockchain-transactions-wallet
cd blockchain-transactions-wallet
```
### Build the Project
```bash 
mvn clean install
```

## Running with Docker
Docker simplifies the setup by handling the database configuration and dependencies.
### Build the Docker Image:
```bash 
docker-compose up --build
```
#### Note
- Docker will handle the PostgreSQL setup and configuration.
- The application will be available on port 8080.
- **GET http://localhost:8080/api/bitcoin/blocks**: List all blocks.


## Running with Maven and PostgreSQL
### PostgreSQL Configuration
Ensure you have PostgreSQL running with the following configurations:
- URL: `jdbc:postgresql://localhost:5432/blockchain_db`
- Username: `blockchain`
- Password: `myPassword243OSOKpadpa`
- Driver: `org.postgresql.Driver`
### Run the Application
```bash
mvn spring-boot:run
```



### Notes
- Ensure PostgresSQL is running and accessible with the provided configuration.
- Maven is required to build and run the application.
- The application will be available on port 9001. 
-- **GET http://localhost:9001/api/bitcoin/blocks**: List all blocks.

## Accessing Swagger UI
Once the application is running, you can access the Swagger UI at:
- **Swagger UI**: http://localhost:9001/api/bitcoin/swagger-ui/index.html
- **API Docs**: http://localhost:9001/api/bitcoin/v3/api-docs

## Actuator Endpoints
Spring Boot Actuator endpoints can be accessed for application monitoring:

**Actuator**: http://localhost:9001/actuator

# API Endpoints
- **GET /api/bitcoin/blocks**: List all blocks.
- **GET /api/bitcoin/blocks/{hash}**: Get details of a specific block by hash.
- **GET /api/bitcoin/blocks/{hash}/transactions**: List transactions of a specific block by hash.

# Achievement the Challenge
## Connecting to the Bitcoin Network - 10 marks
- The application connects to the Bitcoin network to receive broadcasted blocks and transactions.
### Continuous Listening for New Blocks
- The application continuously listens to the Bitcoin network for new blocks, ensuring that it always has the latest data.

## Storing Data in PostgreSQL and Parsing Transactions - 10 marks
- The blocks and transactions are stored in a PostgreSQL database, ensuring persistence and efficient querying.
### Managing Database Schema with Flyway
- Flyway is used for database migrations, ensuring that the database schema is up-to-date and consistent.

## Using Spring Boot for REST API to display blockchain data - 5 marks
- Spring Boot provides a robust framework for developing RESTful web services, making it easy to create endpoints for accessing blockchain data.


## README file with clear instructions - 5 marks
### Swagger UI for API Documentation 
- Swagger UI is integrated for API documentation and testing, making it easy for developers to understand and interact with the API.


