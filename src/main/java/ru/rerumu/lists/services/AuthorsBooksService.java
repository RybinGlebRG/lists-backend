package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.repository.AuthorsBooksRepository;

import java.util.List;

@Service
public class AuthorsBooksService {

    private final AuthorsBooksRepository authorsBooksRepository;

    public AuthorsBooksService(
            AuthorsBooksRepository authorsBooksRepository
    ){
        this.authorsBooksRepository = authorsBooksRepository;
    }

    public List<AuthorBookRelation> getByBookId(Long bookId){
        return authorsBooksRepository.getByBookId(bookId);
    }
}
