package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.repository.SeriesBooksRespository;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookSeriesRelationService {

    private final SeriesBooksRespository seriesBooksRespository;

    public BookSeriesRelationService(
            SeriesBooksRespository seriesBooksRespository
    ) {
        this.seriesBooksRespository = seriesBooksRespository;
    }


    public void update(SeriesBookRelation seriesBookRelation) {
        seriesBooksRespository.update(seriesBookRelation);
    }

    public void add(SeriesBookRelation seriesBookRelation){
        seriesBooksRespository.add(seriesBookRelation);
    }

    public void delete(Long bookId, Long seriesId, Long readListId){
        seriesBooksRespository.delete(bookId, seriesId, readListId);
    }

    public List<SeriesBookRelation> getByBookId(Long bookId, Long readListId){
        return seriesBooksRespository.getByBookId(bookId,readListId);
    }
}
