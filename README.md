# Spring Cloud Microservices Reference

> A learning project demonstrating service discovery, client-side load balancing, and resilient communication between Spring-based microservices.

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Cloud--Native-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

## 🎯 Purpose

This repository started as a 2017 Spring Cloud demonstration showing how an `application-client` communicates with multiple `application-server` instances registered through a Eureka discovery server. The original implementation uses Java 8 and the Spring Cloud Camden generation. fileciteturn7file0 fileciteturn9file0

The project is being **modernized as a production-oriented reference implementation** while preserving the original learning objective:

```text
                    ┌──────────────────────┐
                    │     Client / API     │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Service Discovery  │
                    │      / Registry      │
                    └──────────┬───────────┘
                               │
                    ┌──────────┴──────────┐
                    ▼                     ▼
          ┌─────────────────┐   ┌─────────────────┐
          │ Server Instance │   │ Server Instance │
          │       #1        │   │       #2        │
          └─────────────────┘   └─────────────────┘
```

## 🧩 Architecture Goals

- Service discovery and registration
- Multiple instances of a service
- Client-side load balancing
- API gateway patterns
- Health and operational endpoints
- Resilient service-to-service communication
- Containerized local development
- Automated build and test pipeline
- Clear separation between infrastructure and application services

## 🛠️ Target Technology Stack

| Area | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot 3.x |
| Cloud | Spring Cloud |
| Build | Maven |
| Service Discovery | Eureka-compatible discovery pattern |
| Gateway | Spring Cloud Gateway |
| Resilience | Resilience4j |
| Observability | Spring Boot Actuator + Micrometer |
| Containers | Docker / Docker Compose |
| Testing | JUnit 5 + Spring Boot Test |
| CI | GitHub Actions |

## 📁 Current Modules

```text
Spring-Cloud-Demo/
├── discovery-service/      # Service registry
├── application-server/     # Discoverable service instances
├── application-client/     # Client consuming the service
└── pom.xml                 # Maven multi-module build
```

The original repository is a Maven multi-module project containing these three services. fileciteturn11file0

## 🚧 Modernization Roadmap

- [x] Document original architecture and intent
- [ ] Upgrade Java baseline to Java 17+
- [ ] Upgrade Spring Boot / Spring Cloud dependencies
- [ ] Remove legacy duplicate dependencies
- [ ] Replace IDE-specific run instructions with reproducible commands
- [ ] Add Docker Compose for the complete environment
- [ ] Add Spring Cloud Gateway
- [ ] Add Resilience4j retry / circuit-breaker examples
- [ ] Add Actuator health and metrics endpoints
- [ ] Add integration tests
- [ ] Add GitHub Actions CI
- [ ] Add architecture and sequence diagrams
- [ ] Add local observability with Prometheus / Grafana

## 🧪 Original Demo Flow

The original project runs a discovery server, two application-server instances on different ports, and an application client. The two server instances register with Eureka and requests can be served by either instance. fileciteturn7file0

The original Maven parent currently targets Spring Boot 1.5.3, Spring Cloud Camden, and Java 8, so the existing implementation should be treated as a **legacy baseline rather than a modern production dependency set**. fileciteturn9file0

## ▶️ Planned Local Experience

The modernization target is a single reproducible workflow:

```bash
# Clone
 git clone https://github.com/syedjafar01/Spring-Cloud-Demo.git
 cd Spring-Cloud-Demo

# Build
./mvnw clean verify

# Start the complete environment
 docker compose up --build
```

The Docker Compose workflow will eventually start the discovery, gateway, and service instances without requiring IDE-specific configuration.

## 📚 Engineering Topics Demonstrated

This project is intentionally focused on **why** distributed systems need these patterns rather than simply showing annotations:

- How service discovery decouples service locations from clients
- How multiple service instances improve availability and capacity
- Where client-side versus gateway-side load balancing belongs
- How retries can amplify failures when used incorrectly
- When circuit breakers are useful
- How health checks differ from business-level readiness
- How observability helps diagnose distributed requests

## 🔭 Future Direction

The next version will evolve this repository from a historical Spring Cloud demo into a compact **microservices reference architecture** suitable for experimenting with system-design patterns, resilience, observability, and cloud-native deployment.

---

### Author

**Syed Jafar** — Software Development Engineer II

[GitHub](https://github.com/syedjafar01)