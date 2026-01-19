package ru.rerumu.lists.dao.book.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.dao.book.BookMyBatisEntity;
import ru.rerumu.lists.domain.author.Author;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BookMapper extends CrudMapper<BookMyBatisEntity, Long, BookMyBatisEntity> {

    void update(
            Long readListId,
            Long bookId,
            String title,
            Integer statusId,
            LocalDateTime insertDate,
            LocalDateTime lastUpdateDate,
            Integer lastChapter,
            Long bookTypeId,
            String note,
            String URL,
            Long userId
    );

    List<BookMyBatisEntity> findByUserChained(Long userId);

    void addOne(Long bookId,
                Long readListId,
                String title,
                Integer statusId,
                LocalDateTime insertDate,
                LocalDateTime lastUpdateDate,
                Integer lastChapter,
                Long bookTypeId,
                String note,
                String URL,
                Long userId
    );

    List<Author> getAuthors(Long bookId, Long roleId);

    List<Long> findSeriesIds(Long bookId);
}
