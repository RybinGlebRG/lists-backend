package ru.rerumu.lists.services;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.OptionalDouble;
import java.util.Queue;

@ManagedResource(
        objectName = "ru.rerumu.lists:name=MonitoringService",
        description = "MonitoringService")
@Component
public final class MonitoringServiceMBean {

    private final MonitoringService monitoringService = MonitoringService.getServiceInstance();

    private Double getAvg(Queue<Metric<?>> queue){
        OptionalDouble avg = queue.parallelStream()
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
    public Double getSeriesMapperGetAllExecutionTimeAvg() {
        return getAvg(monitoringService.getMetricQueue(
                MetricType.DB_QUERY__SERIES_MAPPER__GET_ALL__EXECUTION_TIME
        ));

    }

    @ManagedAttribute
    public Double getSeriesControllerGetAllExecutionTimeAvg() {
        return getAvg(monitoringService.getMetricQueue(
                MetricType.SERIES_CONTROLLER__GET_ALL__EXECUTION_TIME
        ));
    }

    @ManagedAttribute
    public Double getSeriesServiceGetAllExecutionTimeAvg() {
        return getAvg(monitoringService.getMetricQueue(
                MetricType.SERIES_SERVICE__GET_ALL__EXECUTION_TIME
        ));
    }

    @ManagedAttribute
    public Double getSeriesBookMapperFindBySeriesExecutionTimeAvg() {
        return getAvg(monitoringService.getMetricQueue(
                MetricType.SERIES_BOOK_MAPPER__FIND_BY_SERIES__EXECUTION_TIME
        ));
    }

    @ManagedAttribute
    public Double getSeriesBookRepositoryGetBySeriesExecutionTimeAvg() {
        return getAvg(monitoringService.getMetricQueue(
                MetricType.SERIES_BOOK_REPOSITORY__GET_BY_SERIES__EXECUTION_TIME
        ));
    }

    @ManagedAttribute
    public Double getSeriesEnrichmentExecutionTimeAvg() {
        return getAvg(monitoringService.getMetricQueue(
                MetricType.SERIES_ENRICHMENT__EXECUTION_TIME
        ));
    }

    @ManagedAttribute
    public Double getSeriesEnrichmentOneLoopExecutionTimeAvg() {
        return getAvg(monitoringService.getMetricQueue(
                MetricType.SERIES_ENRICHMENT__ONE_LOOP__EXECUTION_TIME
        ));
    }
}
