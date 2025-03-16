package ru.rerumu.lists.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.repository.AuthorsBooksRepository;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.services.author.AuthorsService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthorsServiceTest {

    @Mock
    private AuthorsRepository authorsRepository;
    @Mock
    private AuthorsBooksRepository authorsBooksRepository;


    @Test
    void shouldGetAuthor()throws Exception{
        Author author = new Author(1L,2L,"TestAuthor");

        Mockito.when(authorsRepository.getOne(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(author));

        AuthorsService authorsService = new AuthorsService(authorsRepository,authorsBooksRepository);
        Optional<Author> gotAuthor = authorsService.getAuthor(2L,1L);

        Assertions.assertTrue(gotAuthor.isPresent());

        Assertions.assertEquals(author,gotAuthor.get());
        Mockito.verify(authorsRepository).getOne(2L,1L);
    }

}