package ru.rerumu.lists.services.author;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.model.author.Author;
import ru.rerumu.lists.model.author.AuthorFactory;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.views.AddAuthorView;

import java.util.List;

@Service
@Slf4j
public class AuthorsService {

    private final AuthorsRepository authorsRepository;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final AuthorFactory authorFactory;

    @Autowired
    public AuthorsService(
            @NonNull AuthorsRepository authorsRepository,
            @NonNull AuthorsBooksRepository authorsBooksRepository,
            @NonNull AuthorFactory authorFactory
    ){
        this.authorsRepository = authorsRepository;
        this.authorsBooksRepository = authorsBooksRepository;
        this.authorFactory = authorFactory;
    }

    public Author getAuthor(@NonNull Long authorId) {
        return authorFactory.findById(authorId);
    }

    public List<Author> getAuthors(@NonNull User user) {
        return authorFactory.findAll(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Author addAuthor(@NonNull AddAuthorView addAuthorView, @NonNull User user) throws EntityNotFoundException {
        Author author = authorFactory.create(addAuthorView.getName(), user);
        return author;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAuthor(@NonNull Long authorId) {
        authorsBooksRepository.deleteByAuthor(authorId);
        authorsRepository.deleteOne(authorId);
    }
}
