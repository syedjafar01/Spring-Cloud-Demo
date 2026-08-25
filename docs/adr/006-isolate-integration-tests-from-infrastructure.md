# ADR 006: Isolate integration tests from infrastructure

## Status

Accepted

## Context

The reference architecture contains infrastructure dependencies such as Eureka and Docker Compose services. Integration tests should validate application behavior without requiring the complete local infrastructure stack to be running.

The Gateway route uses `lb://greeting-service` and normally resolves that logical service through discovery. A test that depends on a live Eureka server would make the Gateway test slower, more fragile, and harder to run in CI or from an IDE.

The Gateway therefore needs a deterministic downstream service while still exercising the real Gateway application and route configuration.

## Decision

Use Spring Cloud's `SimpleDiscoveryClient` for test-time service discovery and OkHttp `MockWebServer` as the isolated downstream HTTP service.

The Gateway integration test will:

- start the real Gateway with `RANDOM_PORT`;
- disable Eureka for the test context;
- register the test `greeting-service` URI through `spring.cloud.discovery.client.simple.instances`;
- use `MockWebServer` to control downstream responses;
- verify both successful routing and downstream error propagation.

## Alternatives considered

### Live Eureka + Docker Compose

Provides a production-like environment, but couples the test to infrastructure startup, service registration timing, container networking, and CI environment availability.

### Mocking Gateway internals

Would make tests faster but would reduce confidence in the actual route, predicate, filter, and load-balanced URI configuration.

### WireMock

A valid alternative for HTTP stubbing, but `MockWebServer` is sufficient for the small request/response scenarios in this reference project and is already aligned with the project's testing stack.

## Consequences

### Positive

- Tests are deterministic and self-contained.
- No Eureka server or Docker environment is required.
- The real Spring Cloud Gateway route is exercised.
- Downstream responses can be precisely controlled.
- CI failures are less likely to be caused by infrastructure startup timing.
- Developers can run the Gateway tests directly from an IDE.

### Negative

- The test does not validate Eureka registration or discovery itself.
- It does not prove that the complete Docker Compose network behaves correctly.
- A separate end-to-end smoke test would still be appropriate if production-like infrastructure validation is required.

## Validation

`GatewayIntegrationTest` verifies that `/service` reaches the configured `greeting-service`, that `StripPrefix=1` produces the expected downstream path, and that a downstream `503` is propagated by the Gateway.
