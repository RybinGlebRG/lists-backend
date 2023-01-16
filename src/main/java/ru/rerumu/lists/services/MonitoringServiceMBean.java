package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.Metric;
import ru.rerumu.lists.model.MetricType;

import java.time.Duration;
import java.util.OptionalDouble;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ManagedResource(
        objectName = "ru.rerumu.lists:name=MonitoringService",
        description = "MonitoringService")
@Component
public final class MonitoringServiceMBean {

    private final MonitoringService monitoringService = MonitoringService.getServiceInstance();

    @ManagedAttribute
    public Double getDbQuerySeriesMapperGetAllExecutionTimeAvg() {
        OptionalDouble avg = monitoringService.getMetricQueue(
                        MetricType.DB_QUERY__SERIES_MAPPER__GET_ALL__EXECUTION_TIME
                ).parallelStream()
                .map(item -> {
                    if (item.value() instanceof Duration duration) {
                        return duration;
                    } else {
                        throw new IllegalArgumentException();
                    }
                })
                .mapToLong(Duration::toMillis)
                .average();
        return avg.orElse(0.0);
    }

    @ManagedAttribute
    public Double getSeriesControllerGetAllExecutionTimeAvg() {
        OptionalDouble avg = monitoringService.getMetricQueue(
                        MetricType.SERIES_CONTROLLER__GET_ALL__EXECUTION_TIME
                ).parallelStream()
                .map(item -> {
                    if (item.value() instanceof Duration duration) {
                        return duration;
                    } else {
                        throw new IllegalArgumentException();
                    }
                })
                .mapToLong(Duration::toMillis)
                .average();
        return avg.orElse(0.0);
    }
}
