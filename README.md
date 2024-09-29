# Order Management System

## Table of Contents
- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Kafka Integration](#kafka-integration)
- [Running the Application](#running-the-application)

## Introduction
This project is part of a case study. It simulates an **Order Management System** for an e-commerce platform. The system handles the following functionalities:
- Managing products and orders
- Calculating order totals, including taxes and discounts
- Generating invoices upon successful order creation using **Apache Kafka** for inter-service communication.

## Technologies Used
- **Java 11**  
- **Spring Boot** for creating REST APIs  
- **PostgreSQL** as the database  
- **Docker** for containerization  
- **Apache Kafka** for microservice communication  
- **JUnit** for testing  
- **Lombok** for reducing boilerplate code  

## Kafka Integration
The system uses **Apache Kafka** to communicate between the Order Management and Invoice Management modules. When an order is successfully created, an `OrderEvent` is published to a Kafka topic, which is consumed by the Invoice Management module to generate an invoice.

### Kafka Configuration:
- Topic: `order-topic`
- Producer: Sends order creation event.
- Consumer: Generates invoices based on order events.

## Running The Application
- git clone https://github.com/your-username/getmobilcasestudy.git
- mvn clean install
- docker-compose up (in order module)

- You can use the postman collection to test the controllers.


