package ru.rerumu.lists.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;

@SpringBootTest
public class SeriesMapperTest {

    @Autowired
    private SeriesMapper seriesMapper;

//    @Test
//    void shouldLoad(){
//        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
//                .setLevel(Level.INFO);
//        Logger logger = LoggerFactory.getLogger(this.getClass());
//        List<SeriesDTO> seriesList = seriesMapper.getAll(2L);
//        logger.debug("shouldLoad: "+seriesList.toString());
//
//    }
}
