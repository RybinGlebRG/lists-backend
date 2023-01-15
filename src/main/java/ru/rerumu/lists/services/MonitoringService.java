package ru.rerumu.lists.services;

import ru.rerumu.lists.model.Metric;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class MonitoringService {
    private static MonitoringService instance;

    private final Queue<Long> dbQueryExecutionTime = new ConcurrentLinkedQueue<>();

    private MonitoringService(){}

    public synchronized static MonitoringService getServiceInstance(){
        if (instance == null){
            instance = new MonitoringService();
        }
        return instance;
    }

    public void addMetricValue(Metric metric, Long value){
        switch (metric){
            case DB_QUERY_EXECUTION_TIME -> dbQueryExecutionTime.add(value);
        }
    }
}
