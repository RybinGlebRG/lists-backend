package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

public interface EntityDTO<T> {

    T toDomain();
}
