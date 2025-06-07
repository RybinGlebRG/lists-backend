package ru.rerumu.lists.model.series;

import ru.rerumu.lists.model.base.Entity;

public interface Series extends Entity {

    String getTitle();

    SeriesDTOv2 toDTO();
}
