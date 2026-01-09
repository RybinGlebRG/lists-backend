package ru.rerumu.lists.services.author;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.views.AddAuthorView;

import java.util.List;

@Service
@Slf4j
public class AuthorsService {

    private final AuthorsRepository authorsRepository;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public AuthorsService(
            @NonNull AuthorsRepository authorsRepository,
            @NonNull AuthorsBooksRepository authorsBooksRepository,
            @NonNull UsersRepository usersRepository
    ){
        this.authorsRepository = authorsRepository;
        this.authorsBooksRepository = authorsBooksRepository;
        this.usersRepository = usersRepository;
    }

    public Author getAuthor(@NonNull Long authorId, @NonNull Long userId) {
        User user = usersRepository.findById(userId);
        return authorsRepository.findById(authorId, user);
    }

    public List<Author> getAuthors(@NonNull User user) {
        return authorsRepository.findByUser(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Author addAuthor(@NonNull AddAuthorView addAuthorView, @NonNull User user) throws EntityNotFoundException {
        return authorsRepository.create(addAuthorView.getName(), user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAuthor(@NonNull Long authorId) {
        authorsBooksRepository.deleteByAuthor(authorId);
        authorsRepository.deleteOne(authorId);
    }
}
