package ru.rerumu.lists.model.series.item;

import org.json.JSONObject;

import java.time.LocalDateTime;

public interface SeriesItem {

    JSONObject toJSONObject();

    LocalDateTime getUpdateDate();
}
