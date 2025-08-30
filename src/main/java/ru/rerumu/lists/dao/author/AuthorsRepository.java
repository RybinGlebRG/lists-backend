package ru.rerumu.lists.dao.author;

import ru.rerumu.lists.domain.user.User;

import java.util.List;

public interface AuthorsRepository {

    AuthorDtoDao getOne(Long authorId);
    List<AuthorDtoDao> getAll(User user);
    void addOne(AuthorDtoDao author);
    Long getNextId();
    void deleteOne(Long authorId);

    void addToBook(Long authorId, Long bookId);

    List<AuthorDtoDao> findByIds(List<Long> ids);
}
