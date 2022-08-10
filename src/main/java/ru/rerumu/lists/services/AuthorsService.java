package ru.rerumu.lists.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.repository.AuthorsRepository;
import ru.rerumu.lists.views.AddAuthorView;

import java.util.List;

@Service
public class AuthorsService {

    private final AuthorsRepository authorsRepository;

    public AuthorsService(AuthorsRepository authorsRepository){
        this.authorsRepository = authorsRepository;
    }

    public Author getAuthor(Long readListId, Long authorId) {
        return authorsRepository.getOne(readListId, authorId);
    }

    public List<Author> getAuthors(Long readListId) {
        return authorsRepository.getAll(readListId);
    }


    // TODO: Test transactions
    @Transactional(rollbackFor = Exception.class)
    public Author addAuthor(Long readListId, AddAuthorView addAuthorView) {
        Long nextId = authorsRepository.getNextId();
        Author newAuthor = new Author.Builder()
                .name(addAuthorView.getName())
                .authorId(nextId)
                .readListId(readListId)
                .build();
        authorsRepository.addOne(newAuthor);
        return authorsRepository.getOne(readListId,nextId);
    }
}
