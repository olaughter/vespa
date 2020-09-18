// Copyright 2019 Oath Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package ai.vespa.metricsproxy.node;

import ai.vespa.metricsproxy.metric.model.ConsumerId;
import ai.vespa.metricsproxy.metric.model.DimensionId;
import ai.vespa.metricsproxy.metric.model.MetricsPacket;
import ai.vespa.metricsproxy.metric.model.ServiceId;
import ai.vespa.metricsproxy.service.VespaServices;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author olaa
 */
public class ServiceHealthGatherer {

    protected static List<MetricsPacket.Builder> gatherServiceHealthMetrics(VespaServices vespaServices)  {
        return vespaServices.getVespaServices()
                .stream()
                .map(service ->
                     new MetricsPacket.Builder(ServiceId.toServiceId(service.getMonitoringName()))
                            .timestamp(Instant.now().getEpochSecond())
                            .statusMessage(service.getHealth().getStatus().status)
                            .statusCode(service.getHealth().getStatus().code)
                            .putDimension(DimensionId.toDimensionId("instance"), service.getInstanceName())
                            .putDimension(DimensionId.toDimensionId("metrictype"), "health")
                            .addConsumers(Set.of(ConsumerId.toConsumerId("Vespa")))
                )
                .collect(Collectors.toList());
    }

}
