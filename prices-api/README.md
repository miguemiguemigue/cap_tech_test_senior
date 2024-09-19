# Price API

## Description

The Price API allows querying the price of a product for a specific brand and date.

## Endpoints

### `GET /prices`

**Description**: Get price details for a product and brand on a specific date.

**Parameters**:
- `date` (required): Date and time (`date-time`).
- `product_id` (required): Product identifier (integer).
- `brand_id` (required): Brand identifier (integer).

**Responses**:
- `200 OK`: Price details.
- `400 Bad Request`: Invalid request.
- `404 Not Found`: No price information found.

## Setup MongoDB with Docker

1. **Run MongoDB Container**:
   ```bash
   docker run -d \
     --name mongodb \
     -p 27017:27017 \
     -e MONGO_INITDB_ROOT_USERNAME=root \
     -e MONGO_INITDB_ROOT_PASSWORD=12345678 \
     mongo
   ```

2. **Verify**:
   ```bash
   docker ps
   ```

## Running the Application

- **Build and Run**: Run PricesApiApplication.java
