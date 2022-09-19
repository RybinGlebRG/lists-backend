package ru.rerumu.lists.factories;

import java.util.Date;

public class DateFactoryImpl implements DateFactory {

    public Date getCurrentDate(){
        return new Date();
    }
}
