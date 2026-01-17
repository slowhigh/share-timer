# 🕒 ShareTimer

**ShareTimer** is a web service that lets you create custom timers and share them instantly with others.  
Whether you're coordinating an online study session, managing a team sprint, or setting a countdown for an event, ShareTimer makes time management collaborative and effortless.

---

## Features

- **Create** — Set up timers in seconds with a clean, intuitive interface.  
- **Share** — Generate a unique link to share your timer with anyone, anywhere.  
- **Sync (SSE)** — All timer events (updates, timestamp additions, and expirations) are synchronized to connected clients using Server‑Sent Events (SSE).
- **Flexible** — Works on any device, no login required.

---

## How to Run
### Using Docker Compose
To start the service with Docker, simply run the following command from the project root directory, where the compose.yaml file is located:
```
docker compose up -d
```

### Access the Web Interface
Once the containers are running, open your browser and navigate to:
- http://localhost:8080/

### API Documentation
#### API Service Documentation
Access the main API Service’s Swagger documentation at:
- http://localhost:8080/api/v1/swagger-ui/index.html

#### Sync Service Documentation
View the Sync Service’s Swagger documentation here:
- http://localhost:8080/sync/v1/swagger-ui/index.html

### Monitoring
Access the Eureka dashboard at:
- http://localhost:8761/

Access the Prometheus dashboard at:
- http://localhost:9090/targets

Access the Grafana dashboard at:
- http://localhost:3000/
- default account: admin / admin

---

## Architecture Overview

The ShareTimer system is composed of the following main components:
- **Client (Owner / Guest)** — Front-end web application. Opens an SSE stream for real‑time events and sends REST requests to manage timers.
- **API Service** — Core business logic service. Provides REST endpoints for creating, updating, and retrieving timers and timestamps.
- **Sync Service (SSE Gateway)** — Real-time event handler. Exposes SSE endpoints for client subscriptions, delivers ordered events, and bridges internal pub/sub messages.
- **API Gateway** — Unified entry point. Built with Spring Cloud Gateway, it routes traffic to backend services and handles cross-cutting concerns.
- **Discovery Service** — Service registry based on Netflix Eureka. Enables dynamic registration and discovery, allowing services to communicate without hardcoded URLs.
- **PostgreSQL** — Durable relational database. Stores persistent data including timer configurations and timestamp history.
- **Redis** — In-memory data store. Manages TTL-based expiration and acts as a pub/sub message broker for real-time synchronization.
- **Prometheus** — Monitoring and metrics collection. Tracks service performance and provides visibility into system health.
- **Grafana** — Visualization tool. Displays metrics and provides a user-friendly interface for monitoring system performance.

```mermaid
graph TD
    %% Nodes
    Client[Client Web App]
    subgraph "Infrastructure"
        Gateway[API Gateway]
        Discovery[Discovery Service]
    end
    
    subgraph "Microservices"
        API[API Service]
        Sync[Sync Service]
    end
    
    subgraph "Persistence"
        PG[(PostgreSQL)]
        Redis[(Redis)]
    end
    
    subgraph "Observability"
        Prometheus[Prometheus]
        Grafana[Grafana]
    end

    %% Connections
    Client -- "REST / SSE" --> Gateway
    Gateway -- "Route / Load Balance" --> API
    Gateway -- "Route SSE" --> Sync
    
    API -- "Read/Write" --> PG
    API -- "Publish Events" --> Redis
    Sync -- "Subscribe Events" --> Redis
    
    API -. "Register" .-> Discovery
    Sync -. "Register" .-> Discovery
    Gateway -. "Discover" .-> Discovery
    
    Prometheus -- "Scrape Metrics" --> Gateway
    Prometheus -- "Scrape Metrics" --> API
    Prometheus -- "Scrape Metrics" --> Sync
    Grafana -- "Visualize" --> Prometheus
```

---

## Server Architecture

The server codebase is structured as a **Monorepo** and follows **Hexagonal Architecture** principles to ensure maintainability and scalability.

### Monorepo Structure

The `server` directory is organized into `apps` and `libs`:

