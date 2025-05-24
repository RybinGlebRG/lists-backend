package ru.rerumu.lists.model.author;

import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.model.user.User;

import java.util.List;

public interface AuthorFactory {

    Author create(String name, User user);
    Author findById(Long authorId);
    List<Author> findByIds(List<Long> authorIds);
    List<Author> findAll(User user);
    Author fromDTO(AuthorDtoDao authorDTO);
    List<Author> fromDTO(List<AuthorDtoDao> authorDTOs);

}
