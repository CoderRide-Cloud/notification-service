# 🔔 Notification Service

The Notification Service is an event-driven microservice responsible for delivering alerts to CoderRide users.

## 🏗️ Architecture Flow

```mermaid
graph LR
    EventService[Event Service] -- Messages --> Kafka((Kafka / Message Broker))
    AuthService[Auth Service] -- Messages --> Kafka
    
    Kafka -- Consumes --> NotificationService[Notification Service]
    
    NotificationService -- Sends --> Email[Email Server (SMTP)]
    NotificationService -- Sends --> Push[Push Notifications]
    NotificationService -- Sends --> WS[WebSockets]
```

## 🔑 Key Responsibilities
- **Asynchronous Processing**: Listening to message queues (e.g., Apache Kafka or RabbitMQ) for system events like "User Registered" or "Event Created".
- **Multi-channel Delivery**: Routing alerts via Email, SMS, or in-app WebSocket notifications depending on user preferences.
- **Decoupling**: Ensures that core services don't wait for emails to send before responding to the user.

## ⚙️ Environment Variables
Required variables in `.env` (Future):
- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_USER`
- `SMTP_PASS`

## 🛠️ Tech Stack
- **Messaging**: Kafka (Planned)
- **Port**: `8087`
