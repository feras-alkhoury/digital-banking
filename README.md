Digital Banking Core Platform

A backend digital banking platform developed using Java 21, Spring Boot, PostgreSQL, Apache Kafka, and Keycloak.

The system provides core banking operations, including customer and account management, secure money transfers, double-entry bookkeeping, transaction processing, fraud prevention, and KYC management.

Developed as part of the Nexvia Technologies Backend Development Internship.

⸻

Features

Banking Operations

* Customer management
* Bank account management
* Account-to-account money transfers
* Account statements
* Transaction history

Transaction Processing

* Double-entry ledger bookkeeping
* Atomic database transactions
* Idempotency protection against duplicate transfers
* Sender and receiver balance updates

Fraud Prevention

* Maximum transfer amount validation
* Transaction velocity limits
* Transfer validation before processing

KYC Management

* KYC submission
* KYC status retrieval
* KYC approval and rejection

Event-Driven Communication

* Kafka transfer event publishing
* Kafka event consumer
* Asynchronous event processing

Security

* Keycloak identity provider
* OAuth2 Authorization Code Flow
* JWT bearer token authentication
* Spring Security Resource Server
* Protected REST API endpoints

⸻

Technology Stack

Technology	Purpose
Java 21	Programming language
Spring Boot 4.2.0	Backend framework
Spring Security	Authentication and authorization
Spring Data JPA	Database persistence
PostgreSQL 18	Relational database
Apache Kafka 4.0.1	Event-driven messaging
Keycloak 26.8.2	Identity and access management
Flyway	Database schema migrations
Swagger / OpenAPI	API documentation
Docker Compose	Infrastructure management
Maven	Build and dependency management

⸻

Architecture

The application follows a layered backend architecture with separate responsibilities for API controllers, business logic, persistence, authentication, and event messaging.

flowchart TB
    U["Client / User"]
    SW["Swagger UI / REST API"]
    KC["Keycloak<br/>OAuth2 / JWT"]
    subgraph APP["Spring Boot Application"]
        SEC["SecurityConfig"]
        CTRL["REST Controllers"]
        SERVICE["TransferService"]
        REPO["Repositories"]
        EVENT["Kafka Producer / Consumer"]
        ERR["GlobalExceptionHandler"]
    end
    DB[("PostgreSQL")]
    KAFKA[["Apache Kafka"]]
    U --> SW
    SW --> SEC
    KC --> SEC
    SEC --> CTRL
    CTRL --> SERVICE
    SERVICE --> REPO
    REPO <--> DB
    SERVICE --> EVENT
    EVENT <--> KAFKA
    CTRL -.-> ERR

Main Components

Component	Responsibility
Controllers	Handle incoming REST API requests
TransferService	Manage transfer business logic
Repositories	Handle database access
SecurityConfig	Configure API security
OpenApiConfig	Configure Swagger OAuth2 documentation
TransferEventProducer	Publish transfer events
TransferEventConsumer	Consume Kafka events
GlobalExceptionHandler	Handle application exceptions
Flyway	Manage database migrations

⸻

Project Structure

digital-banking/

│

├── .mvn/

│

├── docs/

│   └── architecture.png

│

├── src/

│   │

│   ├── main/

│   │   │

│   │   ├── java/com/nexvia/digital_banking/

│   │   │   │

│   │   │   ├── DigitalBankingApplication.java

│   │   │   │

│   │   │   ├── Account.java

│   │   │   ├── AccountRepository.java

│   │   │   ├── Customer.java

│   │   │   ├── BankTransaction.java

│   │   │   ├── TransactionRepository.java

│   │   │   ├── LedgerEntry.java

│   │   │   ├── LedgerEntryRepository.java

│   │   │   │

│   │   │   ├── TransferController.java

│   │   │   ├── TransferService.java

│   │   │   ├── TransferRequest.java

│   │   │   │

│   │   │   ├── TransferEvent.java

│   │   │   ├── TransferEventProducer.java

│   │   │   ├── TransferEventConsumer.java

│   │   │   │

│   │   │   ├── StatementController.java

│   │   │   │

│   │   │   ├── Kyc.java

│   │   │   ├── KycController.java

│   │   │   ├── KycRepository.java

│   │   │   │

│   │   │   ├── SecurityConfig.java

│   │   │   ├── OpenApiConfig.java

│   │   │   └── GlobalExceptionHandler.java

│   │   │

│   │   └── resources/

│   │       │

│   │       ├── application.properties

│   │       │

│   │       └── db/

│   │           └── migration/

│   │               ├── V1__create_core_tables.sql

│   │               └── V2__create_kyc.sql

│   │

│   └── test/

│       └── java/com/nexvia/digital_banking/

│           └── DigitalBankingApplicationTests.java

│

├── docker-compose.yml

├── pom.xml

├── mvnw

├── mvnw.cmd

├── .gitignore

├── .gitattributes

└── README.md


⸻

Database & Migrations

PostgreSQL is used as the primary relational database.

Flyway manages database schema migrations.

Current migration files:

* V1__create_core_tables.sql — Core banking tables.
* V2__create_kyc.sql — KYC table.

⸻

Authentication & Security

The application uses Keycloak for identity management and OAuth2 authentication.

Authentication Flow

1. The user opens Swagger UI.
2. The user authenticates through Keycloak.
3. Keycloak issues an access token.
4. Swagger sends the JWT bearer token with API requests.
5. Spring Security validates the token.
6. Authenticated users can access protected endpoints.

Keycloak Realm: digital-banking

Authentication Protocol: OAuth2 Authorization Code Flow

Token Type: JWT Bearer Token

Public Documentation Endpoints

* /swagger-ui/**
* /v3/api-docs/**

Other API endpoints require authentication.

⸻

Kafka Event Processing

Apache Kafka is used for transfer event messaging.

The application includes:

* TransferEvent — Transfer event data.
* TransferEventProducer — Publishes transfer events.
* TransferEventConsumer — Receives and processes events.

This provides an event-driven communication mechanism for transfer-related operations.

⸻

Running the Application

Prerequisites

* Java 21
* Maven
* PostgreSQL
* Docker
* Apache Kafka
* Keycloak

1. Clone the Repository

git clone https://github.com/feras-alkhoury/digital-banking.git
cd digital-banking

2. Configure Application Properties

Update src/main/resources/application.properties with your local PostgreSQL, Kafka, and Keycloak configuration.

Do not commit passwords, secrets, or production credentials.

3. Start Infrastructure

Start the required infrastructure services using your configured Docker Compose setup.

docker compose up -d

Ensure PostgreSQL, Kafka, and Keycloak are available with the configured connection settings.

4. Run the Application

On Windows:

.\mvnw.cmd spring-boot:run

On Linux / macOS:

./mvnw spring-boot:run

⸻

API Documentation

After starting the application, open:

Swagger UI

http://localhost:8080/swagger-ui/index.html

Swagger provides interactive API documentation and allows authenticated endpoint testing.

⸻

Testing

The project was tested using Maven:

.\mvnw.cmd clean test

Latest recorded test results:

Test Result	Count
Tests run	1
Failures	0
Errors	0
Skipped	0

Build Status: BUILD SUCCESS

⸻

Repository

GitHub: https://github.com/feras-alkhoury/digital-banking

⸻

Author

Feras Alkhoury

Backend Development Internship Project — Nexvia Technologies
