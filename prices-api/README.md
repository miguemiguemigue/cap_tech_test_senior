# Price API

## Description

The Price API allows querying the price of a product for a specific brand and date.

## Main features
* Spring boot 3
* Java 17
* Reactive Programming with Spring WebFlux and Mongo Reactive
* OpenAPI yml integration to define API endpoint
* Hexagonal architecture
* Tests
  * Unit tests
  * Persistence and integration tests using TestContainers for MongoDB
* Mongock for MongoDB evolution control

## Requirements

- JDK 17
- Maven 3.8.X
- Docker (for MongoDB)

## Steps to test it
### 1. Clone the Repository

First, clone the repository using the following command:

```bash
git clone https://github.com/miguemiguemigue/cap_tech_test_senior.git
cd cap_tech_test_senior
```

### 2. Compile the project

Make sure you are in the project directory and run the following command to compile it:

```bash
mvn clean install
```

### 3. Run tests

To run the tests for the project, use the following command:

```bash
mvn test
```

### 4. Run MongoDB with Docker

Before starting the application, you need to run a MongoDB image. Use the following command to start the MongoDB container with username `root` and password `12345678`:

```bash
docker run --name mongodb -d -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=12345678 -p 27017:27017 mongo:7.0
```

### 5. Run the Application

Once the MongoDB container is running, you can start the application with the following command:

```bash
mvn spring-boot:run
```

Mongock will populate the database with prices of all kind



## Endpoints

### `GET /prices`

**Description**: Get price details for a product and brand on a specific date.

**Parameters**:
- `date` (required): Date and time (`date-time`). Example: "2018-06-16T00:00:00"
- `product_id` (required): Product identifier (integer).
- `brand_id` (required): Brand identifier (integer).

**Responses**:
- `200 OK`: Price details.
- `400 Bad Request`: Invalid request.
- `404 Not Found`: No price information found.
