package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rerumu.lists.model.Metric;
import ru.rerumu.lists.model.MetricType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Queue;
import java.util.concurrent.*;

public final class MonitoringService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static MonitoringService instance;
    private final Map<MetricType,Queue<Metric<?>>> metricMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors()
    );


    private MonitoringService() {
        for(MetricType metricType: MetricType.values()){
            metricMap.put(metricType,new ConcurrentLinkedQueue<>());
            executorService.scheduleAtFixedRate(() -> {
                        synchronized (metricMap.get(metricType)) {
                            removeHeadIfOld(metricMap.get(metricType), Duration.ofHours(1));
                        }
                    }
                    ,
                    1L,
                    1L,
                    TimeUnit.MINUTES
            );
        }
    }

    private void removeHeadIfOld(Queue<Metric<?>> queue, Duration duration) {
        while (true) {
            Metric<?> metric = queue.peek();
            if (metric != null && metric.metricTime().compareTo(LocalDateTime.now().minus(duration)) < 0) {
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
            case DB_QUERY__SERIES_MAPPER__GET_ALL__EXECUTION_TIME ->
                    metricMap.get(MetricType.DB_QUERY__SERIES_MAPPER__GET_ALL__EXECUTION_TIME).add(metric);
            case SERIES_CONTROLLER__GET_ALL__EXECUTION_TIME ->
                    metricMap.get(MetricType.SERIES_CONTROLLER__GET_ALL__EXECUTION_TIME).add(metric);
        }
    }

    public Queue<Metric<?>> getMetricQueue(MetricType metricType){
        return new ConcurrentLinkedQueue<>(metricMap.get(metricType));
    }

    public static <T> T gatherExecutionTime(Callable<T> callable, MetricType metricType) throws Exception {
        LocalDateTime start = LocalDateTime.now();
        T res = callable.call();
        LocalDateTime end = LocalDateTime.now();
        MonitoringService.getServiceInstance().addMetricValue(new Metric<>(
                metricType,
                LocalDateTime.now(),
                Duration.between(start, end)
        ));
        return res;
    }
}
