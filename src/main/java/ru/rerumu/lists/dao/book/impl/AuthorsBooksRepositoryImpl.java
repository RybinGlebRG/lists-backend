package ru.rerumu.lists.dao.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.book.AuthorBookDto;
import ru.rerumu.lists.dao.book.AuthorRole;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.mapper.AuthorBookRelationMapper;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorsBooksRepositoryImpl implements AuthorsBooksRepository {

    private final AuthorBookRelationMapper authorBookRelationMapper;

    private final AuthorsRepository authorsRepository;

    @Autowired
    public AuthorsBooksRepositoryImpl(
            AuthorBookRelationMapper authorBookRelationMapper,
            AuthorsRepository authorsRepository
    ){

        this.authorBookRelationMapper = authorBookRelationMapper;
        this.authorsRepository = authorsRepository;
    }
    @Override
    public void deleteByAuthor(Long authorId) {
        authorBookRelationMapper.deleteByAuthor(authorId);
    }

    @Override
    public void addAuthorTo(Long bookId, Long authorId, Long userId, Long roleId) {
        authorBookRelationMapper.add(bookId, authorId, userId, roleId);
    }

    @Override
    public void addAuthorTo(Author author, Book book, AuthorRole role) {
        authorBookRelationMapper.add(book.getId(), author.getId(), book.getUser().getId(), role.getId());
    }

    @Override
    @NonNull
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false)
    public List<AuthorDtoDao> getAuthorsByBookId(@NonNull Long bookId) {
        List<Long> authorIdList = authorBookRelationMapper.getAuthorsByBookId(bookId);
        List<AuthorDtoDao> authors = new ArrayList<>();
        for (Long authorId: authorIdList){
            AuthorDtoDao authorDtoDao = authorsRepository.getOne(authorId);
            authors.add(authorDtoDao);
        }
        return authors;
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<AuthorBookDto> getAllByUserId(@NonNull Long userId) {
        return authorBookRelationMapper.getByUserId(userId);
    }

    @Override
    public void delete(long bookId, long authorId) {
        authorBookRelationMapper.delete(bookId, authorId);
    }
}
