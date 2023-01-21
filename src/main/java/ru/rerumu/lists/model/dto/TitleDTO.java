package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.model.TitleStatus;
import ru.rerumu.lists.model.VideoType;

import java.util.Date;

public class TitleDTO {
    public Integer titleId;
    public String name;
    public Date createDateUTC;
    public Long listId;
    public TitleStatus titleStatus;
    public VideoType videoType;

    public TitleDTO() {}

    public Title toDomain(){
        throw new RuntimeException("Not implemented");
    }
}
