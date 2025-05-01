package ru.rerumu.lists.dao.book;

import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.model.AuthorBookRelation;

import java.util.List;

public interface AuthorsBooksRepository {

    void deleteByAuthor(Long authorId);

    void add(Long bookId, Long authorId, Long userId, Long roleID);

    List<AuthorBookRelation> getByBookId(Long bookId);
    List<AuthorDtoDao> getAuthorsByBookId(Long bookId);

    void delete(long bookId, long authorId);
}
