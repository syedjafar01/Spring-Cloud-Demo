# ADR 005: Use Loki for centralized logging

## Status
Accepted

## Decision
Use Grafana Loki as the local centralized log backend and Grafana Alloy as the Docker log collector.

## Context
Containerized services write logs independently. Troubleshooting a distributed request requires searching logs across services without manually inspecting individual containers.

## Consequences
Logs can be queried centrally and correlated with trace identifiers. Alloy discovers Docker containers and forwards their logs to Loki, while Grafana provides a single troubleshooting interface for metrics, traces, and logs.
