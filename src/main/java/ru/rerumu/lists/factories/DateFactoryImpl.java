package ru.rerumu.lists.factories;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateFactoryImpl implements DateFactory {

    public Date getCurrentDate(){
        return new Date();
    }
}
