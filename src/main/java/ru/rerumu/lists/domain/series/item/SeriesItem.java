package ru.rerumu.lists.domain.series.item;

import org.json.JSONObject;

import java.time.LocalDateTime;

public interface SeriesItem {

    JSONObject toJSONObject();

    LocalDateTime getUpdateDate();

    SeriesItemDTOv2 toDTO();
}
