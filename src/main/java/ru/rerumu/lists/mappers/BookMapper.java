package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.Book;

import java.util.Date;
import java.util.List;


public interface BookMapper {
    Book getOne(Long readListId, Long bookId);
    void update(
            Long readListId,
            Long bookId,
            String title,
            Integer statusId,
            Date insertDate,
            Date lastUpdateDate,
            Integer lastChapter
    );

    void updateAuthor(
            Long readListId,
            Long bookId,
            Long authorListId,
            Long authorId
    );

    List<Book> getAll(Long readListId);
}
