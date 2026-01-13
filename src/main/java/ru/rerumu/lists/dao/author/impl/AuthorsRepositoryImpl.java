package ru.rerumu.lists.dao.author.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.author.mapper.AuthorMapper;
import ru.rerumu.lists.dao.user.UserDtoDao;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.author.impl.AuthorImpl;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class AuthorsRepositoryImpl implements AuthorsRepository {

    private final AuthorMapper authorMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public AuthorsRepositoryImpl(
            AuthorMapper authorMapper,
            UsersRepository usersRepository
    ) {
        this.authorMapper = authorMapper;
        this.usersRepository = usersRepository;
    }

    @Override
    public AuthorDtoDao getOne(Long authorId) {
        AuthorDtoDao dto = authorMapper.findById(authorId, null);
        if (dto == null) {
            throw new EntityNotFoundException();
        }
        return dto;
    }

    @Override
    public List<AuthorDtoDao> getAll(User user) {
        return authorMapper.findByUser(user);
    }

    @Override
    public void addOne(AuthorDtoDao author) {
        authorMapper.create(author);
    }

    @Override
    public Long getNextId() {
        return authorMapper.getNextId();
    }

    @Override
    public void deleteOne(Long authorId) {
        authorMapper.delete(authorId, null);
    }

    @Override
    public void addToBook(Long authorId, Long bookId) {

    }

    @Override
    public List<AuthorDtoDao> findByIds(List<Long> ids) {
        return authorMapper.findByIds(ids, null);
    }

    @Override
    public @NonNull Author create(@NonNull String name, @NonNull User user) {
        Long nextId = getNextId();
        AuthorImpl newAuthor = new AuthorImpl(nextId, name, user);

        UserDtoDao userDtoDao = new UserDtoDao(
                user.getId(),
                user.getName(),
                user.getPassword()
        );
        AuthorDtoDao authorDtoDao = new AuthorDtoDao(
                newAuthor.getId(),
                newAuthor.getName(),
                userDtoDao
        );
        addOne(authorDtoDao);

        return newAuthor;
    }

    @Override
    public @NonNull Author findById(@NonNull Long authorId, @NonNull User user) {
        // TODO: check user in *.xml
        AuthorDtoDao dto = authorMapper.findById(authorId, user.getId());
        if (dto == null) {
            throw new EntityNotFoundException();
        }

        return new AuthorImpl(
                dto.getAuthorId(),
                dto.getName(),
                user
        );
    }

    @Override
    public @NonNull List<Author> findByUser(@NonNull User user) {
        List<AuthorDtoDao> authorDtoList = authorMapper.findByUser(user);

        return authorDtoList.stream()
                .map(dto -> new AuthorImpl(
                        dto.getAuthorId(),
                        dto.getName(),
                        user
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public @NonNull Author fromDTO(@NonNull AuthorDtoDao authorDtoDao) {
        User user = usersRepository.findById(authorDtoDao.getUser().getUserId());
        return new AuthorImpl(
                authorDtoDao.getAuthorId(),
                authorDtoDao.getName(),
                user
        );
    }
}
