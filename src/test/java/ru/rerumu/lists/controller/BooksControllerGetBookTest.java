package ru.rerumu.lists.controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.BookAddView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerGetBookTest {
    private final Logger logger = LoggerFactory.getLogger(BooksControllerGetBookTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorsService authorsService;

    @MockBean
    private BookSeriesService bookSeriesService;

    @MockBean
    private AuthorsBooksService authorsBooksService;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void shouldGetOne() throws Exception {
        Date dt = new Date();
        Book book = new Book.Builder()
                .readListId(2L)
                .bookId(3L)
                .title("Title")
                .statusId(1)
                .bookStatus(BookStatus.IN_PROGRESS)
                .insertDate(dt)
                .lastUpdateDate(dt)
                .lastChapter(4)
                .seriesId(5L)
                .authorId(6L)
                .seriesOrder(7L)
                .build();
        Author author = new Author(6L,2L,"Author");
        Series series = new Series(5L,2L,"Series");

        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
                        .thenReturn(book);
        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong()))
                        .thenReturn(Optional.of(series));
        Mockito.when(authorsBooksService.getByBookId(Mockito.anyLong()))
                        .thenReturn(List.of(new AuthorBookRelation(book,author)));


        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/3")
                .then().statusCode(200)
                .and().body("readListId",equalTo(2))
                .and().body("bookId",equalTo(3))
                .and().body("title",equalTo("Title"))
                .and().body("statusId",equalTo(1))
                .and().body("bookStatus.statusId",equalTo(1))
                .and().body("bookStatus.statusName",equalTo("In Progress"))
                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("lastChapter",equalTo(4))
                .and().body("seriesId",equalTo(5))
                .and().body("seriesOrder",equalTo(7))
                .and().body("series.seriesId",equalTo(5))
                .and().body("series.seriesListId",equalTo(2))
                .and().body("series.title",equalTo("Series"))
                .and().body("authors.findAll{i -> i.authorId == 6 && i.readListId == 2 && i.name == 'Author'}", not(empty()))
        ;

        Mockito.verify(userService).checkOwnership("Test",2L);
        Mockito.verify(readListService).getBook(2L,3L);
        Mockito.verify(bookSeriesService).getSeries(2L,5L);
        Mockito.verify(authorsBooksService).getByBookId(3L);
    }


    @Test
    void shouldGetOneNoAuthor() throws Exception {
        Date dt = new Date();
        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
                .thenReturn(
                        new Book.Builder()
                                .readListId(2L)
                                .bookId(3L)
                                .title("Title")
                                .statusId(1)
                                .bookStatus(BookStatus.IN_PROGRESS)
                                .insertDate(dt)
                                .lastUpdateDate(dt)
                                .lastChapter(4)
                                .seriesId(5L)
                                .seriesOrder(7L)
                                .build()
                );

        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(
                        Optional.of(new Series(5L,2L,"Series"))
                );


        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/3")
                .then().statusCode(200)
                .and().body("readListId",equalTo(2))
                .and().body("bookId",equalTo(3))
                .and().body("title",equalTo("Title"))
                .and().body("statusId",equalTo(1))
                .and().body("bookStatus.statusId",equalTo(1))
                .and().body("bookStatus.statusName",equalTo("In Progress"))
                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("lastChapter",equalTo(4))
                .and().body("seriesId",equalTo(5))
                .and().body("seriesOrder",equalTo(7))
                .and().body("series.seriesId",equalTo(5))
                .and().body("series.seriesListId",equalTo(2))
                .and().body("series.title",equalTo("Series"))
                .and().body("authors",empty())
        ;

        Mockito.verify(userService).checkOwnership("Test",2L);
        Mockito.verify(readListService).getBook(2L,3L);
        Mockito.verify(bookSeriesService).getSeries(2L,5L);
    }

    @Test
    void shouldGetOneNoSeries() throws Exception {
        Date dt = new Date();
        Book book = new Book.Builder()
                .readListId(2L)
                .bookId(3L)
                .title("Title")
                .statusId(1)
                .bookStatus(BookStatus.IN_PROGRESS)
                .insertDate(dt)
                .lastUpdateDate(dt)
                .lastChapter(4)
                .seriesId(5L)
                .authorId(6L)
                .seriesOrder(7L)
                .build();
        Author author = new Author(6L,2L,"Author");

        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
                .thenReturn(book);
        Mockito.when(authorsBooksService.getByBookId(Mockito.anyLong()))
                .thenReturn(List.of(new AuthorBookRelation(book,author)));


        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/3")
                .then().statusCode(200)
                .and().body("readListId",equalTo(2))
                .and().body("bookId",equalTo(3))
                .and().body("title",equalTo("Title"))
                .and().body("statusId",equalTo(1))
                .and().body("bookStatus.statusId",equalTo(1))
                .and().body("bookStatus.statusName",equalTo("In Progress"))
                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("lastChapter",equalTo(4))
                .and().body("authors.findAll{i -> i.authorId == 6 && i.readListId == 2 && i.name == 'Author'}", not(empty()))
                .and().body("$",not(hasKey("series")))
        ;

        Mockito.verify(userService).checkOwnership("Test",2L);
        Mockito.verify(readListService).getBook(2L,3L);
        Mockito.verify(authorsBooksService).getByBookId(3L);
    }

    @Test
    void shouldGetOneNoChapter() throws Exception {
        Date dt = new Date();
        Book book = new Book.Builder()
                .readListId(2L)
                .bookId(3L)
                .title("Title")
                .statusId(1)
                .bookStatus(BookStatus.IN_PROGRESS)
                .insertDate(dt)
                .lastUpdateDate(dt)
                .seriesId(5L)
                .authorId(6L)
                .seriesOrder(7L)
                .build();
        Author author = new Author(6L,2L,"Author");
        Series series = new Series(5L,2L,"Series");

        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
                .thenReturn(book);
        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(series));
        Mockito.when(authorsBooksService.getByBookId(Mockito.anyLong()))
                .thenReturn(List.of(new AuthorBookRelation(book,author)));


        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/3")
                .then().statusCode(200)
                .and().body("readListId",equalTo(2))
                .and().body("bookId",equalTo(3))
                .and().body("title",equalTo("Title"))
                .and().body("statusId",equalTo(1))
                .and().body("bookStatus.statusId",equalTo(1))
                .and().body("bookStatus.statusName",equalTo("In Progress"))
                .and().body("insertDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("lastUpdateDate",equalTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt)))
                .and().body("seriesId",equalTo(5))
                .and().body("authorId",equalTo(6))
                .and().body("seriesOrder",equalTo(7))
                .and().body("series.seriesId",equalTo(5))
                .and().body("series.seriesListId",equalTo(2))
                .and().body("series.title",equalTo("Series"))
                .and().body("authors.findAll{i -> i.authorId == 6 && i.readListId == 2 && i.name == 'Author'}", not(empty()))
//                .and().body("series.findAll{i -> i.seriesId == 5 && i.seriesListId == 2 && i.title == 'Series'}", not(empty()))
                .and().body("$",not(hasKey("lastChapter")))
        ;

        Mockito.verify(userService).checkOwnership("Test",2L);
        Mockito.verify(readListService).getBook(2L,3L);
        Mockito.verify(bookSeriesService).getSeries(2L,5L);
        Mockito.verify(authorsBooksService).getByBookId(3L);
    }

    @Test
    void shouldNoBook() throws Exception {
        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
                .thenReturn(null);


        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/3")
                .then().statusCode(404)
                .and().body("error",startsWith("ru.rerumu.lists.exception.EntityNotFoundException"))
                .and().body("errorMessage",equalTo(null));
        ;
    }

    @Test
    void shouldThrowException(){
        Mockito.when(readListService.getBook(Mockito.anyLong(),Mockito.anyLong()))
                .thenThrow(IllegalArgumentException.class);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/1")
                .then().statusCode(500)
                .and().body("error",equalTo("java.lang.IllegalArgumentException"))
                .and().body("errorMessage",equalTo(null));

    }

}