package ru.rerumu.lists.domain.movie.impl;

import lombok.Getter;
import lombok.Setter;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.domain.movie.Movie;
import ru.rerumu.lists.domain.movietype.MovieType;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class MovieImpl implements Movie {

    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.MOVIE;

    @Getter
    private final Long titleId;

    @Getter
    private String name;

    @Getter
    private Date createDateUTC;

    @Getter
    private final Long watchListId;

    @Getter
    @Setter
    private Long statusId;

    @Getter
    @Setter
    private MovieType videoType;


    public MovieImpl(
            Long titleId,
            Long watchListId,
            String name,
            Date createDateUTC,
            Long statusId,
            MovieType videoType
    ) {
        this.titleId = titleId;
        this.watchListId = watchListId;
        this.name = name;
        this.createDateUTC = createDateUTC;
        this.statusId = statusId;
        this.videoType = videoType;
    }

    public int getdd() {
        return Integer.parseInt(new SimpleDateFormat("dd").format(this.createDateUTC));
    }

    public int getMonth() {
        return Integer.parseInt(new SimpleDateFormat("MM").format(this.createDateUTC));
    }

    public int getyyyy() {
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(this.createDateUTC));
    }

    public int getHH() {
        return Integer.parseInt(new SimpleDateFormat("HH").format(this.createDateUTC));
    }

    public int getmm() {
        return Integer.parseInt(new SimpleDateFormat("mm").format(this.createDateUTC));
    }

    public int getss() {
        return Integer.parseInt(new SimpleDateFormat("ss").format(this.createDateUTC));
    }

    public void setName(String name) throws EmptyMandatoryParameterException {
        if (name == null || name.isEmpty()) {
            throw new EmptyMandatoryParameterException("Name is null or empty");
        }
        this.name = name;
    }

    public void setCreateDateUTC(Date createDateUTC) throws EmptyMandatoryParameterException {
        if (createDateUTC == null) {
            throw new EmptyMandatoryParameterException("createDateUTC is null");
        }
        this.createDateUTC = createDateUTC;
    }

    public LocalDateTime getCreateDateLocal(){
        return createDateUTC
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    @Override
    public Long getId() {
        return titleId;
    }

    public static class Builder {
        private Long titleId;
        private String name;
        private Date createDateUTC;
        private Long watchListId;
        private Long statusId;
        private MovieType videoType;

        public Builder titleId(Long titleId) {
            this.titleId = titleId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder createDateUTC(Date createDateUTC) {
            this.createDateUTC = createDateUTC;
            return this;
        }

        public Builder watchListId(Long watchListId) {
            this.watchListId = watchListId;
            return this;
        }

        public Builder statusId(Long statusId) {
            this.statusId = statusId;
            return this;
        }

        public Builder videoType(MovieType videoType) {
            this.videoType = videoType;
            return this;
        }

        public Movie build() {
            Movie movie = new MovieImpl(
                    titleId,
                    watchListId,
                    name,
                    createDateUTC,
                    statusId,
                    videoType
            );
            return movie;
        }

    }

}
