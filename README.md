# Spring Cloud Microservices Reference

[![CI](https://github.com/syedjafar01/Spring-Cloud-Microservices-Reference/actions/workflows/ci.yml/badge.svg)](https://github.com/syedjafar01/Spring-Cloud-Microservices-Reference/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Cloud--Native-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)

A hands-on Spring Cloud reference architecture demonstrating **service discovery, API gateway routing, client-side load balancing, resilient service-to-service communication, integration testing, containerized development, metrics, and distributed tracing**.

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

                 ┌──────────────────────────────┐
                 │ Prometheus :9090             │
                 │ Grafana :3000                │
                 │ Tempo :3200 / OTLP :4318     │
                 └──────────────────────────────┘
```

## Modules

| Module | Port | Responsibility |
|---|---:|---|
| `discovery-service` | 8761 | Eureka service registry |
| `gateway-service` | 8080 | External API entry point and routing |
| `greeting-service` | 8081 / 8082 | Discoverable service with multiple instances |
| `consumer-service` | 8083 | Service-to-service client with resilience policies |
| Prometheus | 9090 | Metrics collection and querying |
| Grafana | 3000 | Metrics and trace visualization |
| Tempo | 3200 / 4317 / 4318 | Distributed trace storage and OTLP ingestion |

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
- **Grafana** provisioned with Prometheus and Tempo datasources
- **OpenTelemetry distributed tracing** exported over OTLP to Tempo
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
| Dashboards | Grafana |
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

The application services expose Prometheus-compatible metrics through Spring Boot Actuator:

```text
http://localhost:8080/actuator/prometheus
http://localhost:8083/actuator/prometheus
http://localhost:8081/actuator/prometheus
http://localhost:8082/actuator/prometheus
```

Prometheus scrapes the application instances using the Docker service names configured in:

```text
observability/prometheus/prometheus.yml
```

Open Prometheus:

```text
http://localhost:9090
```

Open Grafana:

```text
http://localhost:3000
```

Grafana is automatically provisioned with both Prometheus and Tempo datasources. The metrics pipeline is:

```text
Spring Boot
    ↓
Micrometer
    ↓
Actuator /actuator/prometheus
    ↓
Prometheus
    ↓
Grafana
```

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

Tracing is sampled at 100% for this local demonstration. Spring Boot supports configurable sampling through `management.tracing.sampling.probability`; the default in production-oriented applications is intentionally lower to control trace volume. citeturn0search7turn0search0

Useful first metric queries in Prometheus/Grafana include:

```text
http_server_requests_seconds_count
http_server_requests_seconds_bucket
jvm_memory_used_bytes
process_cpu_usage
system_cpu_usage
```

## Distributed Tracing Demonstration

Generate a request through the Gateway:

```bash
curl http://localhost:8080/service
```

Then open Grafana and go to **Explore → Tempo → Search**.

A successful request should produce a trace containing spans for the HTTP request path, allowing you to inspect service boundaries and latency across the distributed request.

The trace backend is Grafana Tempo, receiving OTLP over HTTP on port `4318` and exposing its query API on port `3200`. Tempo's OTLP receiver supports both gRPC (`4317`) and HTTP (`4318`) transports. citeturn0search2turn0search3

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
curl http://localhost:8080/service
```

Expected fallback:

```text
Service temporarily unavailable
```

Restart them with:

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
- [ ] Gateway integration tests
- [ ] Centralized structured logging
- [ ] Architecture decision records
- [ ] Kubernetes deployment examples

## Useful Commands

```bash
mvn clean verify
docker compose up --build -d
docker compose down
docker compose logs -f gateway
docker compose logs -f consumer
docker compose logs -f tempo
docker compose ps
```

## Author

**Syed Jafar** — Software Development Engineer

- GitHub: https://github.com/syedjafar01
