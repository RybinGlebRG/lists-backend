package ru.rerumu.lists.dao.book.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.book.AuthorBookDto;

import java.util.List;

@Mapper
public interface AuthorBookRelationMapper {

    void deleteByAuthor(Long authorId);

    void add(Long bookId, Long authorId, Long userId, Long roleId);

//    List<AuthorBookRelation> getByBookId(Long bookId);

    void delete(long bookId, long authorId);

    List<Long> getAuthorsByBookId(long bookId);

    List<AuthorBookDto> getByUserId(Long userId);
}
