package ru.rerumu.lists.dao.book.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.BookDTO;

import java.util.Date;
import java.util.List;

@Mapper
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
            String note,
            String URL
    );

    void updateAuthor(
            Long readListId,
            Long bookId,
            Long authorListId,
            Long authorId
    );

    List<BookDTO> getAll(Long readListId);
    List<BookDTO> getAllChained(Long readListId);
//    List<BookDTO> getPrevious(Long bookId);
    List<BookDTO> findByUser(Long userId);
    List<BookOrderedDTO> findPrevious(List<Long> bookIds);

    Long getNextId();

    void addOne(Long bookId,
                Long readListId,
                String title,
                Integer statusId,
                Date insertDate,
                Date lastUpdateDate,
                Integer lastChapter,
                Integer bookTypeId,
                String note,
                String URL,
                Long userId
    );

    void delete (long bookId);

    User getBookUser(Long bookId);
}
