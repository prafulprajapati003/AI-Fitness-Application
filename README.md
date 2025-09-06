# AI Fitness Application

A modern fitness application leveraging AI for personalized workout and health recommendations, built on a scalable cloud-native microservices architecture.

---

## 🚀 Purpose

- **Personalized Fitness**: AI-driven recommendations for workouts and health using Google Gemini API.
- **Scalable Architecture**: Microservices design using Spring Boot & React.
- **Real-World AI Integration**: Showcases seamless AI utilization in distributed systems.
- **Secure & Modular**: Production-ready, modular, and secure full-stack application.

---

## ⭐️ Key Features

1. **User Authentication & Authorization**
   - Secured via Keycloak.

2. **Personalized AI Guidance**
   - Google Gemini API suggests workouts & fitness plans.

3. **Workout & Progress Tracking**
   - Log exercises, monitor performance, set goals.

4. **Microservices Architecture**
   - Independent services for fitness, users, recommendations, etc.

5. **Service Discovery & Routing**
   - Eureka Server + Spring Cloud Gateway.

6. **Asynchronous Communication**
   - RabbitMQ for inter-service messaging.

7. **Centralized Configuration**
   - Spring Cloud Config for config management.

8. **Database Management**
   - PostgreSQL/MySQL stores users & fitness data.

9. **Frontend Experience**
   - React-based, responsive UI.

10. **Scalability & Maintainability**
    - Each service can be independently scaled and updated.

---

## ⚙️ Tech Stack

- **Backend**: Spring Boot (Microservices), Spring Cloud Config Server, Eureka Server, Spring Cloud Gateway
- **Frontend**: React.js
- **Authentication & Security**: Keycloak
- **Messaging**: RabbitMQ (Spring AMQP)
- **Database**: PostgreSQL / MySQL
- **AI Integration**: Google Gemini API
- **Architecture**: Microservices (cloud-native, distributed, scalable)

---

## 📁 Repository Structure (Suggested)

```
ai-fitness-application/
├── config-server/
├── eureka-server/
├── gateway/
├── user-service/
├── fitness-service/
├── recommendation-service/
├── messaging-service/
├── frontend/
└── README.md
```

---

## 🛠️ Getting Started

> Detailed setup instructions for each microservice, Keycloak, RabbitMQ, and Gemini API integration will be provided in service-specific docs.

---

## 📢 Contribution

Open to contributions! Please open issues/PRs for bug fixes, features, or improvements.

---

## 📄 License

[MIT](LICENSE)
