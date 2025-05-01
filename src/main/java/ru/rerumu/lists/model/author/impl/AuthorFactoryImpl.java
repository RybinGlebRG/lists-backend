package ru.rerumu.lists.model.author.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.user.UserDtoDao;
import ru.rerumu.lists.model.author.Author;
import ru.rerumu.lists.model.author.AuthorDTO;
import ru.rerumu.lists.model.author.AuthorFactory;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.user.UserDTO;
import ru.rerumu.lists.model.user.UserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthorFactoryImpl implements AuthorFactory {

    private final AuthorsRepository authorsRepository;
    private final UserFactory userFactory;

    @Autowired
    public AuthorFactoryImpl(AuthorsRepository authorsRepository, UserFactory userFactory) {
        this.authorsRepository = authorsRepository;
        this.userFactory = userFactory;
    }

    @Override
    public Author create(String name, User user) {
        Long nextId = authorsRepository.getNextId();
        AuthorImpl newAuthor = new AuthorImpl(nextId, null, name, user);
        AuthorDTO authorDTO = newAuthor.toDTO();
        UserDTO userDTO = authorDTO.getUser();
        authorsRepository.addOne(new AuthorDtoDao(
                authorDTO.getAuthorId(),
                authorDTO.getName(),
                new UserDtoDao(
                        userDTO.userId(),
                        userDTO.name(),
                        userDTO.password()
                )
        ));
        return newAuthor;
    }

    @Override
    public Author findById(Long authorId) {
        return fromDTO(authorsRepository.getOne(authorId));
    }

    @Override
    public List<Author> findByIds(List<Long> authorIds) {
        return fromDTO(authorsRepository.findByIds(authorIds));
    }

    @Override
    public List<Author> findAll(User user) {
        return fromDTO(authorsRepository.getAll(user));
    }

    @Override
    public Author fromDTO(AuthorDtoDao authorDTO) {
        return new AuthorImpl(
                authorDTO.getAuthorId(),
                null,
                authorDTO.getName(),
                userFactory.fromDTO(authorDTO.getUser())
        );
    }

    @Override
    public List<Author> fromDTO(List<AuthorDtoDao> authorDTOs) {
        return authorDTOs.stream()
                .map(this::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }


}
