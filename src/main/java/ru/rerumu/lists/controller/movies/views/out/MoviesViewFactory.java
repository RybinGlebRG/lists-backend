package ru.rerumu.lists.controller.movies.views.out;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.movie.Movie;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MoviesViewFactory {

    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.MOVIE;

    @NonNull
    public MovieView buildMovieView(@NonNull Movie movie) {
        return new MovieView(
                movie.getId(),
                movie.getName(),
                movie.getCreateDateLocal(),
                movie.getWatchListId(),
                movie.getStatusId(),
                movie.getVideoType().toJSONObject().toString(),
                SERIES_ITEM_TYPE.name()
        );
    }

    @NonNull
    public MovieListView buildMovieListView(@NonNull List<Movie> movies) {
        Comparator<Movie> comparator = new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                int res = Integer.compare(o1.getyyyy(), o2.getyyyy());
                if (res != 0) {
                    return res;
                }
                res = Integer.compare(o1.getMonth(), o2.getMonth());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getdd(), o2.getdd());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getHH(), o2.getHH());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getmm(), o2.getmm());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getss(), o2.getss());
                if (res != 0) {
                    return res;
                }

                res = o1.getName().compareTo(o2.getName());
                if (res != 0) {
                    return res;
                }

                res = Long.compare(o1.getId(), o2.getId());
                return res;
            }
        };

        return new MovieListView(
                movies.stream()
                        .sorted(comparator)
                        .map(this::buildMovieView)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

}
