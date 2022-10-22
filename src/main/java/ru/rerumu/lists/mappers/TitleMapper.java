package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.*;
import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.views.TitleCreateView;

import java.util.Date;
import java.util.List;


public interface TitleMapper {
    List<Title> getAllTitles(Long watchListId);
    Title getOne(Long watchListId, Long titleId);
    void update(
            String name,
            Long titleId,
            Long watchListId,
            Date createDateUTC,
            Long statusId,
            Long videoType);

    void addOne(
            Long watchListId,
            Long titleId,
            String name,
            Date createDateUTC,
            Long statusId,
            Long typeId);
    Long getNextId();

    void delete(Long titleId);
}
