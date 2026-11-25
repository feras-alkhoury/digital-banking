Digital Banking Core Platform
A backend banking platform built with Spring Boot, PostgreSQL, Kafka, Keycloak, Flyway, JPA, and OpenAPI.
The platform provides account transfers, double-entry ledger processing, idempotency, fraud controls, KYC workflow, Kafka events, account statements, and OAuth2/JWT authentication.

⸻

Features
• Customer and bank account management
• Account-to-account transfers
• Double-entry bookkeeping
• Atomic database transactions
• Idempotent transfers
• Transfer amount and velocity fraud rules
• KYC submission, approval, and rejection
• Kafka transfer events
• Kafka event consumer
• Account statements
• Keycloak OAuth2 / JWT authentication
• Protected REST APIs
• Swagger / OpenAPI documentation
• PostgreSQL persistence
• Flyway database migrations
• Dockerized Kafka infrastructure

⸻

Architecture
                         +-—————--------------——+
                         |     Client / User    |
                         +-———------+-------————+
                                    |
                                    v
                         +-———————--------------+
                         |   Swagger / REST API |
                         +-———————--+-----------+
                                    |
                                    v
                         +-———————--------------+
                         |      Keycloak        |
                         |   OAuth2 / JWT       |
                         +-———————--+-----------+
                                    |
                          Bearer JWT validation
                                    |
                                    v
                    +——————————-----+---------------+
                    |       Spring Boot API         |
                    |                               |
                    |  Transfer   KYC   Statement   |
                    +-——————-+————------------------+
                             |
             +—————+—————+---+-------------+
             |                             |
             v                             v
      +-————--------+              +—————----------+
      | PostgreSQL  |              |     Kafka     |
      |             |              | completed-    |
      | Customers   |              | transfers     |
      | Accounts    |              +-————----------+
      | Transactions|                      |
      | Ledger      |                      v
      | KYC         |              +—————-----------+
      +-————--------+              | Kafka Consumer |
                                   +—————-----------+

⸻

Authentication and Security
The API is protected using Keycloak OAuth2 with JWT bearer tokens.
Authentication flow:
User
  |
  v
Keycloak
  |
  | OAuth2 Authorization Code
  v
Access Token (JWT)
  |
  v
Swagger / API Client
  |
  | Authorization: Bearer <token>
  v
Spring Boot Resource Server
  |
  v
