# Testing Strategy

The test suite focuses on behavior that is important in a distributed system rather than only testing individual methods.

## Verification command

```bash
mvn clean verify
```

## Load-balancing integration test

`LoadBalancingIntegrationTest` verifies that the logical `greeting-service` destination can resolve to two independently running service instances and that requests can reach both instances.

## Resilience integration test

`ResilienceIntegrationTest` uses a controlled downstream endpoint to verify retry, circuit-breaker opening, fallback, and rejection while the circuit remains open.

This avoids coupling the resilience test to Docker or a live Eureka server.

## Docker verification

The GitHub Actions pipeline also builds all four service images after Maven verification succeeds:

```text
discovery-service
greeting-service
consumer-service
gateway-service
```

## Manual smoke tests

```bash
curl http://localhost:8761
curl http://localhost:8081/
curl http://localhost:8082/
curl http://localhost:8080/service
curl http://localhost:8083/
```

The project intentionally keeps the business response simple so distributed behavior is easy to verify.
