package ru.rerumu.lists.domain.game;

import org.json.JSONObject;
import ru.rerumu.lists.domain.base.Entity;
import ru.rerumu.lists.domain.seriesitem.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;


public interface Game extends SeriesItem, Entity {

    String getTitle();

    LocalDateTime getCreateDateUTC();

    User getUser();

    String getNote();

    JSONObject toJSONObject();
}
