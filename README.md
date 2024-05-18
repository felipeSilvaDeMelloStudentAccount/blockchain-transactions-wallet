# Secure Systems Development

### Assignment 3, Option A: Bitcoin blockchain viewer

### Weight: 30%

### Student Info

- Felipe Silva de Mello
- D23125661

# Bitcoin Blockchain Viewer

## Overview

This API is a tool for viewing the Bitcoin blockchain allowing users to see newly mined blocks.
It presents information such, as the timestamp of block addition contained transactions, nonce value
and difficulty level.
By connecting to the Bitcoin network it receives updates on blocks and checks the authenticity of
each one.

## Features

- Display blocks and their details (hash, previous hash, nonce, difficulty, timestamp).
- List transactions within each block along with their inputs and outputs.
- Continuously listen to the Bitcoin network for new blocks.
- Robust connection management with retry logic.
- Swagger UI for API documentation and testing.
- Actuator endpoints for health checks and metrics.

## Technologies Used
### Backend
- **Java 21**: The primary programming language utilized for the application is Java 21.
- **Spring Boot**:  is employed as a framework to construct the REST API and oversee the application
  lifecycle.
- **PostgreSQL**: serves as the database for storing data.
- **Hibernate**: functions as an ORM tool for interacting with the database.
- **Flyway**: is utilized as a database migration tool.
- **Docker**: is used as a containerization tool for packaging the application.
- **Maven**: serves as a build automation tool to manage dependencies and project building.
- **Swagger**: is employed as an API documentation and testing tool.
- **Spring Boot Actuator**:  aids, in monitoring and managing the application.
### Frontend
- **React**: JavaScript library for building user interfaces.
- **Bootstrap**: Frontend framework for building responsive and mobile-first websites.
- **React Bootstrap**: React components for Bootstrap.
- **React Router DOM**: DOM bindings for React Router.
- **Axios**: Promise-based HTTP client for the browser and Node.js.
- **Date-fns**: Modern JavaScript date utility library.
- **Recharts**: React charting library.


### Prerequisites
- Java 21 or higher
- Maven
- Docker
- PostgreSQL (Optinal) only required if running it manually
- Node.js (v14 or higher) (Optional) only required if running it manually

# Setup
## Clone the Repository

```bash
git clone https://github.com/felipeSilvaDeMelloStudentAccount/blockchain-transactions-wallet 
 cd blockchain-transactions-wallet
```

## Build the Project

```bash 
mvn clean install
```

# Option 1: Fast Track with Docker
```bash
git clone https://github.com/felipeSilvaDeMelloStudentAccount/blockchain-transactions-wallet 
 cd blockchain-transactions-wallet
```
# Build and run the Docker Image:

```bash 
docker-compose up --build
```
## Bringing the UI up
- Let the backend running on port 8080
- then open a new terminal and run the following commands
## Clone the Repository blockchain-transactions-ui
```bash
  git clone https://github.com/felipeSilvaDeMelloStudentAccount/blockchain-transactions-ui 
  cd blockchain-transactions-ui
```

## Build the docker image:
```bash
  docker build -t blockchain-viewer-web .
```
## Run the docker image:
```bash
  docker run -p 3000:80 blockchain-viewer-web
```

# Option 2: Manual Setup with Java, Maven and PostgreSQL and Node.js

## Running with Maven and PostgreSQL and Node.js

### PostgreSQL Configuration
Ensure you have PostgreSQL running with the following configurations:
- URL: `jdbc:postgresql://localhost:5432/blockchain_db`
- Username: `blockchain`
- Password: `myPassword243OSOKpadpa`
- Driver: `org.postgresql.Driver`

```sql
psql -U admin
CREATE USER "blockchain" WITH PASSWORD 'myPassword243OSOKpadpa';
CREATE DATABASE blockchain_db;
GRANT ALL PRIVILEGES ON DATABASE blockchain_db TO "blockchain-api";
```

### Run the the Backend Application with maven
```bash
mvn spring-boot:run
```

### Running the Frontend Application
```bash
git clone https://github.com/felipeSilvaDeMelloStudentAccount/blockchain-transactions-ui 
cd blockchain-transactions-ui
npm install
npm start
```

# Finally After everything is up and running
## UI Running 
- **React App**: http://localhost:3000

## Backend Running 
### Accessing Swagger UI
Once the application is running, you can access the Swagger UI at:
- **Swagger UI**: http://localhost:8080/api/bitcoin/swagger-ui/index.html
- **API Docs**: http://localhost:8080/api/bitcoin/v3/api-docs

### Actuator Endpoints
Spring Boot Actuator endpoints can be accessed for application monitoring:
**Actuator**: http://localhost:8080/actuator


#### Note

- Docker will handle the PostgreSQL setup and configuration.
- The application will be available on port 8080.
- **GET http://localhost:8080/api/bitcoin/blocks**: List all blocks.

#### API Endpoints

- **GET /api/bitcoin/blocks**: List all blocks.
- **GET /api/bitcoin/blocks/{hash}**: Get details of a specific block by hash.
- **GET /api/bitcoin/blocks/{hash}/transactions**: List transactions of a specific block by hash.

# Achievement the Challenge

## Connecting to the Bitcoin Network - 10 marks

- The application connects to the Bitcoin network to receive broadcasted blocks and transactions.

### Continuous Listening for New Blocks

- The application continuously listens to the Bitcoin network for new blocks, ensuring that it
  always has the latest data.

## Storing Data in PostgreSQL and Parsing Transactions - 10 marks

- The blocks and transactions are stored in a PostgreSQL database, ensuring persistence and
  efficient querying.

### Managing Database Schema with Flyway

- Flyway is used for database migrations, ensuring that the database schema is up-to-date and
  consistent.

## Using Spring Boot for REST API to display blockchain data - 5 marks

- Spring Boot provides a robust framework for developing RESTful web services, making it easy to
  create endpoints for accessing blockchain data.

## README file with clear instructions - 5 marks

### Swagger UI for API Documentation

- Swagger UI is integrated for API documentation and testing, making it easy for developers to
  understand and interact with the API.


