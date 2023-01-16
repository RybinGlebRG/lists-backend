package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.BookType;

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
        BookType bookType
) {}
