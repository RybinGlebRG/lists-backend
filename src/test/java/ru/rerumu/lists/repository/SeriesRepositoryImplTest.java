package ru.rerumu.lists.repository;

import org.junit.jupiter.api.Test;
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
        List<Series> seriesList = seriesRepository.getAll(2L);
        System.out.println(seriesList);
    }
}
