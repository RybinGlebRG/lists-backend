package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.dao.repository.AuthorsBooksRepository;

import java.util.List;

@Service
public class AuthorsBooksRelationService {

    private final AuthorsBooksRepository authorsBooksRepository;

    public AuthorsBooksRelationService(
            AuthorsBooksRepository authorsBooksRepository
    ){
        this.authorsBooksRepository = authorsBooksRepository;
    }

    public List<AuthorBookRelation> getByBookId(Long bookId, Long readListId){
        return authorsBooksRepository.getByBookId(bookId, readListId);
    }

    public void delete(long bookId, long authorId, long readListId){
        authorsBooksRepository.delete(bookId, authorId, readListId);
    }

    public void add(AuthorBookRelation authorBookRelation){
        authorsBooksRepository.add(
                authorBookRelation.getBook().getId(),
                authorBookRelation.getAuthor().getId(),
                authorBookRelation.getBook().getListId()
        );
    }
}
