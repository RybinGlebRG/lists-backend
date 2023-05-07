package ru.rerumu.lists.mapper;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rerumu.lists.mappers.BookStatusMapper;
import ru.rerumu.lists.mappers.UserMapper;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.services.UserService;

import java.util.List;

@SpringBootTest
public class BookStatusMapperTest {

    @Autowired
    BookStatusMapper bookStatusMapper;


    @Test
    void shouldFindAll(){
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.INFO);

        List<BookStatusRecord> res = bookStatusMapper.findAll();

        Assertions.assertEquals(3,res.size());
        Assertions.assertTrue(res.contains(new BookStatusRecord(1,"In progress")));
        Assertions.assertTrue(res.contains(new BookStatusRecord(2,"Completed")));
        Assertions.assertTrue(res.contains(new BookStatusRecord(3,"Expecting")));
    }
}
