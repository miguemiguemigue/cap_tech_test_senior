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
cd cap_tech_test_senior/prices-api
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

### 6. Mongock populates the database with prices of all kind

A variety of prices are inserted in MongoDB by Mongock to test the API

| ID  | Product ID | Brand ID | Price List ID | Start Date           | End Date             | Priority | Price  | Currency |
|-----|------------|----------|---------------|----------------------|----------------------|----------|--------|----------|
| 1   | 1          | 1        | 1             | 2018-06-14T00:00:00  | 2018-12-31T23:59:59  | 1        | 35.50  | EUR      |
| 2   | 1          | 1        | 1             | 2018-06-14T10:00:00  | 2018-10-14T15:00:00  | 2        | 40.00  | EUR      |
| 3   | 2          | 2        | 3             | 2019-06-14T00:00:00  | 2019-07-14T23:59:59  | 1        | 25.75  | EUR      |
| 4   | 4          | 2        | 4             | 2019-07-01T00:00:00  | 2019-12-31T23:59:59  | 2        | 28.50  | EUR      |
| 5   | 13         | 10       | 5             | 2020-08-01T00:00:00  | 2020-08-31T23:59:59  | 1        | 50.00  | EUR      |
| 6   | 13         | 11       | 6             | 2020-08-15T00:00:00  | 2020-09-15T23:59:59  | 1        | 52.00  | EUR      |
| 7   | 5          | 3        | 9             | 2021-09-01T00:00:00  | 2021-09-30T23:59:59  | 1        | 70.00  | EUR      |
| 8   | 6          | 3        | 10            | 2021-10-01T00:00:00  | 2021-12-31T23:59:59  | 1        | 75.00  | EUR      |

* Price 1 and 2: Dates overlap, same product and brand, different priority
* Price 3 and 4: Dates overlap, same brand and different product
* Price 5 and 6: Dates overlap, same product and different brand
* Price 7 and 8: Dates do not overlap, different product and brand

### 7. Postman collection test

Integration and persistence tests are already providing confidence about the API behaviour, but you can import
a postman collection that contains tests, and reuse it to call the API the way you need.

Import ../postman/Price API.postman_collection.json to Postman.

Run collection to pass tests.

Create new request to test whatever you need.

#### Tests of the Postman collection
* Check 400 BAD REQUEST for invalid parameters
* Check 404 NOT FOUND for not found price
* Check 200 OK, obtaining the price with top priority (Price 2 of the Mongock populated data)

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
