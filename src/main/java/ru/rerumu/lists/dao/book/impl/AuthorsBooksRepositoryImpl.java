package ru.rerumu.lists.dao.book.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.mapper.AuthorBookRelationMapper;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.author.AuthorFactory;
import ru.rerumu.lists.model.book.impl.BookFactoryImpl;
import ru.rerumu.lists.model.book.impl.BookImpl;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorsBooksRepositoryImpl implements AuthorsBooksRepository {

    private final AuthorBookRelationMapper authorBookRelationMapper;

    private final BookRepository bookRepository;
    private final AuthorsRepository authorsRepository;

    private final BookFactoryImpl bookFactory;
    private final AuthorFactory authorFactory;

    @Autowired
    public AuthorsBooksRepositoryImpl(
            AuthorBookRelationMapper authorBookRelationMapper,
            BookRepository bookRepository,
            AuthorsRepository authorsRepository, BookFactoryImpl bookFactory, AuthorFactory authorFactory
    ){

        this.authorBookRelationMapper = authorBookRelationMapper;
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
        this.bookFactory = bookFactory;
        this.authorFactory = authorFactory;
    }
    @Override
    public void deleteByAuthor(Long authorId) {
        authorBookRelationMapper.deleteByAuthor(authorId);
    }

    @Override
    public void add(Long bookId, Long authorId, Long userId, Long roleId) {
        authorBookRelationMapper.add(bookId, authorId, userId, roleId);
    }

    @Override
    public List<AuthorBookRelation> getByBookId(Long bookId) {
        List<AuthorBookRelation> authorBookRelationList = new ArrayList<>();
        List<Long> authorIdList = authorBookRelationMapper.getAuthorsByBookId(bookId);
        BookImpl book = (BookImpl) bookFactory.getBook(bookId);
        for (Long authorId: authorIdList){
            AuthorDtoDao authorDtoDao = authorsRepository.getOne(authorId);
            authorBookRelationList.add(new AuthorBookRelation(book, authorFactory.fromDTO(authorDtoDao)));
        }
        return authorBookRelationList;
    }

    @Override
    @NonNull
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
    public void delete(long bookId, long authorId) {
        authorBookRelationMapper.delete(bookId, authorId);
    }
}
