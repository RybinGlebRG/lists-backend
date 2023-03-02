package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.SeriesItem;

public interface SeriesItemDTO {

    SeriesItem toDomain() throws EmptyMandatoryParameterException;
}
