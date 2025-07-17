package ru.rerumu.lists.domain.dto;

import ru.rerumu.lists.domain.BookStatus;
import ru.rerumu.lists.domain.book.type.BookType;

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
