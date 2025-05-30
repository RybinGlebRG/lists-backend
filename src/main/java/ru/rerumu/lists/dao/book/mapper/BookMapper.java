package ru.rerumu.lists.dao.book.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.model.author.Author;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.user.User;

import java.util.Date;
import java.util.List;

@Mapper
public interface BookMapper extends CrudMapper<BookDtoDao, Long, BookDtoDao> {

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

    List<BookDTO> findByUser(Long userId);
    List<BookDtoDao> findByUserChained(Long userId);

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

    List<Author> getAuthors(Long bookId, Long roleId);
}
