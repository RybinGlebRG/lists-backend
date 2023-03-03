package ru.rerumu.lists.factories.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.factories.DateFactory;

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
