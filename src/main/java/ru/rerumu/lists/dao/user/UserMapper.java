package ru.rerumu.lists.dao.user;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.user.User;

@Mapper
public interface UserMapper extends CrudMapper<User,Long,User> {

    User getOne(String name);

    int count(String name, Long listId);
    int countAuthor(String name, Long authorId);

    int countSeries(String name, long seriesId);

    int countBooks(String name, long bookId);

//    @Override
//    User findById(Long id);

//    @Override
//    default List<User> findAll(User user){
//        return new ArrayList<>();
//    };

//    @Override
//    default void save(User entity){
//        return;
//    };
}
