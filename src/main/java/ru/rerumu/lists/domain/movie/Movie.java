package ru.rerumu.lists.domain.movie;

import ru.rerumu.lists.domain.movietype.MovieType;
import ru.rerumu.lists.domain.series.item.SeriesItem;

import java.time.LocalDateTime;
import java.util.Date;


public interface Movie extends SeriesItem {

    LocalDateTime getCreateDateLocal();

    String getName();

    Long getId();

    void setName(String name);

    void setCreateDateUTC(Date createDateUTC);

    Date getCreateDateUTC();

    void setStatusId(Long statusId);

    Long getStatusId();

    MovieType getVideoType();

    void setVideoType(MovieType videoType);

    Long getWatchListId();

    int getdd();

    int getMonth();

    int getyyyy();

    int getHH();

    int getmm();

    int getss();

}