package ru.rerumu.lists.dao.movie;

import ru.rerumu.lists.dao.series.item.SeriesItemDTO;
import ru.rerumu.lists.domain.movietype.MovieType;

import java.util.Date;

public class MovieDTO implements SeriesItemDTO {
    public Integer titleId;
    public String name;
    public Date createDateUTC;
    public Long listId;
    public MovieStatus movieStatus;
    public MovieType videoType;

    public MovieDTO() {}

}
