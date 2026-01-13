package ru.rerumu.lists.dao.book;

import lombok.NonNull;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;

import java.util.List;

public interface AuthorsBooksRepository {

    void deleteByAuthor(Long authorId);

    @Deprecated
    void addAuthorTo(Long bookId, Long authorId, Long userId, Long roleID);
    void addAuthorTo(Author author, Book book, AuthorRole role);

    List<AuthorDtoDao> getAuthorsByBookId(Long bookId);

    void delete(long bookId, long authorId);

    List<AuthorBookDto> getAllByUserId(@NonNull Long userId);
}
