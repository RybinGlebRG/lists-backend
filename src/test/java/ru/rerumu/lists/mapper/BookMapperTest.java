package ru.rerumu.lists.mapper;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.BookStatusMapper;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.dto.BookDTO;

import java.util.List;

// TODO: Separate into unit and integration
@SpringBootTest
public class BookMapperTest {

    @Autowired
    BookMapper bookMapper;


    @Test
    void shouldFindAll(){
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.INFO);

        List<BookDTO> res = bookMapper.getAll(2L);
        Assertions.assertTrue(res.size() >0);
    }

    @Test
    void shouldFindNoType(){
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.TRACE);

        BookDTO res = bookMapper.getOne(477L);
        Assertions.assertNull(res.bookTypeObj);
    }
}
