package ru.rerumu.lists.dao.author;

import lombok.NonNull;
import ru.rerumu.lists.domain.author.Author;
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

    /**
     * Create author
     */
    @NonNull
    Author create(
            @NonNull String name,
            @NonNull User user
    );

    @NonNull
    Author findById(@NonNull Long authorId, @NonNull User user);

    @NonNull
    List<Author> findByUser(@NonNull User user);

    @NonNull
    Author fromDTO(@NonNull AuthorDtoDao authorDtoDao);
}
