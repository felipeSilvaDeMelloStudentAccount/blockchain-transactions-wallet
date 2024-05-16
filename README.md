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
- **Maven**: Build automation tool for managing dependencies and building the project.
- **Swagger**: API documentation and testing tool.

## Setup

### Prerequisites

- Java 21 or higher
- Maven
- PostgreSQL running on `localhost:5432` with the following configuration:
    - **URL**: `jdbc:postgresql://localhost:5432/blockchain_db`
    - **Username**: `blockchain`
    - **Password**: `myPassword243OSOKpadpa`
    - **Driver Class Name**: `org.postgresql.Driver`

### Database Setup

Ensure you have a PostgreSQL database running and configured as per the above settings.

### Clone the Repository

```bash
git clone https://github.com/felipeSilvaDeMelloStudentAccount/blockchain-transactions-wallet
cd blockchain-transactions-wallet
```
### Build the Project
```bash 
mvn clean install
```
### Run the Application
```bash
mvn spring-boot:run
```

### Notes
- Ensure PostgresSQL is running and accessible with the provided configuration.
- Maven is required to build and run the application.


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
## Connecting to the Bitcoin Network
- The application connects to the Bitcoin network to receive broadcasted blocks and transactions.

## Storing Data in PostgreSQL
- The blocks and transactions are stored in a PostgreSQL database, ensuring persistence and efficient querying.

## Using Spring Boot for REST API
- Spring Boot provides a robust framework for developing RESTful web services, making it easy to create endpoints for accessing blockchain data.

## Managing Database Schema with Flyway
- Flyway is used for database migrations, ensuring that the database schema is up-to-date and consistent.

## Swagger UI for API Documentation
- Swagger UI is integrated for API documentation and testing, making it easy for developers to understand and interact with the API.

## Continuous Listening for New Blocks
- The application continuously listens to the Bitcoin network for new blocks, ensuring that it always has the latest data.

