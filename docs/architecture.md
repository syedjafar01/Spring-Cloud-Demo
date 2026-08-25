# Architecture

## Overview

This project is a reference implementation of a small Spring Cloud microservices platform. It intentionally keeps the business domain simple so the infrastructure and distributed-system behavior are easy to observe.

```text
                       External Client
                              │
                              ▼
                     ┌─────────────────┐
                     │ gateway-service │
                     │      :8080      │
                     └────────┬────────┘
                              │
                              ▼
                     ┌─────────────────┐
                     │discovery-service│
                     │      :8761      │
                     └────────┬────────┘
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
             greeting-service-1  greeting-service-2
                  :8081               :8082
                    ▲                   ▲
                    └─────────┬─────────┘
                              │
                       consumer-service
                            :8083

          ┌────────────────────────────────────────┐
          │              Observability             │
          │ Prometheus → Metrics                   │
          │ Tempo      → Traces                    │
          │ Loki       ← Logs ← Alloy              │
          │ Grafana    → Visualization             │
          └────────────────────────────────────────┘
```

## Responsibilities

### discovery-service

Eureka server used as the service registry. Application services register themselves and consumers discover logical service names instead of hard-coded instance addresses.

### gateway-service

The external HTTP entry point. It routes `/service` traffic to the logical `greeting-service` destination and participates in distributed tracing.

### greeting-service

A deliberately small discoverable HTTP service. Two Docker instances run on ports 8081 and 8082 to demonstrate client-side load balancing and failure handling.

### consumer-service

An internal client that calls `greeting-service` through service discovery. Retry, circuit breaker, and fallback behavior are demonstrated here.

## Request flows

### Gateway flow

```text
Client
  ↓
gateway-service :8080
  ↓
Spring Cloud LoadBalancer
  ↓
greeting-service
  ├── instance-1 :8081
  └── instance-2 :8082
```

### Consumer flow

```text
consumer-service :8083
          ↓
logical service name
          ↓
Spring Cloud LoadBalancer
          ↓
greeting-service
```

### Resilient flow

```text
greeting-service unavailable
              ↓
           Retry
              ↓
       Circuit Breaker
              ↓
           Fallback
```

## Why the architecture is intentionally small

The repository is designed to isolate important microservice patterns without adding unnecessary business complexity. A reviewer can start the stack locally, observe discovery and load balancing, stop dependencies, reproduce a failure, and inspect metrics, traces, and logs.

Kubernetes is intentionally outside the scope of this repository. Docker Compose provides a reproducible local environment while keeping the focus on Spring Cloud and distributed-system behavior.
