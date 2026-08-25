# Spring Cloud Microservices Reference

[![CI](https://github.com/syedjafar01/Spring-Cloud-Microservices-Reference/actions/workflows/ci.yml/badge.svg)](https://github.com/syedjafar01/Spring-Cloud-Microservices-Reference/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Cloud--Native-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)

A hands-on Spring Cloud reference architecture demonstrating **service discovery, API gateway routing, client-side load balancing, resilient service-to-service communication, integration testing, containerized development, metrics, distributed tracing, centralized logging, and trace-to-log correlation**.

> **Goal:** provide a small but production-oriented microservices playground where distributed-system patterns can be built, tested, observed, and demonstrated locally.

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
                              │ consumer-service │
                              │      :8083       │
                              └────────┬─────────┘
                                       │
                         service discovery / load balancing
                                       │
                         ┌─────────────┴─────────────┐
                         ▼                           ▼
                ┌──────────────────┐       ┌──────────────────┐
                │ greeting-service │       │ greeting-service │
                │    instance-1    │       │    instance-2    │
                │      :8081       │       │      :8082       │
                └──────────────────┘       └──────────────────┘
                         ▲                           ▲
                         └─────────────┬─────────────┘
                                       │
                              ┌────────┴────────┐
                              │ discovery-service│
                              │      :8761       │
                              │      Eureka      │
                              └──────────────────┘

       ┌─────────────────────────────────────────────────────┐
       │ Observability                                       │
       │ Prometheus :9090  → Metrics                         │
       │ Tempo :3200       → Traces                          │
       │ Loki :3100        → Logs                            │
       │ Alloy :12345      → Docker log collection           │
       │ Grafana :3000     → Metrics + Traces + Logs         │
       └─────────────────────────────────────────────────────┘
```

### Request flow

```text
Client
  │
  ▼
Gateway
  │
  ▼
Consumer
  │
  ├── Retry
  ├── Circuit Breaker
  └── Load Balancer
          │
          ├── greeting-service:8081
          └── greeting-service:8082
```

Eureka is used for service registration and discovery; it is not itself part of the synchronous request path.

## Modules

| Module | Port | Responsibility |
|---|---:|---|
| `discovery-service` | 8761 | Eureka service registry |
| `gateway-service` | 8080 | External API entry point and routing |
| `greeting-service` | 8081 / 8082 | Discoverable service with multiple instances |
| `consumer-service` | 8083 | Service-to-service client with resilience policies |
| Prometheus | 9090 | Metrics collection and querying |
| Grafana | 3000 | Metrics, traces, and logs visualization |
| Tempo | 3200 / 4317 / 4318 | Distributed trace storage and OTLP ingestion |
| Loki | 3100 | Centralized log storage and querying |
| Alloy | 12345 | Docker log collection and forwarding to Loki |

## Key capabilities

- **Service discovery** with Netflix Eureka
- **API Gateway** with Spring Cloud Gateway
- **Client-side load balancing** with Spring Cloud LoadBalancer
- **Multiple service instances** for the same logical service
- **Retry** for transient downstream failures
- **Circuit breaker** with Resilience4j
- **Fallback** when the downstream service is unavailable
- **Actuator** health and operational endpoints
- **Micrometer / Prometheus metrics** from gateway, consumer, and greeting services
- **Grafana** provisioned with Prometheus, Tempo, and Loki datasources
- **OpenTelemetry distributed tracing** exported over OTLP to Tempo
- **Centralized Docker log collection** with Grafana Alloy and Loki
- **Trace IDs in application logs** for cross-signal correlation
- **Trace → logs and logs → trace navigation** in Grafana
- **Integration tests** for load-balancing behavior
- **Automated resilience tests** covering retry, circuit opening, and fallback
- **Docker Compose** environment for reproducible local execution
- **GitHub Actions CI** running Maven verification and Docker image builds

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
| Metrics | Micrometer + Prometheus |
| Tracing | Micrometer Tracing + OpenTelemetry |
| Trace backend | Grafana Tempo |
| Logging | Logback + Grafana Alloy + Loki |
| Visualization | Grafana |
| Build | Maven |
| Testing | JUnit 5, Spring Boot Test, MockWebServer |
| Runtime | Docker / Docker Compose |
| CI | GitHub Actions |

## Quick Start

### Prerequisites

- Java 17
- Maven 3.9+
- Docker Desktop / Docker Engine with Compose

### Build and test

```bash
mvn clean verify
```

### Start the complete environment

```bash
docker compose up --build -d
```

Check the containers:

```bash
docker compose ps
```

### Verify the services

Eureka:

```text
http://localhost:8761
```

Greeting instances:

```bash
curl http://localhost:8081/
curl http://localhost:8082/
```

Gateway:

```bash
curl http://localhost:8080/service
```

Consumer service:

```bash
curl http://localhost:8083/
```

## Observability

The stack implements the three core observability signals:

```text
                    Microservices
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
       Metrics         Traces          Logs
          │              │              │
    Prometheus          Tempo           Loki
          │              │              ▲
          │              │              │
          │          OpenTelemetry      │
          │              │              │
          └──────────────┼──────────────┘
                         ▼
                      Grafana
```

### Metrics

Application services expose Prometheus-compatible metrics through Spring Boot Actuator:

```text
http://localhost:8080/actuator/prometheus
http://localhost:8083/actuator/prometheus
http://localhost:8081/actuator/prometheus
http://localhost:8082/actuator/prometheus
```

Prometheus:

```text
http://localhost:9090
```

Grafana:

```text
http://localhost:3000
```

### Traces

The tracing pipeline is:

```text
HTTP request
    ↓
gateway-service
    ↓
consumer-service / greeting-service
    ↓
Micrometer Tracing
    ↓
OpenTelemetry OTLP
    ↓
Tempo
    ↓
Grafana Explore
```

Tempo receives OTLP over HTTP on `4318` and gRPC on `4317` and exposes its query API on `3200`.

Generate traces:

```bash
for i in {1..20}; do
  curl -s http://localhost:8080/service
  echo
done
```

Then open **Grafana → Explore → Tempo → Search**.

### Logs

Grafana Alloy discovers Docker containers through the Docker socket, collects their stdout/stderr logs, adds a low-cardinality `service_name` label, and forwards them to Loki.

```text
Docker containers
       ↓
Grafana Alloy
       ↓
Loki :3100
       ↓
Grafana
```

Loki:

```text
http://localhost:3100/ready
```

Alloy UI:

```text
http://localhost:12345
```

Application logs include the active trace and span IDs using Spring Boot logging correlation MDC:

```text
[trace_id=4f3a... ,span_id=8b21...] request completed
```

This lets a trace in Tempo be correlated with the corresponding log entries in Loki.

### Trace-to-log correlation

Grafana provisions the Tempo and Loki datasource relationship automatically.

From a Tempo span, use **Logs for this span** to open matching Loki logs. From a Loki log containing `trace_id=...`, use the **View Trace** derived field to navigate back to Tempo.

The Loki stream selector uses `service_name` as the low-cardinality service label; trace IDs are kept in the log content rather than promoted to Loki stream labels.

## Load Balancing Demonstration

Run the Gateway request several times:

```bash
for i in {1..10}; do
  curl -s http://localhost:8080/service
  echo
done
```

You should see responses from both greeting-service instances. The sequence does not need to alternate perfectly; the important behavior is that both registered instances can receive traffic.

## Resilience Demonstration

The consumer service uses Resilience4j Retry and Circuit Breaker around the downstream greeting-service call.

```text
downstream failure
       ↓
     Retry
       ↓
Circuit Breaker
       ↓
    Fallback
       ↓
Service temporarily unavailable
```

With both greeting instances stopped:

```bash
docker compose stop greeting-service-1 greeting-service-2
curl http://localhost:8083/
```

Expected fallback:

```text
Service temporarily unavailable
```

The circuit breaker can be observed through Prometheus with:

```promql
resilience4j_circuitbreaker_state{name="greeting-service",state=~"closed|open|half_open"}
```

The expected lifecycle is:

```text
CLOSED → OPEN → HALF_OPEN → CLOSED
```

Restart the greeting instances with:

```bash
docker compose start greeting-service-1 greeting-service-2
```

## Automated Tests

`LoadBalancingIntegrationTest` verifies that calls can reach two independently running test service instances.

`ResilienceIntegrationTest` verifies that:

1. downstream `503` responses are retried;
2. the circuit breaker opens after the configured failure threshold;
3. the fallback response is returned;
4. subsequent calls do not reach the failed downstream service while the circuit is open.

These tests run as part of:

```bash
mvn clean verify
```

## CI/CD

GitHub Actions runs on pushes to `master` and pull requests targeting `master`.

```text
                 GitHub Actions
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
    Maven verify              Docker builds
          │                         │
   Java 17 + tests          4 service images
          │                         │
          └────────────┬────────────┘
                       ▼
                    Quality Gate
```

The Docker matrix validates:

```text
discovery-service
greeting-service
consumer-service
gateway-service
```

## Observability Demo

The screenshots below are captured from the running local environment and show the main operational signals and failure behavior.

### 1. Eureka — Service Discovery

Two `greeting-service` instances are registered with Eureka and available for client-side load balancing.

![Eureka Service Registry](docs/img/EurekaServiceRegistry.png)

### 2. Loki — Centralized Service Logs

Application and infrastructure logs are collected centrally through Grafana Alloy and queried through Loki.

![Service Logs via Loki](docs/img/ServiceLogs.png)

### 3. Tempo — Distributed Trace

A request can be followed across service boundaries, showing the gateway and downstream service spans in a single trace.

![Distributed Trace via Tempo](docs/img/Tempo%20trace.png)

### 4. Resilience4j — Circuit Breaker State Transitions

The circuit breaker automatically transitions between `CLOSED`, `OPEN`, and `HALF_OPEN` as downstream failures and recovery probes occur.

![Resilience4j Circuit Breaker State Transitions](docs/img/Resilience4j.png)

## Engineering Patterns Demonstrated

- Service discovery decoupling consumers from instance locations
- Gateway routing as a single external entry point
- Client-side load balancing across service instances
- Bounded retries for transient failures
- Circuit breakers for controlled degradation
- Fallback behavior for unavailable dependencies
- Integration testing without requiring a full Docker environment
- Metrics collection and visualization for distributed services
- Distributed tracing across service boundaries
- Centralized log collection with low-cardinality Loki labels
- Trace ID propagation into application logs
- Trace-to-log and log-to-trace correlation
- Reproducible local infrastructure with Docker Compose
- CI quality gates for builds, tests, and container images

## Roadmap

- [x] Java 17 / Spring Boot 3 modernization
- [x] Eureka service discovery
- [x] Multiple service instances
- [x] Client-side load balancing
- [x] Spring Cloud Gateway
- [x] Resilience4j retry and circuit breaker
- [x] Integration and resilience tests
- [x] Docker Compose environment
- [x] GitHub Actions CI
- [x] Prometheus metrics
- [x] Grafana metrics visualization
- [x] OpenTelemetry distributed tracing
- [x] Grafana Tempo trace backend
- [x] Loki centralized logging
- [x] Grafana Alloy Docker log collection
- [x] Trace-to-log correlation
- [ ] Gateway integration tests
- [ ] Architecture decision records
- [ ] Kubernetes deployment examples

## Useful Commands

```bash
mvn clean verify
docker compose up --build -d
docker compose down
docker compose logs -f gateway-service
docker compose logs -f consumer-service
docker compose logs -f tempo
docker compose logs -f loki
docker compose logs -f alloy
docker compose ps
```

## Author

**Syed Jafar** — Software Development Engineer

- GitHub: https://github.com/syedjafar01