- **apps/**: Contains deployable microservices.
    - `api-service`: Core business logic.
    - `sync-service`: Real-time SSE gateway.
    - `api-gateway`, `discovery-server`: Infrastructure services.
- **libs/**: Contains shared reusable libraries.
    - `common`: Common utilities.
    - `db-jpa`: Database persistence layers.
    - `storage-redis`: Redis client and configuration.
    - `web-support`: Shared web configuration and filters.

### Hexagonal Architecture

Services like `api-service` and `sync-service` are designed using Hexagonal Architecture (Ports and Adapters) to isolate core business logic from external dependencies.

- **Domain Layer (`domain`)**: Contains the core business logic and entities. This layer has no dependencies on frameworks or external details.
- **Application Layer (`application`)**: Orchestrates use cases (`port.in`) and defines interfaces for external resources (`port.out`).
- **Adapter Layer (`adapter`)**: Implements the interfaces to interact with the outside world.
    - **Inbound Adapters (`web`, `listener`)**: Handle REST API requests and message/event listeners.
    - **Outbound Adapters (`persistence`, `redis`, `external`)**: Communicate with databases, message brokers, or external services.

#### API Service Class Diagram

```mermaid
classDiagram
    %% Domain Layer
    namespace Domain {
        class Timer {
            -UUID id
            -UUID ownerToken
            -Instant targetTime
            -Instant updatedAt
            -List~Timestamp~ timestamps
            +updateTargetTime(Instant)
        }
        class Timestamp {
            -Long id
            -Instant targetTime
            -Instant capturedAt
            -Timer timer
        }
    }
    Timer "1" *-- "*" Timestamp : contains

    %% Application Layer - Input Ports
    namespace Application_Input_Port {
        class TimerUseCase {
            <<interface>>
            +createTimer(CreateTimerCommand)
            +getTimerInfo(GetTimerCommand)
            +updateTimer(UpdateTimerCommand)
            +deleteTimer(DeleteTimerCommand)
            +addTimestamp(AddTimestampCommand)
        }
    }

    %% Application Layer - Output Ports
    namespace Application_Output_Port {
        class LoadTimerPort {
            <<interface>>
            +loadTimer(UUID)
        }
        class SaveTimerPort {
            <<interface>>
            +saveTimer(Timer)
            +deleteTimer(UUID)
        }
        class TimerEventPort {
            <<interface>>
            +scheduleExpiration(String, Instant)
            +publishUpdateTimerTargetTime(String, Instant, Instant)
            +publishAddTimestamp(String, Instant, Instant)
        }
    }

    %% Application Layer - Service
    namespace Application_Service {
        class TimerServiceImpl {
            -LoadTimerPort loadTimerPort
            -SaveTimerPort saveTimerPort
            -TimerEventPort timerEventPort
            +createTimer(CreateTimerCommand)
            +getTimerInfo(GetTimerCommand)
            +updateTimer(UpdateTimerCommand)
            +deleteTimer(DeleteTimerCommand)
            +addTimestamp(AddTimestampCommand)
        }
    }

    TimerUseCase <|.. TimerServiceImpl : implements
    TimerServiceImpl --> LoadTimerPort : uses
    TimerServiceImpl --> SaveTimerPort : uses
    TimerServiceImpl --> TimerEventPort : uses
    TimerServiceImpl ..> Timer : manipulates

    %% Adapter Layer - In (Web)
    namespace Adapter_In_Web {
        class TimerController {
            -TimerUseCase timerUseCase
            +createTimer(TimerCreateReq)
            +getTimerInfo(String, String)
            +updateTimer(String, String, TimerUpdateReq)
            +deleteTimer(String)
            +addTimestamp(String, TimerAddTimestampReq)
        }
    }
    TimerController --> TimerUseCase : calls

    %% Adapter Layer - Out (Persistence)
    namespace Adapter_Out_Persistence {
        class TimerPersistenceAdapter {
            -TimerRepository timerRepository
            +loadTimer(UUID)
            +saveTimer(Timer)
            +deleteTimer(UUID)
        }
        class TimerRepository {
            <<interface>>
            +findWithTimestampsById(UUID)
        }
    }
    LoadTimerPort <|.. TimerPersistenceAdapter : implements
    SaveTimerPort <|.. TimerPersistenceAdapter : implements
    TimerPersistenceAdapter --> TimerRepository : uses
    TimerRepository ..> Timer : returns

    %% Adapter Layer - Out (Redis)
    namespace Adapter_Out_Redis {
        class TimerRedisPublisher {
            -StringRedisTemplate timerExpirationRedisTemplate
            -StringRedisTemplate timerPubSubRedisTemplate
            +scheduleExpiration(String, Instant)
            +publishUpdateTimerTargetTime(...)
            +publishAddTimestamp(...)
        }
    }
    TimerEventPort <|.. TimerRedisPublisher : implements
```

---

## Sequence Diagrams

### Creating a Timer

The user configures a timer and sends a creation request to the API Service.
The server validates the input, stores the timer in PostgreSQL, and returns a unique shareable Id.

![Create Timer Sequence](./docs/start-timer-sequnce-diagram.png)

### Joining a Shared Timer

Another user accesses the timer through its shared link.
The Client retrieves timer details via the API Service and displays the synchronized countdown UI.

![Join Timer Sequence](./docs/share-timer-sequnce-diagram.png)

### Real-Time Synchronization

Clients connect to the Sync Service via SSE.
When the owner adds a timestamp or changes the timer, the API Service updates the database and uses Redis to alert the Sync Service.
All linked clients are instantly informed of any changes to the timer's status, guaranteeing shared real-time consistency.

![Realtime Sync Sequence 1](./docs/add-timestamp-sequnce-diagram.png)
![Realtime Sync Sequence 2](./docs/update-timer-sequnce-diagram.png)

### Timer Expiration & Notification

When the countdown reaches zero, Redis triggers an expiration event.
Every client modifies their UI in response to a timer-expired event published by the Sync Service.

![Expiration Sequence](./docs/expire-timer-sequnce-diagram.png)
