# Resilience

## Goal

The consumer-service demonstrates predictable degradation when greeting-service becomes unavailable. Resilience4j provides retry, circuit breaker, and fallback behavior.

## Failure sequence

```text
greeting-service unavailable
        |
      Retry
        |
 Circuit Breaker
        |
     Fallback
        |
Service temporarily unavailable
```

## Reproduce locally

```bash
docker compose up --build -d
curl http://localhost:8080/service

docker compose stop greeting-service-1 greeting-service-2
curl http://localhost:8080/service

docker compose start greeting-service-1 greeting-service-2
```

The degraded response is:

```text
Service temporarily unavailable
```

## Retry vs circuit breaker

Retry addresses transient failures where a dependency may recover immediately. A circuit breaker protects the caller and dependency when failures persist by rejecting calls locally after the configured failure threshold.

## Automated verification

`ResilienceIntegrationTest` uses a controlled failing downstream endpoint, so retry, circuit opening, fallback, and rejection while the circuit is open can be tested without Docker or a real Eureka server.

Run:

```bash
mvn clean verify
```

## Observability during failure

The failure should be visible through metrics, traces, and logs: increased request latency from retries, failed downstream spans, error/request-rate changes, and correlated application log entries.
