package ru.rerumu.lists.model;

import org.json.JSONObject;

import java.time.LocalDateTime;

public interface SeriesItem {

    JSONObject toJSONObject();

    LocalDateTime getUpdateDate();
}
