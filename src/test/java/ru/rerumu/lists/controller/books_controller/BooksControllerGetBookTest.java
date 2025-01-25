//package ru.rerumu.lists.controller.books_controller;
//
//import io.restassured.RestAssured;
//import io.restassured.module.mockmvc.RestAssuredMockMvc;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.rerumu.lists.controller.BooksController;
//import ru.rerumu.lists.model.*;
//import ru.rerumu.lists.model.book.BookImpl;
//import ru.rerumu.lists.services.*;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.Matchers.equalTo;
//import static org.mockito.Mockito.*;
//
//@WebMvcTest(BooksController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class BooksControllerGetBookTest {
//    private final Logger logger = LoggerFactory.getLogger(BooksControllerGetBookTest.class);
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReadListService readListService;
//
//    @MockBean(name="UserService")
//    private UserService userService1;
//
//    @MockBean(name="UserServiceProtectionProxy")
//    private UserService userService;
//
//    @MockBean
//    private AuthorsService authorsService;
//
//    @MockBean
//    private SeriesServiceImpl seriesService;
//
//    @MockBean
//    private AuthorsBooksRelationService authorsBooksRelationService;
//
//    @MockBean
//    private BookSeriesRelationService bookSeriesRelationService;
//
//    @BeforeAll
//    static void setup() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
//    }
//
//    @BeforeEach
//    void setUp() {
//        RestAssuredMockMvc.mockMvc(mockMvc);
//    }
//
//    @Test
//    void shouldGetOne() throws Exception {
//        Date dt = new Date();
//        BookImpl book = new BookImpl.Builder()
//                .readListId(2L)
//                .bookId(3L)
//                .title("Title")
//                .bookStatus(new BookStatusRecord(1,"In Progress"))
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .lastChapter(4)
//                .bookType(new BookType(3,"Webtoon"))
//                .build();
//        Author author = new Author(6L,2L,"Author");
//        Series series = new Series.Builder().seriesId(5L).readListId(2L).title("Series").build();
//
//        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
//                        .thenReturn(book);
//        Mockito.when(authorsBooksRelationService.getByBookId(Mockito.anyLong(), Mockito.anyLong()))
//                        .thenReturn(List.of(new AuthorBookRelation(book,author)));
////        when(bookSeriesRelationService.getByBookId(anyLong(), anyLong()))
////                .thenReturn(List.of(new SeriesBookRelation(book,series,1L)));
//        when(seriesService.findByBook(Mockito.any(BookImpl.class)))
//                .thenReturn(List.of(series));
//
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/books/3")
//                .then().statusCode(200)
//                .and().body("readListId",equalTo(2))
//                .and().body("bookId",equalTo(3))
//                .and().body("title",equalTo("Title"))
//                .and().body("bookStatus.statusId",equalTo(1))
//                .and().body("bookStatus.statusName",equalTo("In Progress"))
//                .and().body("bookType.typeId",equalTo(3))
//                .and().body("bookType.typeName",equalTo("Webtoon"))
//                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
//                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
//                .and().body("lastChapter",equalTo(4))
////                .and().body("series.seriesId",equalTo(5))
////                .and().body("series.seriesListId",equalTo(2))
////                .and().body("series.title",equalTo("Series"))
//                .and().body("authors.findAll{i -> i.authorId == 6 && i.readListId == 2 && i.name == 'Author'}", not(empty()))
//                .and().body("seriesList.findAll{i -> i.seriesId == 5 && i.title == 'Series' }", not(empty()))
//        ;
//
//        verify(userService).checkOwnershipList("Test",2L);
//        verify(readListService).getBook(2L,3L);
//        verify(authorsBooksRelationService).getByBookId(3L, 2L);
////        verify(bookSeriesRelationService).getByBookId(3L,2L);
//        verify(seriesService).findByBook(book);
//    }
//
//
//    @Test
//    void shouldGetOneNoAuthor() throws Exception {
//        Date dt = new Date();
//        BookImpl book = new BookImpl.Builder()
//                .readListId(2L)
//                .bookId(3L)
//                .title("Title")
//                .bookStatus(new BookStatusRecord(1,"In Progress"))
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .lastChapter(4)
//                .build();
//        Series series = new Series.Builder().seriesId(5L).readListId(2L).title("Series").build();
//        when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong())).thenReturn(book);
////        when(bookSeriesRelationService.getByBookId(anyLong(), anyLong()))
////                .thenReturn(List.of(new SeriesBookRelation(book,series,1L)));
//        when(seriesService.findByBook(Mockito.any(BookImpl.class)))
//                .thenReturn(List.of(series));
//
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/books/3")
//                .then().statusCode(200)
//                .and().body("readListId",equalTo(2))
//                .and().body("bookId",equalTo(3))
//                .and().body("title",equalTo("Title"))
//                .and().body("bookStatus.statusId",equalTo(1))
//                .and().body("bookStatus.statusName",equalTo("In Progress"))
//                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
//                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
//                .and().body("lastChapter",equalTo(4))
////                .and().body("series.seriesId",equalTo(5))
////                .and().body("series.seriesListId",equalTo(2))
////                .and().body("series.title",equalTo("Series"))
//                .and().body("seriesList.findAll{i -> i.seriesId == 5 && i.title == 'Series'}", not(empty()))
//                .and().body("authors",empty())
//        ;
//
//        Mockito.verify(userService).checkOwnershipList("Test",2L);
//        Mockito.verify(readListService).getBook(2L,3L);
////        verify(bookSeriesRelationService).getByBookId(3L,2L);
//        verify(seriesService).findByBook(book);
//    }
//
//
//    @Test
//    void shouldGetOneNoSeries() throws Exception {
//        Date dt = new Date();
//        BookImpl book = new BookImpl.Builder()
//                .readListId(2L)
//                .bookId(3L)
//                .title("Title")
//                .bookStatus(new BookStatusRecord(1,"In Progress"))
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .lastChapter(4)
//                .build();
//        Author author = new Author(6L,2L,"Author");
//
//        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
//                .thenReturn(book);
//        Mockito.when(authorsBooksRelationService.getByBookId(Mockito.anyLong(), Mockito.anyLong()))
//                .thenReturn(List.of(new AuthorBookRelation(book,author)));
//
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/books/3")
//                .then().statusCode(200)
//                .and().body("readListId",equalTo(2))
//                .and().body("bookId",equalTo(3))
//                .and().body("title",equalTo("Title"))
//                .and().body("bookStatus.statusId",equalTo(1))
//                .and().body("bookStatus.statusName",equalTo("In Progress"))
//                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
//                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
//                .and().body("lastChapter",equalTo(4))
//                .and().body("authors.findAll{i -> i.authorId == 6 && i.readListId == 2 && i.name == 'Author'}", not(empty()))
//                .and().body("seriesList", empty())
//        ;
//
//        Mockito.verify(userService).checkOwnershipList("Test",2L);
//        Mockito.verify(readListService).getBook(2L,3L);
//        Mockito.verify(authorsBooksRelationService).getByBookId(3L,2L);
//        verify(bookSeriesRelationService).getByBookId(3L,2L);
//    }
//
//    @Test
//    void shouldGetOneNoChapter() throws Exception {
//        Date dt = new Date();
//        BookImpl book = new BookImpl.Builder()
//                .readListId(2L)
//                .bookId(3L)
//                .title("Title")
//                .bookStatus(new BookStatusRecord(1,"In Progress"))
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .build();
//        Author author = new Author(6L,2L,"Author");
//        Series series = new Series.Builder().seriesId(5L).readListId(2L).title("Series").build();
//
//        when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
//                .thenReturn(book);
//        when(authorsBooksRelationService.getByBookId(Mockito.anyLong(), Mockito.anyLong()))
//                .thenReturn(List.of(new AuthorBookRelation(book,author)));
////        when(bookSeriesRelationService.getByBookId(anyLong(),anyLong()))
////                .thenReturn(List.of(new SeriesBookRelation(book,series,1L)));
//        when(seriesService.findByBook(Mockito.any(BookImpl.class)))
//                .thenReturn(List.of(series));
//
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/books/3")
//                .then().statusCode(200)
//                .and().body("readListId",equalTo(2))
//                .and().body("bookId",equalTo(3))
//                .and().body("title",equalTo("Title"))
//                .and().body("bookStatus.statusId",equalTo(1))
//                .and().body("bookStatus.statusName",equalTo("In Progress"))
//                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
//                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
////                .and().body("series.seriesId",equalTo(5))
////                .and().body("series.seriesListId",equalTo(2))
////                .and().body("series.title",equalTo("Series"))
//                .and().body("authors.findAll{i -> i.authorId == 6 && i.readListId == 2 && i.name == 'Author'}", not(empty()))
//                .and().body("seriesList.findAll{i -> i.seriesId == 5 && i.title == 'Series' }", not(empty()))
//                .and().body("$",not(hasKey("lastChapter")))
//        ;
//
//        Mockito.verify(userService).checkOwnershipList("Test",2L);
//        Mockito.verify(readListService).getBook(2L,3L);
//        Mockito.verify(authorsBooksRelationService).getByBookId(3L,2L);
////        Mockito.verify(bookSeriesRelationService).getByBookId(3L,2L);
//        verify(seriesService).findByBook(book);
//    }
//
//    @Test
//    void shouldNoBook() throws Exception {
//        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
//                .thenReturn(null);
//
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/books/3")
//                .then().statusCode(404)
//                .and().body("error", Matchers.startsWith("ru.rerumu.lists.exception.EntityNotFoundException"))
//                .and().body("errorMessage",equalTo(null));
//        ;
//    }
//
//    @Test
//    void shouldThrowException(){
//        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
//                .thenThrow(IllegalArgumentException.class);
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/books/1")
//                .then().statusCode(500)
//                .and().body("error",equalTo("java.lang.IllegalArgumentException"))
//                .and().body("errorMessage",equalTo(null));
//
//    }
//
//}