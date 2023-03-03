package ru.rerumu.lists.repository;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.Series;

import java.util.List;

@SpringBootTest
public class SeriesRepositoryImplTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @Test
    void shouldGetAll(){
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.TRACE);
        List<Series> seriesList = seriesRepository.getAll(2L);
        System.out.println(seriesList);
    }
}
