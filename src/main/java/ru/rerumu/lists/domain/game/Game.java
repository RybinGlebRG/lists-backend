package ru.rerumu.lists.domain.game;

import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;


public interface Game extends SeriesItem {

    String getTitle();

    LocalDateTime getCreateDateUTC();

    User getUser();

}
