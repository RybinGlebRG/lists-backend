package ru.rerumu.lists.dao.series;

import ru.rerumu.lists.domain.BookStatus;
import ru.rerumu.lists.domain.booktype.impl.BookTypeImpl;

import java.util.Date;

public record SeriesBookRelationDTO(
        Long seriesId,
        Long seriesListId,

        Long order,
        Long bookId,
        Long bookListId,
        String title,
        BookStatus bookStatus,
        Date insertDate,
        Date lastUpdateDate,
        Integer lastChapter,
        BookTypeImpl bookType
) {}
