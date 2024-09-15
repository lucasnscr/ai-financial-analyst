

![Image description](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/gufr7497jybvux2sd6hv.png)

### 1. **Load Balancer**

The Load Balancer is responsible for distributing incoming traffic across multiple instances of the API Gateway and microservices to ensure high availability and fault tolerance. It helps to scale the system horizontally by adding more instances as needed and provides failover in case of service outages.

### 2. **API Gateway**

The **API Gateway** serves as the single entry point for all client requests (mobile app, web app, etc.). It manages authentication, authorization, rate-limiting, and routing requests to appropriate microservices. Additionally, it applies security policies and provides load balancing for downstream services. The API Gateway can also be extended to handle API versioning, caching, and request/response transformation.

### 3. **Microservices Architecture**

To build a scalable and modular application, we are adopting a **microservices architecture**. Each domain is designed around **Domain-Driven Design (DDD)** principles, ensuring that each microservice is isolated and handles specific business capabilities. The services communicate through **event-driven architecture** using **Kafka** for asynchronous messaging and **REST APIs** for synchronous calls.

#### A. **Goals Domain**

- **Description**: 
  The Goals domain manages all operations related to the creation, modification, deletion, and progress tracking of customers' financial goals. 

- **Microservices**:
  1. **Goals Management Service**:
     - Handles CRUD operations (Create, Read, Update, Delete) for financial goals.
     - Interfaces with the user to manage goal settings and configurations.
     - Stores goal data in its own database and communicates with other services using Kafka events.
  
  2. **Goals Progress Service**:
     - Monitors and tracks the progress of users' financial goals.
     - Continuously recalculates progress based on user transactions and external inputs (e.g., market conditions).
     - Provides real-time goal progress insights via APIs or events to other services like Notifications.

- **Integration**:  
  - **Pub/Sub (Kafka Events)**:
    - **Goals Management Service**: Publishes events such as `GoalCreated`, `GoalUpdated`, and `GoalDeleted`.
    - **Goals Progress Service**: Subscribes to goal-related events to update and track progress.
    - **Notification Service**: Subscribes to these events to notify users of any goal changes or milestones.

#### B. **Financial Product Domain**

- **Description**: 
  The Financial Product domain focuses on personalized recommendations of financial products (e.g., savings accounts, loans, or investment options) based on users' goals and financial data.

- **Microservices**:
  1. **Product Catalog Service**:
     - Maintains a database of available financial products.
     - Provides search and filtering options for various products based on user needs and preferences.
  
  2. **Product Recommendation Engine**:
     - Analyzes user goals and profiles to suggest relevant financial products using machine learning models.
     - Continuously updates recommendations based on real-time data.

- **Integration**:  
  - **Pub/Sub (Kafka Events)**:
    - **Product Recommendation Engine**: Publishes `ProductRecommended` events.
    - **Notification Service**: Subscribes to inform users about new or relevant product recommendations.
    - **Analytics Service**: Subscribes to track the effectiveness of recommendations.

#### C. **Notifications Domain**

- **Description**: 
  This domain manages all user notifications (push, SMS, email) and ensures timely delivery based on critical events, such as missed targets or new product recommendations.

- **Microservices**:
  1. **Notification Alert Service**:
     - Receives various events from other microservices and triggers notifications based on predefined rules (e.g., goal achievement, transaction success, or product updates).
     - Manages user preferences for receiving alerts.

- **Integration**:
  - **Pub/Sub (Kafka Events)**:
    - **Notification Alert Service**: Publishes `NotificationSent` events and subscribes to relevant events like `GoalAchieved`, `ProductRecommended`, and `TransactionProcessed` to issue alerts.

#### D. **User Domain**

- **Description**: 
  The User domain is responsible for managing user profiles, preferences, and authentication.

- **Microservices**:
  1. **User Profile Service**:
     - Stores and manages user data, including personal information, preferences, and settings.
     - Handles updates to user profiles and publishes profile update events.

- **Integration**:
  - **Pub/Sub (Kafka Events)**:
    - **User Profile Service**: Publishes `UserProfileUpdated` and subscribes to events related to user actions.
    - **Notification Service**: Subscribes to notify users when profile changes occur.

#### E. **Transactions Domain**

- **Description**: 
  This domain manages financial transactions like deposits, withdrawals, and transfers, focusing on tracking contributions to user financial goals.

- **Microservices**:
  1. **Transaction Service**:
     - Processes all types of financial transactions.
     - Updates goal progress based on successful transactions and triggers relevant events.

- **Integration**:  
  - **Pub/Sub (Kafka Events)**:
    - **Transaction Service**: Publishes `TransactionProcessed`, `DepositCompleted`, and `WithdrawalCompleted` events.
    - **Goals Progress Service**: Subscribes to these events to update goal progress accordingly.
    - **Analytics Service**: Subscribes for cash flow and behavior analysis.

### 4. **Event-Driven Architecture Using Kafka**

Kafka is used to implement an **event-driven architecture** that decouples services and enables asynchronous communication between them. This allows services to publish and subscribe to events efficiently, improving the scalability and resilience of the system.

- **Topic: financial-goals**:
  - Manages events like `GoalCreated`, `GoalUpdated`, `GoalDeleted`, and `GoalProgressUpdated`.
  - Subscribers: Goals Progress Service, Notification Service, Analytics Service.

- **Topic: product-recommendations**:
  - Publishes `ProductRecommended` events.
  - Subscribers: Notification Service, Analytics Service.

- **Topic: transactions**:
  - Handles `TransactionProcessed`, `DepositCompleted`, and `WithdrawalCompleted` events.
  - Subscribers: Goals Progress Service, Notification Service, Analytics Service.

- **Topic: user-profile**:
  - Publishes events such as `UserProfileUpdated`, `UserAuthenticated`, and `UserLoggedIn`.
  - Subscribers: Notification Service, Analytics Service.

- **Topic: notifications**:
  - Publishes `NotificationSent` events.
  - Subscribers: Analytics Service, Goals Progress Service.

---

This architecture emphasizes modularity, scalability, and event-driven communication between services. The use of Kafka ensures that microservices are loosely coupled, with clear boundaries for each domain based on business requirements. This allows for high availability, easier maintenance, and the ability to scale individual components as needed.
