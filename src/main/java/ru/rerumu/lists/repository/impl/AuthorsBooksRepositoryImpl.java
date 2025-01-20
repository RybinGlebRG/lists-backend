package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.AuthorBookRelationMapper;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.repository.AuthorsBooksRepository;
import ru.rerumu.lists.repository.AuthorsRepository;
import ru.rerumu.lists.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AuthorsBooksRepositoryImpl implements AuthorsBooksRepository {

    private final AuthorBookRelationMapper authorBookRelationMapper;

    private final BookRepository bookRepository;
    private final AuthorsRepository authorsRepository;

    public AuthorsBooksRepositoryImpl(
            AuthorBookRelationMapper authorBookRelationMapper,
            BookRepository bookRepository,
            AuthorsRepository authorsRepository
    ){

        this.authorBookRelationMapper = authorBookRelationMapper;
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
    }
    @Override
    public void deleteByAuthor(Long authorId) {
        authorBookRelationMapper.deleteByAuthor(authorId);
    }

    @Override
    public void add(Long bookId, Long authorId, Long readListId) {
        authorBookRelationMapper.add(bookId, authorId, readListId);
    }

    @Override
    public List<AuthorBookRelation> getByBookId(Long bookId, Long readListId) {
        List<AuthorBookRelation> authorBookRelationList = new ArrayList<>();
        List<Long> authorIdList = authorBookRelationMapper.getAuthorsByBookId(bookId);
        BookImpl book = bookRepository.getOne(readListId,bookId);
        for (Long authorId: authorIdList){
            Optional<Author>  authorOptional = authorsRepository.getOne(readListId,authorId);
            if (authorOptional.isEmpty()){
                throw new RuntimeException("authorOptional is empty");
            }
            authorOptional.ifPresent(author -> authorBookRelationList.add(new AuthorBookRelation(book, author)));
        }
        return authorBookRelationList;
    }

    @Override
    public void delete(long bookId, long authorId, long readListId) {
        authorBookRelationMapper.delete(bookId, authorId, readListId);
    }
}
