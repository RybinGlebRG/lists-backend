package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.Metric;

import java.time.Duration;
import java.util.OptionalDouble;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ManagedResource(
        objectName="ru.rerumu.lists:name=MonitoringService",
        description="MonitoringService")
@Component
public final class MonitoringServiceMBean {

    private final MonitoringService monitoringService = MonitoringService.getServiceInstance();

    @ManagedAttribute
    public Double getDbQuerySeriesMapperGetAllExecutionTimeAvg(){
        return monitoringService.getDbQuerySeriesMapperGetAllExecutionTimeAvg();
    }

    @ManagedAttribute
    public Double getSeriesControllerGetAllExecutionTimeAvg(){
        return monitoringService.getSeriesControllerGetAllExecutionTimeAvg();
    }
}
