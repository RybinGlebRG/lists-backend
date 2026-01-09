package ru.rerumu.lists.dao.user;

import lombok.NonNull;
import ru.rerumu.lists.dao.base.CrudRepository;
import ru.rerumu.lists.domain.user.User;

import java.util.List;

public interface UsersRepository extends CrudRepository<User,Long> {

    User getOne(String name);

    boolean isOwner(String name, Long listId);
    boolean isOwnerAuthor(String name, Long authorId);

    boolean isOwnerBook(String name, Long bookId);
    boolean isOwnerSeries(String name, Long seriesId);

    User findById(Long userId);
    List<User> findByIds(List<Long> userIds);

    @NonNull
    User create(
            @NonNull Long userId,
            @NonNull String name,
            char[] password
    );
}
