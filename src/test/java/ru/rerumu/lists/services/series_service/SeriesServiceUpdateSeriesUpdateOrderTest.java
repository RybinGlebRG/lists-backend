package ru.rerumu.lists.services.series_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.BookUpdateView;
import ru.rerumu.lists.views.series_update.SeriesUpdateItem;
import ru.rerumu.lists.views.series_update.SeriesUpdateView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeriesServiceUpdateSeriesUpdateOrderTest {
    @Mock
    private SeriesRepository seriesRepository;
    @Mock
    private SeriesBooksRespository seriesBooksRespository;


    private BookSeriesRelationService bookSeriesRelationService;

    @Test
    void shouldUpdateOrder()throws Exception{
        List<SeriesUpdateItem> seriesUpdateItemList = new ArrayList<>();
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,5L,0L));
        SeriesUpdateView seriesUpdateView = new SeriesUpdateView("Series",seriesUpdateItemList);
        Book book = new Book.Builder()
                .bookId(5L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.IN_PROGRESS)
                .readListId(3L)
                .build();
        Series series = new Series.Builder()
                .seriesId(6L)
                .readListId(3L)
                .title("Series")
                .build();
        Optional<SeriesBookRelation> optionalSeriesBookRelation = Optional.of(new SeriesBookRelation(book,series,2L));
        SeriesBookRelation shouldSeriesBookRelation = new SeriesBookRelation(book,series,0L);

        when(seriesBooksRespository.getByIds(anyLong(),anyLong())).thenReturn(optionalSeriesBookRelation);

        SeriesService seriesService = new SeriesService(seriesRepository,bookSeriesRelationService,seriesBooksRespository);
        seriesService.updateSeries(3L,seriesUpdateView);

        verify(seriesBooksRespository).update(shouldSeriesBookRelation);
    }

    @Test
    void shouldNotUpdateOrder()throws Exception{
        List<SeriesUpdateItem> seriesUpdateItemList = new ArrayList<>();
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,5L,0L));
        SeriesUpdateView seriesUpdateView = new SeriesUpdateView("Series",seriesUpdateItemList);
        Book book = new Book.Builder()
                .bookId(5L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.IN_PROGRESS)
                .readListId(3L)
                .build();
        Series series = new Series.Builder()
                .seriesId(6L)
                .readListId(3L)
                .title("Series")
                .build();
        Optional<SeriesBookRelation> optionalSeriesBookRelation = Optional.of(new SeriesBookRelation(book,series,0L));

        when(seriesBooksRespository.getByIds(anyLong(),anyLong())).thenReturn(optionalSeriesBookRelation);

        SeriesService seriesService = new SeriesService(seriesRepository,bookSeriesRelationService,seriesBooksRespository);
        seriesService.updateSeries(3L,seriesUpdateView);

        verify(seriesBooksRespository, never()).update(any());
    }
}