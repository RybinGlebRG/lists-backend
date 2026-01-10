package ru.rerumu.lists.controller.movies.views.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

public class MovieView {



    @Getter
    private final Long id;

    @Getter
    private final String name;

    @JsonProperty("create_date_utc")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Getter
    private final LocalDateTime createDateUTC;

    @Getter
    private final Long watchListId;

    @Getter
    private final Long statusId;

    @Getter
    private final String videoType;

    @Getter
    private final String itemType;


    public MovieView(Long id, String name, LocalDateTime createDateUTC, Long watchListId, Long statusId, String videoType, String itemType) {
        this.id = id;
        this.name = name;
        this.createDateUTC = createDateUTC;
        this.watchListId = watchListId;
        this.statusId = statusId;
        this.videoType = videoType;
        this.itemType = itemType;
    }
}
