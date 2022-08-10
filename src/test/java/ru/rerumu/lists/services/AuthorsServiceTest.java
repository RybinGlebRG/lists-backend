package ru.rerumu.lists.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.repository.AuthorsRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorsServiceTest {

    @Mock
    private AuthorsRepository authorsRepository;

    @Test
    void shouldGetAuthor()throws Exception{
        Author author = new Author(1L,2L,"TestAuthor");

        Mockito.when(authorsRepository.getOne(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(author);

        AuthorsService authorsService = new AuthorsService(authorsRepository);
        Author gotAuthor = authorsService.getAuthor(2L,1L);

        Assertions.assertEquals(author,gotAuthor);
        Mockito.verify(authorsRepository).getOne(2L,1L);
    }

}