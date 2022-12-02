package ru.rerumu.lists.factories;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class DateFactoryImpl implements DateFactory {

    public Date getCurrentDate(){
        return new Date();
    }

    @Override
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }
}
