package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.dao.book.AuthorRole;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.domain.AuthorBookRelation;

@Service
public class AuthorsBooksRelationService {

    private final AuthorsBooksRepository authorsBooksRepository;

    public AuthorsBooksRelationService(
            AuthorsBooksRepository authorsBooksRepository
    ){
        this.authorsBooksRepository = authorsBooksRepository;
    }

    public void delete(long bookId, long authorId){
        authorsBooksRepository.delete(bookId, authorId);
    }

    public void add(AuthorBookRelation authorBookRelation){
        authorsBooksRepository.add(
                authorBookRelation.getBook().getId(),
                authorBookRelation.getAuthor().getId(),
                authorBookRelation.getBook().getListId(),
                AuthorRole.TEXT_AUTHOR.getId()
        );
    }
}
