# ADR 007: Keep trace IDs out of Loki stream labels

## Status

Accepted

## Context

The platform uses OpenTelemetry tracing with Grafana Tempo and centralized application logging with Grafana Alloy and Loki. Trace IDs are valuable for correlating logs with traces, but Loki stream labels have a significant impact on index and stream cardinality.

A trace ID is unique for a large number of requests. Promoting every trace ID to a Loki label would therefore create a high-cardinality stream dimension.

## Decision

Keep `trace_id` and `span_id` in the application log content through Spring Boot logging correlation MDC, while using `service_name` as the low-cardinality Loki stream label.

The resulting correlation model is:

```text
Application
    │
    ├── service_name ──────► Loki stream label
    │
    └── trace_id/span_id ──► log content
                               │
                               ▼
                           Grafana
                               │
                               ▼
                             Tempo
```

Grafana datasource configuration provides the navigation between Loki logs and Tempo traces using the trace ID contained in the log entry.

## Alternatives considered

### Use trace ID as a Loki label

This makes trace-specific label queries convenient, but creates a very high-cardinality stream. It is unsuitable for the reference architecture's centralized logging model.

### Do not include trace IDs in logs

Avoids cardinality concerns but loses direct trace-to-log correlation and makes distributed request troubleshooting harder.

### Use only application/service labels

Keeps streams efficient but provides insufficient request-level correlation for distributed tracing.

## Consequences

### Positive

- Loki streams remain based on low-cardinality dimensions.
- Trace-to-log correlation remains available.
- Log queries can locate a trace using the trace ID in log content.
- The design scales better than creating a Loki stream for each trace.

### Negative

- Trace-specific searches require querying log content rather than selecting a trace ID label.
- Grafana datasource configuration is required to provide seamless trace navigation.

## Validation

The application logging pattern includes `trace_id` and `span_id`, while the Loki pipeline uses `service_name` as the stream label. The README documents both directions of trace-to-log and log-to-trace navigation.
