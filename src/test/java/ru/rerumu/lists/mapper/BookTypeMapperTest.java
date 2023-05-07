package ru.rerumu.lists.mapper;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rerumu.lists.mappers.BookStatusMapper;
import ru.rerumu.lists.mappers.BookTypeMapper;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.dto.BookTypeDTO;
import ru.rerumu.lists.model.dto.EntityDTO;

import java.util.List;

@SpringBootTest
public class BookTypeMapperTest {

    @Autowired
    BookTypeMapper bookTypeMapper;


    @Test
    void shouldFindAll(){
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.INFO);

        List<EntityDTO<BookType>> res = bookTypeMapper.findAll();

        Assertions.assertTrue(res.size()>0);
    }
}
