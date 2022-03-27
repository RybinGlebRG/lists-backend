package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.views.TitleCreateView;

import java.util.List;

public interface TitlesRepository {

    List<Title> getAll(Long watchListId);

    Title getOne(Long watchListId, Long TitleId);

    Title update(Title title);

    Title addOne(TitleCreateView newTitle);

    Long getNextId();
}
