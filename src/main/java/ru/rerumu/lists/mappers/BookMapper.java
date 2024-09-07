package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.dto.BookDTO;

import java.util.Date;
import java.util.List;

public interface BookMapper {

    BookDTO getOne(Long bookId);

    void update(
            Long readListId,
            Long bookId,
            String title,
            Integer statusId,
            Date insertDate,
            Date lastUpdateDate,
            Integer lastChapter,
            Integer bookTypeId,
            String note
    );

    void updateAuthor(
            Long readListId,
            Long bookId,
            Long authorListId,
            Long authorId
    );

    List<BookDTO> getAll(Long readListId);
    List<BookDTO> getAllChained(Long readListId);

    Long getNextId();

    void addOne(Long bookId,
                Long readListId,
                String title,
                Integer statusId,
                Date insertDate,
                Date lastUpdateDate,
                Integer lastChapter,
                Integer bookTypeId,
                String note
    );

    void delete (long bookId);

    User getBookUser(Long bookId);
}
