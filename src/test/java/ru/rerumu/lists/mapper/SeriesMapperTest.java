package ru.rerumu.lists.mapper;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.series.SeriesDTO;

import java.util.List;

@SpringBootTest
public class SeriesMapperTest {

    @Autowired
    private SeriesMapper seriesMapper;

    @Test
    void shouldLoad(){
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.INFO);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        List<SeriesDTO> seriesList = seriesMapper.getAll(2L);
        logger.debug("shouldLoad: "+seriesList.toString());

    }
}
