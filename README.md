# FastHealthCheckProcessor for Nacos

> A custom plugin to enhance Nacos health check reliability in production environments.

---

##  Features

- Shortened heartbeat timeout (default 15s → 10s)
-  Real TCP socket probing to detect actual liveness
-  Immediate health state update (no passive sync delay)
- Compatible with Nacos plugin system (`HealthCheckType.CUSTOM`)
-  Avoids false healthy instances causing production outages

---

## How to Use

1. Copy `FastHealthCheckProcessor.java` into your Nacos plugin module
2. In `nacos.properties`, set:
```properties
nacos.health.check.type=FAST_TCP

3.Rebuild and restart Nacos

Why This Plugin Exists

The default Nacos health check relies solely on heartbeats.
In real-world scenarios:

Heartbeats may freeze

Ports may go down

But Nacos still believes the service is healthy

This plugin was written to fix this architectural blind spot.

Author

Created by Yuer, cognitive architecture engineer.
This plugin is part of a long-term initiative to demonstrate how foundational flaws in open-source infra can be solved structurally — with minimal code but maximum clarity.


 I don't expect praise — but if you must, send it here: jiabase@gmail.com


