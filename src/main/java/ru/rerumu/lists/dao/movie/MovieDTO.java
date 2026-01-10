package ru.rerumu.lists.dao.movie;

import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.domain.VideoType;
import ru.rerumu.lists.dao.series.item.SeriesItemDTO;
import ru.rerumu.lists.domain.movie.Movie;
import ru.rerumu.lists.domain.movie.impl.MovieImpl;

import java.util.Date;

public class MovieDTO implements EntityDTO<Movie>, SeriesItemDTO {
    public Integer titleId;
    public String name;
    public Date createDateUTC;
    public Long listId;
    public MovieStatus movieStatus;
    public VideoType videoType;

    public MovieDTO() {}

    public Movie toDomain(){
        // TODO: Do not use implementation
        return new MovieImpl.Builder()
                .titleId(Long.valueOf(titleId))
                .name(name)
                .createDateUTC(createDateUTC)
                .watchListId(listId)
                .statusId((long) movieStatus.statusId())
                .videoType(videoType)
                .build();
    }
}
