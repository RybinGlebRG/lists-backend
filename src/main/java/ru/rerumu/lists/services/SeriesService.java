package ru.rerumu.lists.services;

import ru.rerumu.lists.model.Series;

import java.util.List;

public interface SeriesService {

    List<Series> getAll(Long readListId);
}
