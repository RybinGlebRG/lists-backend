package ru.rerumu.lists.dao.title;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.domain.title.Title;

import java.util.Date;
import java.util.List;

@Mapper
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