Protected API
Public API documentation endpoints:
/swagger-ui/**
/v3/api-docs/**
Application endpoints require authentication.
Keycloak realm:
digital-banking
OAuth2 issuer:
http://localhost:8081/realms/digital-banking

⸻

Transfer Flow
A transfer follows these steps:
1. Client authenticates with Keycloak.
2. Client sends a transfer request with a JWT.
3. API validates the request.
4. Idempotency key is checked.
5. Fraud rules are evaluated.
6. Sender balance is decreased.
7. Receiver balance is increased.
8. Transaction is stored.
9. Debit and credit ledger entries are created.
10. The completed transfer event is published to Kafka.
11. Kafka consumer receives the event.
All database changes occur inside a transactional operation.

⸻

Double-Entry Ledger
Every successful transfer creates two ledger entries.
Example for a $200 transfer:
Sender:
DEBIT   -200.00

Receiver:
CREDIT  +200.00

-—————
Total      0.00
The ledger therefore remains balanced for every completed transfer.

⸻

Idempotency
Transfers require a unique idempotencyKey.
Example:
{
  “fromAccountId”: 4,
  “toAccountId”: 3,
  “amount”: 100,
  “idempotencyKey”: “demo-100-1”
}
First request:
Transfer successful
Repeating the same request with the same idempotency key:
Transfer already processed
This prevents accidental duplicate transactions.

⸻

Fraud Rules
The current implementation includes simple fraud controls:
Transfer amount limit
Maximum transfer amount:
5000
Transfers above this limit are rejected.
Transfer velocity
An account is limited to:
3 outgoing transfers
within the implemented transaction-count rule.
Rejected requests return:
400 Bad Request

⸻

KYC Workflow
The platform provides a simple KYC lifecycle:
PENDING
   |
   +-—> APPROVED
   |
   +-—> REJECTED
Submit KYC
POST /api/kyc?customerId={id}
Get KYC
GET /api/kyc/{id}
Approve KYC
PUT /api/kyc/{id}/approve
Reject KYC
PUT /api/kyc/{id}/reject

⸻

Account Statements
GET /api/accounts/{accountId}/statement
Returns the ledger entries associated with an account.
Example:
[
  {
    “id”: 5,
    “entryType”: “CREDIT”,
    “amount”: 200.00,
    “createdAt”: “2026-11-25T...”
  }
]

⸻

Kafka
Completed transfers generate events on:
completed-transfers
Example event:
{
  “transactionId”: 4,
  “fromAccountId”: 4,
  “toAccountId”: 3,
  “amount”: 200,
  “status”: “COMPLETED”
}
The Kafka consumer listens to the same topic and processes completed transfer events.
Kafka is used to demonstrate asynchronous event-driven processing for completed transactions.

⸻

Database
PostgreSQL database:
digital_banking
Main tables:
customers
accounts
transactions
ledger_entries
kyc
Relationships
customers
    |
    +-— accounts
              |
              +-— transactions
              |
              +-— ledger_entries

customers
    |
    +-— kyc

⸻

API Endpoints
Transfers
POST /api/transfers
Account Statements
GET /api/accounts/{accountId}/statement
KYC
POST /api/kyc?customerId={id}
GET  /api/kyc/{id}
PUT  /api/kyc/{id}/approve
PUT  /api/kyc/{id}/reject
All application endpoints require a valid OAuth2/JWT access token.

⸻

Swagger / OpenAPI
Swagger UI:
http://localhost:8080/swagger-ui/index.html
The Swagger UI is configured with OAuth2 Authorization Code flow and integrates with the Keycloak realm.
After authentication, Swagger sends the JWT as a Bearer token when calling protected endpoints.

⸻

Technologies
• Java 21
• Spring Boot 4.2
• Spring Web
• Spring Data JPA
• Spring Security
• OAuth2 Resource Server
• Keycloak 26.8.2
• PostgreSQL
• Flyway
• Apache Kafka 4.0.1
• Spring Kafka
• Docker
• OpenAPI / Swagger

⸻

Project Structure
digital-banking/
│
├── src/
│   └── main/
│       ├── java/com/nexvia/digital_banking/
│       │
│       │── Account.java
│       │── AccountRepository.java
│       │── BankTransaction.java
│       │── Customer.java
│       │── DigitalBankingApplication.java
│       │── GlobalExceptionHandler.java
│       │── Kyc.java
│       │── KycController.java
│       │── KycRepository.java
│       │── LedgerEntry.java
│       │── LedgerEntryRepository.java
│       │── OpenApiConfig.java
│       │── SecurityConfig.java
│       │── StatementController.java
│       │── TransactionRepository.java
│       │── TransferController.java
│       │── TransferEvent.java
│       │── TransferEventConsumer.java
│       │── TransferEventProducer.java
│       │── TransferRequest.java
│       │── TransferService.java
│
│       └── resources/
│           ├── db/migration/
│           └── application.properties
│
├── docker-compose.yml
├── pom.xml
└── README.md

⸻

Running the Project
1. Start infrastructure
docker compose up -d
This starts the Kafka infrastructure and Keycloak services configured for the project.
2. Start Spring Boot
Windows:
.\mvnw.cmd spring-boot:run
3. Open Swagger
http://localhost:8080/swagger-ui/index.html
4. Authenticate
Use the Swagger Authorize button and authenticate through the digital-banking Keycloak realm.
After successful authentication, protected endpoints can be executed from Swagger.

⸻

Authentication Verification
The authentication flow was verified through Swagger:
Swagger
   |
   v
Keycloak login
   |
   v
OAuth2 authorization
   |
   v
JWT access token
   |
   v
Protected Spring Boot endpoint
   |
   v
200 OK
A protected account statement endpoint was successfully executed after authentication.

⸻

Testing
The following functionality was manually verified through Swagger and the running application:
• Successful account transfer
• Duplicate transfer protection using idempotency keys
• Insufficient balance validation
• Transfer amount fraud limit
• Transfer velocity rule
• Double-entry ledger creation
• Account statement retrieval
• KYC submission
• KYC approval
• KYC rejection
• Kafka producer
• Kafka consumer
• OAuth2 authentication through Keycloak
• JWT-protected API access
• Swagger OAuth2 authorization
Automated tests are also included in the project.
Run:
.\mvnw.cmd test

⸻

Project Status
Core Banking
• ☑ Customers
• ☑ Accounts
• ☑ Transfers
• ☑ Atomic transactions
• ☑ Double-entry ledger
• ☑ Idempotency
• ☑ Fraud rules
• ☑ Account statements
Security
• ☑ Keycloak
• ☑ OAuth2 Authorization Code flow
• ☑ JWT authentication
• ☑ Protected API endpoints
• ☑ Swagger OAuth2 integration
KYC
• ☑ KYC submission
• ☑ KYC retrieval
• ☑ KYC approval
• ☑ KYC rejection
Event-Driven Processing
• ☑ Kafka producer
• ☑ Kafka topic
• ☑ Kafka consumer
• ☑ Completed transfer events
Documentation
• ☑ Swagger / OpenAPI
• ☑ README
• ☑ Architecture documentation
• ☑ API documentation
• ☑ Test documentation

⸻

Future Improvements
• Role-based authorization for separate customer/admin permissions
• Advanced fraud detection
• Notification service
• Production deployment
• Distributed tracing
• Metrics and monitoring
• Comprehensive load and performance testing
• High-availability Kafka configuration
• Automated integration testing with containers

⸻

Project Deliverables
The project demonstrates:
1. Banking account and transfer processing
2. Double-entry financial ledger
3. Atomic database operations
4. Idempotent transaction handling
5. Fraud prevention rules
6. KYC workflow
7. Event-driven Kafka processing
8. OAuth2/JWT security with Keycloak
9. Protected REST APIs
10. OpenAPI documentation