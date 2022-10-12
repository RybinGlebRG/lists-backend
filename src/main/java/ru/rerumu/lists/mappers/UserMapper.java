package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.User;

public interface UserMapper {

    User getOne(String name);

    int count(String name, Long listId);
    int countAuthor(String name, Long authorId);

    int countSeries(String name, long seriesId);

    int countBooks(String name, long bookId);
}
