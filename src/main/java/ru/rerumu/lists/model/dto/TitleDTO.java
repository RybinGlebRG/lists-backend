package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.model.TitleStatus;
import ru.rerumu.lists.model.VideoType;

import java.util.Date;

public class TitleDTO implements EntityDTO<Title>, SeriesItemDTO {
    public Integer titleId;
    public String name;
    public Date createDateUTC;
    public Long listId;
    public TitleStatus titleStatus;
    public VideoType videoType;

    public TitleDTO() {}

    public Title toDomain(){
        return new Title.Builder()
                .titleId(Long.valueOf(titleId))
                .name(name)
                .createDateUTC(createDateUTC)
                .watchListId(listId)
                .statusId((long) titleStatus.statusId())
                .videoType(videoType)
                .build();
    }
}
