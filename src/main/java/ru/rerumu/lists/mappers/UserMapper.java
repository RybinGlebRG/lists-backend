package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserMapper extends CrudMapper<User,Long,User>{

    User getOne(String name);

    int count(String name, Long listId);
    int countAuthor(String name, Long authorId);

    int countSeries(String name, long seriesId);

    int countBooks(String name, long bookId);

    @Override
    User findById(Long id);

//    @Override
//    default List<User> findAll(User user){
//        return new ArrayList<>();
//    };

//    @Override
//    default void save(User entity){
//        return;
//    };
}
