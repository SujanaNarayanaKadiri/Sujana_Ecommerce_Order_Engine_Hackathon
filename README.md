# Distributed E-Commerce Order Engine (CLI Based)

## Project Overview

This project is a console-based simulation of a distributed e-commerce backend system similar to platforms like Amazon or Flipkart. It demonstrates how real-world systems handle inventory, carts, orders, payments, concurrency, and failures.

The system is designed using a modular service-based architecture, simulating microservices within a single application.

---

## Features

### Product Management

* Add new products
* Prevent duplicate product IDs
* Update and track inventory
* Low stock alerts

### Multi-User Cart System

* Separate cart for each user
* Add and remove items
* Manage item quantities
* Apply coupon codes

### Inventory and Stock Reservation

* Real-time stock reservation when items are added to cart
* Prevents overselling
* Stock is released on cart removal or order failure

### Concurrency Handling

* Uses ReentrantLock for thread safety
* Simulates multiple users accessing the same product
* Ensures consistent stock updates

### Order Processing Engine

* Converts cart to order
* Validates cart contents
* Calculates total and discounts
* Handles atomic transactions

### Payment Simulation

* Simulates payment success and failure
* Failure triggers rollback
* Configurable failure mode

### Transaction Rollback System

* Ensures all-or-nothing execution
* On failure:

  * Stock is restored
  * Order is marked as FAILED

### Order Lifecycle (State Machine)

* CREATED → PENDING_PAYMENT → PAID → SHIPPED → DELIVERED
* FAILED and CANCELLED states handled
* Prevents invalid transitions

### Discount and Coupon Engine

* 10% discount for orders above ₹1000
* Additional 5% discount for quantity greater than 3
* Coupon codes:

  * SAVE10 → 10% off
  * FLAT200 → ₹200 off

### Order Management

* View all orders
* Filter orders by status
* Search orders by ID

### Order Cancellation

* Cancel eligible orders
* Automatically restores stock

### Return and Refund System

* Supports partial returns
* Updates stock and order amount

### Fraud Detection System

* Flags users placing 3 or more orders within 1 minute
* Flags high-value orders above ₹50,000

### Audit Logging

* Tracks all user and system actions
* Maintains immutable logs with timestamps

### Failure Injection

* Toggle failure mode
* Simulates payment and system failures

### Idempotency Handling

* Prevents duplicate order processing
* Ensures safe retries

### Microservice Simulation

Modules:

* Product Service
* Cart Service
* Order Service
* Payment Service
* Audit Service
* Fraud Detection Service

---

## Project Structure

```plaintext
Ecommerce_Order_Engine/
 ├── models/
 │    ├── Product.java
 │    ├── Cart.java
 │    ├── Order.java
 │    ├── AuditLog.java
 │
 ├── services/
 │    ├── ProductService.java
 │    ├── CartService.java
 │    ├── OrderService.java
 │    ├── PaymentService.java
 │    ├── AuditService.java
 │    ├── FraudDetectionService.java
 │
 ├── ecommerce/
 │    └── Main.java
```

---

## How to Run

### Compile

javac ecommerce/Main.java

### Run

java ecommerce.Main

---

## CLI Menu Options

1. Add Product
2. View Products
3. Add to Cart
4. Remove from Cart
5. View Cart
6. Apply Coupon
7. Place Order
8. Cancel Order
9. View Orders
10. Low Stock Alert
11. Return Product
12. Simulate Concurrent Users
13. View Logs
14. Toggle Failure Mode
15. Exit

---

## Design Approach

* Used ConcurrentHashMap for thread-safe data handling
* Implemented locking mechanisms for concurrency control
* Designed transactional workflow with rollback support
* Modularized services to simulate microservice architecture
* Applied real-world backend design principles

---

## Assumptions

* Uses in-memory storage (no database)
* Runs in a single JVM environment
* Simplified payment processing
* No external integrations

---

## Future Enhancements

* Convert to Spring Boot REST APIs
* Integrate database such as MySQL or PostgreSQL
* Implement authentication using JWT
* Add frontend using React
* Deploy as microservices using Docker

---

## Author

Kadiri Sujana Narayana

