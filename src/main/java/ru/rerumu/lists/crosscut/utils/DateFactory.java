package ru.rerumu.lists.crosscut.utils;

import java.time.LocalDateTime;
import java.util.Date;

public interface DateFactory {

    public Date getCurrentDate();

    LocalDateTime getLocalDateTime();
}
