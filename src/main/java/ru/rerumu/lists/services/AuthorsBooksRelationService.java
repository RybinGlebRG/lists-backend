package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.repository.AuthorsBooksRepository;

import java.util.List;

@Service
public class AuthorsBooksRelationService {

    private final AuthorsBooksRepository authorsBooksRepository;

    public AuthorsBooksRelationService(
            AuthorsBooksRepository authorsBooksRepository
    ){
        this.authorsBooksRepository = authorsBooksRepository;
    }

    public List<AuthorBookRelation> getByBookId(Long bookId){
        return authorsBooksRepository.getByBookId(bookId);
    }

    public void delete(long bookId, long authorId, long readListId){
        authorsBooksRepository.delete(bookId, authorId, readListId);
    }

    public void add(AuthorBookRelation authorBookRelation){
        authorsBooksRepository.add(
                authorBookRelation.getBook().getBookId(),
                authorBookRelation.getAuthor().getAuthorId(),
                authorBookRelation.getBook().getReadListId()
        );
    }
}
