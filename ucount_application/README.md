# UCount Counterparty Management System - 3-Service Platform Monorepo

Enterprise microservices application for **UCount (Unified Counterparty Management System)** designed following Freddie Mac architectural patterns, multi-stage database workflows, business rule entitlement engines, ControlM batch archiving, and event-driven/JMS messaging.

---

## 🏛️ Architecture & Component Overview

```
                                [ Angular / JSP Hybrid UI ]
                                             │
                       ┌─────────────────────┴─────────────────────┐
                       ▼                                           ▼
          (Stage 1 User Intake & Auth)                (Common Comment & Auditing)
       ┌───────────────────────────────┐           ┌───────────────────────────────┐
       │   user-onboarding-service     │           │  notification-audit-service   │
       │    Port: 8081 | DB1 (H2/PG)   │           │          Port: 8083           │
       └───────────────┬───────────────┘           └───────────────▲───────────────┘
                       │                                           │
         (WebClient /  │                                           │ (ActiveMQ JMS
          Resilience4j)│                                           │  Audit Queue)
                       ▼                                           │
       ┌───────────────────────────────┐                           │
       │    access-decision-service    │───────────────────────────┘
       │    Port: 8082 | DB2 (H2/PG)   │
       │ (Stage 2 Rules & ControlM ACR)│
       └───────────────────────────────┘
```

---

## 🧱 The 3 Microservice Portfolio

| Service Name | Port | Description & Responsibilities | Data Store |
| :--- | :--- | :--- | :--- |
| **`user-onboarding-service`** | 8081 | Stage 1 Basic User Intake (Org, Address, Contact, Tax ID), Auth Credentials, WebClient REST inter-service call with Resilience4j Circuit Breaker, Kafka Producer (`ucount-user-events`). | DB1 (`ucount_stage1_db` / H2 mem) |
| **`access-decision-service`** | 8082 | Stage 2 Extended Counterparty Profile ("Big Form"), User Type Rules Engine (`HOUSE_SELLER`, `HOUSE_BUYER`, `INSURANCE_AGENT`, `MORTGAGE_SERVICER`), ControlM ACR (Annual Certificate Repair) Batch Archiving & Purging, Kafka Consumer. | DB2 (`ucount_stage2_db` / H2 mem) |
| **`notification-audit-service`** | 8083 | Common Comment API (`/api/v1/comments`) usable across all services, ActiveMQ JMS (`JmsTemplate`) Audit Logging, DLT retry logic, JSP Legacy Admin Review Page (`/admin-review`). | In-Memory Audit Store |

---

## ⚡ Architectural Patterns Implemented

1. **Database-per-Service Pattern**: Dual stage databases (DB1 for Stage 1 investigation, DB2 for Stage 2 counterparty entitlements & audit trail).
2. **REST Inter-Service Communication**: `WebClient` reactive HTTP invocation from `user-onboarding-service` to `access-decision-service`.
3. **Fault Tolerance & Resilience**: Resilience4j Circuit Breaker fallback protecting inter-service dependencies.
4. **Event-Driven Messaging**: Kafka Producer & Consumer for counterparty lifecycle events (`ucount-user-events`).
5. **JMS Queuing & Auditing**: ActiveMQ `JmsTemplate` queue dispatcher (`ucount.audit.queue`) with Dead Letter Topic (DLT) retry capability.
6. **ControlM ACR (Annual Certificate Repair) Batch Job**: Scheduled job (`@Scheduled`) archiving old counterparty records to transaction history tables and purging active DB records.
7. **Common Functionality**: Centralized Comment Management API usable across all services.
8. **Hybrid UI Rendering**: Standalone Angular components for primary web portal + JSP admin review page (`admin-review.jsp`).

---

## 🚀 How to Build & Run

### 1. Compile & Package Monorepo (Maven)
From root directory (`Ucount_Mini1`):
```bash
mvn clean package -DskipTests
```

### 2. Run Unit Tests
```bash
mvn test
```

### 3. Launch via Docker Compose (Optional)
```bash
cd deployment
docker-compose up --build -d
```
