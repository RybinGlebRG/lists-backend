package ru.rerumu.lists.dao.user;

import ru.rerumu.lists.model.user.User;

import java.util.List;

public interface UsersRepository {

    User getOne(String name);

    boolean isOwner(String name, Long listId);
    boolean isOwnerAuthor(String name, Long authorId);

    boolean isOwnerBook(String name, Long bookId);
    boolean isOwnerSeries(String name, Long seriesId);

    User findById(Long userId);
    List<User> findByIds(List<Long> userIds);
}
