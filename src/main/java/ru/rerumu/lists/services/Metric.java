package ru.rerumu.lists.services;

import java.time.LocalDateTime;

public record Metric<T>(MetricType metricType, LocalDateTime metricTime, T value) {
    public Metric{
        if (metricTime == null || value == null){
            throw new IllegalArgumentException();
        }
    }
}
