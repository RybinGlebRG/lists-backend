package ru.rerumu.lists.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.repository.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private SeriesRepository seriesRepository;
    @Mock
    private AuthorsRepository authorsRepository;
    @Mock
    private AuthorsService authorsService;
    @Mock
    private AuthorsBooksRepository authorsBooksRepository;
    @Mock
    private SeriesBooksRespository seriesBooksRespository;

    @Mock
    private DateFactory dateFactory;

    @Mock
    private SeriesService seriesService;

    @Mock
    private BookSeriesRelationService bookSeriesRelationService;

    @Mock
    private  AuthorsBooksRelationService authorsBooksRelationService;


}