# Sujana_Ecommerce_Order_Engine_Hackathon 

## Project Overview

This project is a console-based simulation of a distributed e-commerce backend system similar to platforms like Amazon or Flipkart. It demonstrates how real-world systems handle inventory, carts, orders, payments, concurrency, and failures.

The system is designed using a modular service-based architecture, simulating microservices within a single application.

---

## Features Implemented 
Product and inventory management
Multi-user cart system
Stock reservation to prevent overselling
Order placement with rollback on failure
Payment simulation (success/failure)
Order lifecycle management
Discount and coupon support
Fraud detection and audit logging
Concurrency handling using locks

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

## How to Run the Project

### Compile

javac ecommerce/Main.java

### Run

java ecommerce.Main


