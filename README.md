# Spring Cloud Microservices Reference

[![CI](https://github.com/syedjafar01/Spring-Cloud-Microservices-Reference/actions/workflows/ci.yml/badge.svg)](https://github.com/syedjafar01/Spring-Cloud-Microservices-Reference/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Cloud--Native-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)

A hands-on Spring Cloud reference architecture demonstrating **service discovery, API gateway routing, client-side load balancing, resilient service-to-service communication, integration testing, and containerized development**.

The repository evolved from an older Spring Cloud demonstration into a modern Java 17 / Spring Boot 3 based reference project. The goal is to demonstrate the engineering trade-offs behind common microservice patterns rather than simply collect framework annotations.

## Architecture

```text
                              ┌──────────────────┐
                              │ External Client  │
                              └────────┬─────────┘
                                       │
                                       ▼
                              ┌──────────────────┐
                              │ gateway-service  │
                              │      :8080       │
                              └────────┬─────────┘
                                       │
                                       ▼
                              ┌──────────────────┐
                              │discovery-service │
                              │      :8761       │
                              └────────┬─────────┘
                                       │
                         ┌─────────────┴─────────────┐
                         ▼                           ▼
                ┌──────────────────┐       ┌──────────────────┐
                │ greeting-service │       │ greeting-service │
                │    instance-1    │       │    instance-2    │
                │      :8081       │       │      :8082       │
                └─────────▲────────┘       └─────────▲────────┘
                          │                           │
                          └───────────┬───────────────┘
                                      │
                              ┌───────┴────────┐
                              │consumer-service│
                              │     :8083      │
                              └────────────────┘
```

### Request paths

**External request:**

```text
Client → Gateway → Eureka/LoadBalancer → greeting-service instance
```

**Service-to-service request:**

```text
consumer-service → Eureka/LoadBalancer → greeting-service
```

## Modules

| Module | Port | Responsibility |
|---|---:|---|
| `discovery-service` | 8761 | Eureka service registry |
| `gateway-service` | 8080 | External API entry point and routing |
| `greeting-service` | 8081 / 8082 | Discoverable service with multiple instances |
| `consumer-service` | 8083 | Service-to-service client with resilience policies |

## Key capabilities

- **Service discovery** with Netflix Eureka
- **API Gateway** with Spring Cloud Gateway
- **Client-side load balancing** with Spring Cloud LoadBalancer
- **Multiple service instances** for the same logical service
- **Retry** for transient downstream failures
- **Circuit breaker** with Resilience4j
- **Fallback** when the downstream service is unavailable
- **Actuator** health and operational endpoints
- **Integration tests** for discovery/load-balancing behavior
- **Automated resilience tests** covering retry, circuit opening, and fallback
- **Docker Compose** environment for reproducible local execution
- **GitHub Actions CI** running the complete Maven verification suite

## Technology Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Cloud | Spring Cloud |
| Discovery | Netflix Eureka |
| Gateway | Spring Cloud Gateway |
| Load balancing | Spring Cloud LoadBalancer |
| Resilience | Resilience4j |
| Build | Maven |
| Testing | JUnit 5, Spring Boot Test, MockWebServer |
| Runtime | Docker / Docker Compose |
| CI | GitHub Actions |
| Observability | Spring Boot Actuator |

## Quick Start

### Prerequisites

- Java 17
- Maven 3.9+
- Docker Desktop / Docker Engine with Compose

### Build and test

```bash
mvn clean verify
```

The verification suite includes unit/integration tests for service communication, load balancing, and resilience behavior.

### Start the complete environment

```bash
docker compose up --build -d
```

Check the containers:

```bash
docker compose ps
```

### Verify Eureka

Open:

```text
http://localhost:8761
```

### Verify the service instances directly

```bash
curl http://localhost:8081/
curl http://localhost:8082/
```

Expected responses:

```text
Hello from instance-1
Hello from instance-2
```

### Verify Gateway load balancing

```bash
curl http://localhost:8080/service
```

Run it several times and you should see responses from both service instances:

```text
Hello from instance-1
Hello from instance-2
Hello from instance-1
...
```

The sequence is not required to alternate perfectly; both registered instances should receive traffic.

### Verify consumer-service

```bash
curl http://localhost:8083/
```

This exercises service-to-service discovery from `consumer-service` to `greeting-service`.

## Resilience Demonstration

The consumer service uses Resilience4j Retry and Circuit Breaker around the downstream greeting-service call.

A failure follows this path:

```text
             downstream failure
                     │
                     ▼
                   Retry
                     │
                     ▼
              Circuit Breaker
                     │
                     ▼
                  Fallback
                     │
                     ▼
        Service temporarily unavailable
```

To reproduce the failure scenario with Docker:

```bash
docker compose stop greeting-service-1 greeting-service-2
curl http://localhost:8080/service
```

Expected fallback:

```text
Service temporarily unavailable
```

Restart the services with:

```bash
docker compose start greeting-service-1 greeting-service-2
```

## Automated Tests

The project contains tests for the important distributed-system behaviors.

### Load balancing

`LoadBalancingIntegrationTest` registers two test service instances and verifies that calls reach both instances.

### Resilience

`ResilienceIntegrationTest` uses a controlled failing downstream service to verify:

1. downstream `503` responses are retried;
2. the circuit breaker opens after the configured failure threshold;
3. the fallback response is returned;
4. subsequent calls are rejected by the open circuit without another downstream request.

This keeps the resilience behavior testable without requiring Docker or a real Eureka server during the Maven test run.

## CI/CD

GitHub Actions runs on pushes to `master` and pull requests targeting `master`.

The current quality gate is:

```text
Checkout
   ↓
Java 17 / Temurin
   ↓
Maven dependency cache
   ↓
mvn clean verify
   ↓
Unit + integration + resilience tests
```

A change is not considered healthy unless the Maven verification suite passes.

## Engineering Patterns Demonstrated

This project intentionally focuses on practical distributed-system concerns:

- Why service discovery decouples consumers from instance locations
- Why multiple instances improve availability and capacity
- Where gateway routing and client-side load balancing fit
- Why retries need bounded attempts and appropriate retryable exceptions
- How circuit breakers prevent repeatedly calling an unhealthy dependency
- How fallbacks provide controlled degradation
- How integration tests can validate distributed behavior without requiring a full environment
- How containerization makes the complete topology reproducible
- How CI catches compilation, dependency, and test regressions

## Project Roadmap

- [x] Modernize Java / Spring baseline
- [x] Eureka service discovery
- [x] Multiple service instances
- [x] Client-side load balancing
- [x] Spring Cloud Gateway
- [x] Resilience4j retry and circuit breaker
- [x] Fallback handling
- [x] Integration and resilience tests
- [x] Docker Compose environment
- [x] GitHub Actions CI
- [ ] Gateway integration tests
- [ ] Distributed tracing
- [ ] Prometheus / Grafana observability stack
- [ ] Centralized structured logging
- [ ] Architecture decision records
- [ ] Kubernetes deployment examples

## Useful Commands

```bash
# Build and test
mvn clean verify

# Start everything
 docker compose up --build -d

# Stop everything
 docker compose down

# View logs
 docker compose logs -f gateway
 docker compose logs -f consumer

# Check running containers
 docker compose ps
```

## Author

**Syed Jafar** — Software Development Engineer

- GitHub: https://github.com/syedjafar01
