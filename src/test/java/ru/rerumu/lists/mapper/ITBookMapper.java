package ru.rerumu.lists.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rerumu.lists.dao.book.mapper.BookMapper;

@SpringBootTest
public class ITBookMapper {

    @Autowired
    BookMapper bookMapper;


//    @Test
//    void shouldFindAll(){
//        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
//                .setLevel(Level.INFO);
//
//        List<BookDTO> res = bookMapper.getAllChained(2L);
//        Assertions.assertTrue(res.size() >0);
//    }

//    @Test
//    void shouldFindNoType(){
//        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
//                .setLevel(Level.TRACE);
//
//        BookDTO res = bookMapper.getOne(477L);
//        Assertions.assertNull(res.bookTypeObj);
//    }

    // TODO: Test chained
}
