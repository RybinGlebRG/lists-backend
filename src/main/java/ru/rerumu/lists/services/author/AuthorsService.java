package ru.rerumu.lists.services.author;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.dao.repository.AuthorsBooksRepository;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.views.AddAuthorView;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthorsRepository authorsRepository;
    private final AuthorsBooksRepository authorsBooksRepository;

    public AuthorsService(
            AuthorsRepository authorsRepository,
            AuthorsBooksRepository authorsBooksRepository
            ){
        this.authorsRepository = authorsRepository;
        this.authorsBooksRepository = authorsBooksRepository;
    }

    public Optional<Author> getAuthor(Long readListId, Long authorId) {
        if (authorId == null || readListId == null){
            throw new IllegalArgumentException();
        }
        return authorsRepository.getOne(readListId, authorId);
    }

    public List<Author> getAuthors(Long readListId) {
        return authorsRepository.getAll(readListId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Author addAuthor(Long readListId, AddAuthorView addAuthorView) throws EntityNotFoundException {
        Long nextId = authorsRepository.getNextId();
        Author newAuthor = new Author.Builder()
                .name(addAuthorView.getName())
                .authorId(nextId)
                .readListId(readListId)
                .build();
        authorsRepository.addOne(newAuthor);
        Optional<Author> author = getAuthor(readListId,nextId);
        if (author.isEmpty()){
            throw new EntityNotFoundException();
        }
        return author.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAuthor(Long authorId) {
        authorsBooksRepository.deleteByAuthor(authorId);
        authorsRepository.deleteOne(authorId);
    }
}
