package ru.rerumu.lists.dao.title;

import ru.rerumu.lists.domain.movie.Movie;
import ru.rerumu.lists.controller.movies.views.TitleCreateView;

import java.util.List;

public interface TitlesRepository {

    List<Movie> getAll(Long watchListId);

    Movie getOne(Long watchListId, Long TitleId);

    Movie update(Movie movie);

    Movie addOne(TitleCreateView newTitle);

    Long getNextId();

    void delete(Long titleId);
}
