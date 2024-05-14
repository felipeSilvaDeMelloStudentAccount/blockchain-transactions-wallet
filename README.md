# Bitcoin Blockchain Viewer

## Overview

This application is a simplified Bitcoin blockchain viewer that displays blocks as they are mined. It shows the date and time a block was added to the blockchain, the transactions in the block, the nonce, and the difficulty level. The application connects to the Bitcoin network, receives broadcasts of new blocks, and verifies the integrity of each block.

## Features

- Display blocks and their details (hash, previous hash, nonce, difficulty, timestamp).
- List transactions within each block along with their inputs and outputs.
- Continuously listen to the Bitcoin network for new blocks.
- Robust connection management with retry logic.
- Swagger UI for API documentation and testing.

## Technologies Used

- **Java 21**: The main programming language used for the application.
- **Spring Boot**: Framework for building the REST API and managing the application lifecycle.
- **PostgreSQL**: Database for storing blockchain data.
- **Hibernate**: ORM tool for database interactions.
- **Flyway**: Database migration tool.
- **Docker**: Containerization tool for packaging the application.
- **Maven**: Build automation tool for managing dependencies and building the project.
- **Swagger**: API documentation and testing tool.

## Setup

### Prerequisites

- Java 21 or higher
- Maven
- PostgreSQL
- Docker (optional, for containerized deployment)

# Running the Application
### Clone the repository:
```bash
  git clone https://github.com/felipeSilvaDeMelloStudentAccount/blockchain-transactions-wallet
  cd <repository_directory>
```

## Using Maven
### Build the project:
```bash
  mvn clean install
``` 
### Run the application:
```bash
  mvn spring-boot:run
```

## Using Docker
### Build the Docker image:
```bash
  docker build -t blockchain-viewer .
```
### Run the Docker container:
```bash
  docker run -p 8000:8000 blockchain-viewer
```

## Using Java
### Build the project:
```bash
  mvn clean install
```

### Run the JAR file:
```bash
  java -jar target/blockchain-viewer-0.0.1-SNAPSHOT.jar
```

# API Endpoints
- GET /api/bitcoin/blocks: List all blocks.
- GET /api/bitcoin/blocks/{hash}: Get details of a specific block by hash.
- GET /api/bitcoin/blocks/{hash}/transactions: List transactions of a specific block by hash.


# Achieving the Requirements
## This API achieves the requirements by:

### Connecting to the Bitcoin Network
The application connects to the Bitcoin network to receive broadcasted blocks and transactions.

### Storing Data in PostgreSQL
The blocks and transactions are stored in a PostgreSQL database, ensuring persistence and efficient querying.

### Using Spring Boot for REST API
Spring Boot provides a robust framework for developing RESTful web services, making it easy to create endpoints for accessing blockchain data.

### Managing Database Schema with Flyway
Flyway is used for database migrations, ensuring that the database schema is up-to-date and consistent.

### Swagger UI for API Documentation
Swagger UI is integrated for API documentation and testing, making it easy for developers to understand and interact with the API.

### Continuous Listening for New Blocks
The application continuously listens to the Bitcoin network for new blocks, ensuring that it always has the latest data.
