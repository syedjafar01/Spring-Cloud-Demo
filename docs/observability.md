# Observability

The platform uses three observability signals.

```text
Services
  +-- Micrometer --> Prometheus --> Grafana
  +-- OpenTelemetry --> Tempo --> Grafana
  +-- Docker logs --> Alloy --> Loki --> Grafana
```

## Metrics

Spring Boot Actuator exposes Prometheus metrics. Prometheus scrapes gateway, consumer, and both greeting-service instances.

Useful queries:

```text
http_server_requests_seconds_count
http_server_requests_seconds_bucket
jvm_memory_used_bytes
process_cpu_usage
```

## Traces

Micrometer Tracing exports OpenTelemetry traces to Tempo over OTLP. The local demonstration samples every request so traces are easy to inspect.

## Logs

Grafana Alloy discovers Docker containers and forwards their stdout/stderr logs to Loki. Container labels expose a logical `service_name` for queries such as:

```logql
{service_name="gateway-service"}
```

```logql
{service_name="greeting-service"}
```

## Correlation

Grafana is provisioned with Tempo and Loki. Tempo can query related Loki logs, while Loki has a derived TraceID field linking back to Tempo.

```text
Tempo trace <----> Loki log with trace_id
```

## Endpoints

- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000`
- Tempo: `http://localhost:3200`
- Loki: `http://localhost:3100`
- Alloy: `http://localhost:12345`

## Troubleshooting

```bash
docker compose ps
docker compose logs -f tempo
docker compose logs -f loki
docker compose logs -f alloy
docker compose logs -f grafana
```

Generate traffic with:

```bash
for i in {1..20}; do curl -s http://localhost:8080/service; echo; done
```
