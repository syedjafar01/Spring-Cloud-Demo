# ADR 004: Use Tempo for distributed tracing

## Status
Accepted

## Decision
Use OpenTelemetry-compatible tracing with Grafana Tempo as the local trace backend.

## Context
The project contains multiple service boundaries. Metrics alone show aggregate behavior but do not explain the path and latency of an individual request.

## Consequences
Requests can be followed across gateway, consumer, and greeting services. Tempo integrates naturally with the existing Grafana and Prometheus observability stack while keeping the local deployment lightweight.
