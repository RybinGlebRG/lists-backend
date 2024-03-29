package ru.rerumu.lists.factories;

import java.time.LocalDateTime;
import java.util.Date;

public interface DateFactory {

    public Date getCurrentDate();

    LocalDateTime getLocalDateTime();
}
