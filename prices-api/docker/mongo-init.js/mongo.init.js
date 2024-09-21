db = db.getSiblingDB('prices_db');

db.Price.insertMany([
    {
        "brand_id": 1,
        "start_date": ISODate("2020-06-14T00:00:00Z"),
        "end_date": ISODate("2020-12-31T23:59:59Z"),
        "price_list_id": 1,
        "product_id": 35455,
        "priority": 0,
        "price": 37.50,
        "currency": "EUR"
    },
    {
        "brand_id": 1,
        "start_date": ISODate("2020-06-14T15:00:00Z"),
        "end_date": ISODate("2020-06-14T18:30:00Z"),
        "price_list_id": 2,
        "product_id": 35455,
        "priority": 1,
        "price": 25.45,
        "currency": "EUR"
    },
    {
        "brand_id": 1,
        "start_date": ISODate("2020-06-15T00:00:00Z"),
        "end_date": ISODate("2020-06-15T11:00:00Z"),
        "price_list_id": 3,
        "product_id": 35455,
        "priority": 1,
        "price": 30.50,
        "currency": "EUR"
    },
    {
        "brand_id": 1,
        "start_date": ISODate("2020-06-15T16:00:00Z"),
        "end_date": ISODate("2020-12-31T23:59:59Z"),
        "price_list_id": 4,
        "product_id": 35455,
        "priority": 1,
        "price": 38.95,
        "currency": "EUR"
    },
    {
        "brand_id": 2,
        "start_date": ISODate("2020-06-14T00:00:00Z"),
        "end_date": ISODate("2020-12-31T23:59:59Z"),
        "price_list_id": 1,
        "product_id": 35456,
        "priority": 0,
        "price": 40.00,
        "currency": "EUR"
    },
    {
        "brand_id": 2,
        "start_date": ISODate("2020-07-01T00:00:00Z"),
        "end_date": ISODate("2020-12-31T23:59:59Z"),
        "price_list_id": 2,
        "product_id": 35456,
        "priority": 2,
        "price": 45.00,
        "currency": "EUR"
    }
]);