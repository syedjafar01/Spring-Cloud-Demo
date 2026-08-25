# ADR 002: Use Gateway as the external entry point

## Status
Accepted

## Decision
Expose `gateway-service` as the single external HTTP entry point for the demo application.

## Context
Clients should not need to know which internal service instance handles a request. Centralizing routing also gives the platform a natural place for cross-cutting concerns.

## Consequences
External clients call one stable endpoint while internal services remain discoverable and independently scalable. The gateway becomes an important operational boundary and must therefore be observable and resilient.
