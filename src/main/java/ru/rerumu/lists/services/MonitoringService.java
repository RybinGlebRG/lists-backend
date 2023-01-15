package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rerumu.lists.model.Metric;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.OptionalDouble;
import java.util.Queue;
import java.util.concurrent.*;

public final class MonitoringService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static MonitoringService instance;

    //    private final Queue<Long> dbQueryExecutionTime = new ConcurrentLinkedQueue<>();
    private final Queue<Metric<?>> dbQuerySeriesMapperGetAllExecutionTime = new ConcurrentLinkedQueue<>();
    private final Queue<Metric<?>> seriesControllerGetAllExecutionTime = new ConcurrentLinkedQueue<>();

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());


    private MonitoringService() {
        executorService.scheduleAtFixedRate(() -> {
                    synchronized (dbQuerySeriesMapperGetAllExecutionTime) {
                        removeHeadIfOld(dbQuerySeriesMapperGetAllExecutionTime, Duration.ofHours(1));
                    }
                }
                ,
                1L,
                1L,
                TimeUnit.MINUTES
        );
        executorService.scheduleAtFixedRate(() -> {
                    synchronized (seriesControllerGetAllExecutionTime) {
                        removeHeadIfOld(seriesControllerGetAllExecutionTime, Duration.ofHours(1));
                    }
                },
                1L,
                1L,
                TimeUnit.MINUTES
        );
    }

    private void removeHeadIfOld(Queue<Metric<?>> queue, Duration duration) {
        while (true) {
            Metric<?> metric = queue.peek();
            if (metric.metricTime().compareTo(LocalDateTime.now().minus(duration)) < 0) {
                queue.poll();
            } else {
                break;
            }
        }
    }

    public synchronized static MonitoringService getServiceInstance() {
        if (instance == null) {
            instance = new MonitoringService();
        }
        return instance;
    }

    public void addMetricValue(Metric<?> metric) {
        switch (metric.metricType()) {
            case DB_QUERY__SERIES_MAPPER__GET_ALL__EXECUTION_TIME -> dbQuerySeriesMapperGetAllExecutionTime.add(metric);
            case SERIES_CONTROLLER__GET_ALL__EXECUTION_TIME -> seriesControllerGetAllExecutionTime.add(metric);
        }
    }

    public Double getDbQuerySeriesMapperGetAllExecutionTimeAvg() {
        OptionalDouble avg = dbQuerySeriesMapperGetAllExecutionTime.parallelStream()
                .map(item -> {
                    if (item.value() instanceof Duration duration) {
                        return duration;
                    } else {
                        throw new IllegalArgumentException();
                    }
                })
                .mapToLong(Duration::toMillis)
                .average();
        logger.debug("dbQuerySeriesMapperGetAllExecutionTime: " + dbQuerySeriesMapperGetAllExecutionTime);
        return avg.orElse(0.0);
    }

    public Double getSeriesControllerGetAllExecutionTimeAvg() {
        OptionalDouble avg = seriesControllerGetAllExecutionTime.parallelStream()
                .map(item -> {
                    if (item.value() instanceof Duration duration) {
                        return duration;
                    } else {
                        throw new IllegalArgumentException();
                    }
                })
                .mapToLong(Duration::toMillis)
                .average();
        logger.debug("seriesControllerGetAllExecutionTime: " + dbQuerySeriesMapperGetAllExecutionTime);
        return avg.orElse(0.0);
    }
}
