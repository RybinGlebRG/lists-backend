package ru.rerumu.lists.dao.author;

import ru.rerumu.lists.model.author.impl.AuthorImpl;
import ru.rerumu.lists.model.user.User;

import java.util.List;
import java.util.Optional;

public interface AuthorsRepository {

    Optional<AuthorImpl> getOne(Long readListId, Long authorId);
//    List<AuthorImpl> getAll(Long readListId);

    AuthorDtoDao getOne(Long authorId);
    List<AuthorDtoDao> getAll(User user);

    void addOne(AuthorDtoDao author);

    Long getNextId();

    void deleteOne(Long authorId);
}
