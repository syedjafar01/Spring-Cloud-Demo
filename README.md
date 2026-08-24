# Spring Cloud Microservices Reference

> A learning project demonstrating service discovery, client-side load balancing, and resilient communication between Spring-based microservices.

[![CI](https://github.com/syedjafar01/Spring-Cloud-Demo/actions/workflows/ci.yml/badge.svg)](https://github.com/syedjafar01/Spring-Cloud-Demo/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Cloud--Native-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

## 🎯 Purpose

This repository started as a 2017 Spring Cloud demonstration showing how an `application-client` communicates with multiple `application-server` instances registered through a Eureka discovery server. The original implementation used Java 8 and the Spring Cloud Camden generation.

The project is being **modernized as a production-oriented reference implementation** while preserving the original learning objective.

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

## 🛠️ Technology Stack

| Area | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot 3.x |
| Cloud | Spring Cloud |
| Build | Maven |
| Service Discovery | Eureka |
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
├── docker-compose.yml      # Reproducible local environment
└── pom.xml                 # Maven multi-module build
```

## 🚧 Modernization Roadmap

- [x] Document original architecture and intent
- [x] Upgrade Java baseline to Java 17+
- [x] Upgrade Spring Boot / Spring Cloud dependencies
- [x] Remove legacy duplicate dependencies
- [x] Replace IDE-specific run instructions with reproducible commands
- [x] Add Docker Compose for the complete environment
- [x] Add GitHub Actions CI
- [ ] Add Spring Cloud Gateway
- [ ] Add Resilience4j retry / circuit-breaker examples
- [ ] Add Actuator health and metrics endpoints
- [ ] Add integration tests
- [ ] Add architecture and sequence diagrams
- [ ] Add local observability with Prometheus / Grafana

## 🧪 CI Pipeline

Every push to `master` and every pull request targeting `master` runs the Maven verification pipeline.

The workflow:

1. Checks out the source
2. Installs Java 17 using Eclipse Temurin
3. Restores Maven dependencies from the GitHub Actions cache
4. Runs `mvn clean verify`

The Maven dependency cache is provided by `actions/setup-java`, which supports built-in Maven dependency caching. citeturn0search0turn0search5

## ▶️ Local Development

```bash
# Clone
git clone https://github.com/syedjafar01/Spring-Cloud-Demo.git
cd Spring-Cloud-Demo

# Build
mvn clean verify

# Start the complete environment
docker compose up --build
```

## 📚 Engineering Topics Demonstrated

This project is intentionally focused on **why** distributed systems need these patterns rather than simply showing annotations:

- How service discovery decouples service locations from clients
- How multiple service instances improve availability and capacity
- Where client-side versus gateway-side load balancing belongs
- How retries can amplify failures when used incorrectly
- When circuit breakers are useful
- How health checks differ from business-level readiness
- How observability helps diagnose distributed requests
- How CI prevents dependency or compilation regressions from reaching the main branch

## 🔭 Future Direction

The next version will evolve this repository from a historical Spring Cloud demo into a compact **microservices reference architecture** suitable for experimenting with system-design patterns, resilience, observability, and cloud-native deployment.

---

### Author

**Syed Jafar** — Software Development Engineer II

[GitHub](https://github.com/syedjafar01)
