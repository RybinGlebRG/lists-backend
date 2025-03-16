package ru.rerumu.lists.utils.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.utils.DateFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class DateFactoryImpl implements DateFactory {

    public Date getCurrentDate(){
        return new Date();
    }

    @Override
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
