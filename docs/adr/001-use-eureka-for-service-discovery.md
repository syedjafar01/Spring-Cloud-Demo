# ADR 001: Use Eureka for service discovery

## Status
Accepted

## Decision
Use Netflix Eureka as the service registry for the reference architecture.

## Context
Service instances use dynamic container addresses and ports. Consumers should depend on logical service names rather than hard-coded instance locations.

## Consequences
Consumers can discover registered instances and Spring Cloud LoadBalancer can distribute calls across them. The project remains focused on Spring Cloud patterns. Eureka is intentionally used here as a reference implementation rather than a claim that it is the only production choice.
