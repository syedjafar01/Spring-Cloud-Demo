# ADR 003: Use Resilience4j for fault tolerance

## Status
Accepted

## Decision
Use Resilience4j for retry and circuit-breaker behavior in `consumer-service`.

## Context
A downstream service can fail temporarily or remain unavailable for a sustained period. Retrying every request indefinitely can amplify failures.

## Consequences
Transient failures can be retried within bounded limits, while a circuit breaker stops repeated calls after the failure threshold is reached. A fallback gives callers a controlled degraded response. The behavior is covered by an automated integration test.
