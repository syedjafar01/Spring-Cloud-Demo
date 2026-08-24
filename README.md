# Spring Cloud Microservices Reference

> A compact reference implementation for service discovery, client-side load balancing, and containerized Spring microservices.

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

## 🎯 Purpose

This repository started as a 2017 Spring Cloud demonstration showing how an `application-client` communicates with multiple `application-server` instances registered through Eureka. The original implementation used Java 8 and the Spring Cloud Camden generation.

The codebase has now been **partially modernized** while preserving the original learning objective:

```text
                    ┌──────────────────────┐
                    │   Application Client │
                    └──────────┬───────────┘
                               │
                         lb://service
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Eureka Server     │
                    │   Service Registry   │
                    └──────────┬───────────┘
                               │
                    ┌──────────┴──────────┐
                    ▼                     ▼
          ┌─────────────────┐   ┌─────────────────┐
          │ Service #1      │   │ Service #2      │
          │ instance-1      │   │ instance-2      │
          │ :8081           │   │ :8082           │
          └─────────────────┘   └─────────────────┘
```

## 🧩 Architecture Goals

- Service discovery and registration
- Multiple instances of the same service
- Client-side load balancing through Spring Cloud LoadBalancer
- Health and operational endpoints
- Containerized local development
- Reproducible multi-module builds
- Automated CI and integration tests
- Resilience patterns as the next evolution

## 🛠️ Technology Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.x |
| Cloud | Spring Cloud 2025.0.x |
| Build | Maven |
| Service Discovery | Netflix Eureka |
| Client Load Balancing | Spring Cloud LoadBalancer |
| HTTP Client | Spring `RestClient` |
| Observability | Spring Boot Actuator |
| Containers | Docker / Docker Compose |
| Testing | JUnit 5 + Spring Boot Test |
| CI | GitHub Actions (next step) |

Spring Boot 3.5 requires Java 17 or newer, and Spring Cloud 2025.0.x is the compatible release train for Spring Boot 3.5.x. citeturn0search0turn0search2

## 📁 Modules

```text
Spring-Cloud-Demo/
├── discovery-service/      # Eureka service registry
├── application-server/     # Discoverable service
├── application-client/     # Load-balanced client
├── Dockerfile              # Reusable multi-module image build
├── docker-compose.yml      # Complete local environment
└── pom.xml                 # Maven multi-module build
```

## ▶️ Run Locally

### Maven

```bash
mvn clean verify
```

### Docker Compose

```bash
docker compose up --build
```

Then open:

- Client: `http://localhost:8080`
- Eureka: `http://localhost:8761`
- Service instance 1: `http://localhost:8081`
- Service instance 2: `http://localhost:8082`

Refreshing the client endpoint demonstrates requests being resolved through the service name `service` and distributed across registered instances.

## 🔄 What Changed From the 2017 Version

The original project used Spring Boot 1.5.3, Spring Cloud Camden, Java 8, and the legacy Netflix Eureka client API. fileciteturn9file0

The modernized implementation now uses:

- Java 17
- Spring Boot 3.5.x
- Spring Cloud 2025.0.x
- `spring-cloud-starter-netflix-eureka-client`
- Spring Cloud LoadBalancer
- Spring `RestClient`
- Constructor-based dependency injection
- Externalized service instance configuration
- Docker Compose deployment
- Actuator health/info endpoints

The client no longer calls Eureka directly to select an instance. Instead, it calls `http://service/` through a load-balanced `RestClient`, allowing the Spring Cloud load-balancing layer to resolve service instances.

## 🚧 Next Engineering Milestones

- [x] Upgrade Java baseline to Java 17
- [x] Upgrade Spring Boot / Spring Cloud dependencies
- [x] Remove legacy duplicate dependencies
- [x] Replace IDE-specific service configuration with environment variables
- [x] Add Docker Compose for the complete environment
- [x] Add Actuator endpoints
- [ ] Add integration tests with Testcontainers
- [ ] Add GitHub Actions CI
- [ ] Add Resilience4j retry / circuit-breaker examples
- [ ] Add Spring Cloud Gateway as an optional edge service
- [ ] Add Prometheus / Grafana observability
- [ ] Add architecture and sequence diagrams

## 🧠 Engineering Topics

This project is intentionally focused on the reasoning behind distributed-system patterns:

- How service discovery decouples service locations from clients
- How client-side load balancing works with multiple instances
- Why direct service-to-service calls should use logical service names
- How retries can amplify failures when used incorrectly
- When circuit breakers are useful
- How health endpoints differ from application readiness
- How containerization makes distributed-system demos reproducible

## 🔭 Future Direction

The goal is to evolve this repository into a compact **microservices reference architecture** for experimenting with service discovery, gateway routing, resilience, observability, testing, and cloud-native deployment.

---

### Author

**Syed Jafar** — Software Development Engineer II

[GitHub](https://github.com/syedjafar01)
