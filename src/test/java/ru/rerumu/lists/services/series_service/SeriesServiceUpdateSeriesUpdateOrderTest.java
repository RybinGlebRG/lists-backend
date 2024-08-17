package ru.rerumu.lists.services.series_service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.series_update.SeriesUpdateItem;
import ru.rerumu.lists.views.series_update.SeriesUpdateView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// TODO: Fix tests
@ExtendWith(MockitoExtension.class)
class SeriesServiceUpdateSeriesUpdateOrderTest {
    @Mock
    private SeriesRepository seriesRepository;
    @Mock
    private SeriesBooksRespository seriesBooksRespository;

    @Mock
    private ReadListService readListService;


    private BookSeriesRelationService bookSeriesRelationService;

    @Disabled
    @Test
    void shouldUpdateOrder()throws Exception{
        List<SeriesUpdateItem> seriesUpdateItemList = new ArrayList<>();
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,0L,0L));
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,1L,0L));
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,2L,0L));
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,3L,0L));
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,4L,0L));
        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,5L,0L));
        SeriesUpdateView seriesUpdateView = new SeriesUpdateView("Series",seriesUpdateItemList);
        Book book = new Book.Builder()
                .bookId(5L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .readListId(3L)
                .build();
        Series series = new Series.Builder()
                .seriesId(6L)
                .readListId(3L)
                .title("Series")
                .build();

        List<SeriesBookRelation> relationList = new ArrayList<>();
        relationList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(0L)
                        .title("10")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                0L
        ));
        relationList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(1L)
                        .title("1")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                1L
        ));
        relationList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(3L)
                        .title("3")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                2L
        ));
        relationList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(4L)
                        .title("4")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                3L
        ));
        relationList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(2L)
                        .title("2")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                4L
        ));
        relationList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(5L)
                        .title("5")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                5L
        ));

        when(seriesBooksRespository.getBySeriesId(anyLong())).thenReturn(relationList);

        SeriesServiceImpl seriesService = new SeriesServiceImpl(seriesRepository,bookSeriesRelationService,seriesBooksRespository, readListService);
        seriesService.updateSeries(3L,seriesUpdateView);


        List<SeriesBookRelation> updateRelationsList = new ArrayList<>();
        updateRelationsList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(2L)
                        .title("2")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                2L
        ));
        updateRelationsList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(3L)
                        .title("3")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                3L
        ));
        updateRelationsList.add(new SeriesBookRelation(
                new Book.Builder()
                        .bookId(4L)
                        .title("4")
                        .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                        .bookStatus(new BookStatusRecord(1,"In Progress"))
                        .readListId(3L)
                        .build(),
                series,
                4L
        ));


        verify(seriesBooksRespository).save(updateRelationsList);
    }

    @Disabled
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
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .readListId(3L)
                .build();
        Series series = new Series.Builder()
                .seriesId(6L)
                .readListId(3L)
                .title("Series")
                .build();

        when(seriesBooksRespository.getBySeriesId(anyLong())).thenReturn(List.of(new SeriesBookRelation(book,series,0L)));

        SeriesServiceImpl seriesService = new SeriesServiceImpl(seriesRepository,bookSeriesRelationService,seriesBooksRespository, readListService);
        seriesService.updateSeries(3L,seriesUpdateView);

        verify(seriesBooksRespository).save(List.of());
    }
}