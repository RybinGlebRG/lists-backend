package ru.rerumu.lists.controller.books_controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.book.BooksController;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.services.author.AuthorsService;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.series.impl.SeriesServiceImpl;
import ru.rerumu.lists.services.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerUpdateBookTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean(name="UserService")
    private UserService userService1;

    @MockBean(name="UserServiceProtectionProxy")
    private UserService userService;

    @MockBean
    private AuthorsService authorsService;

    @MockBean
    private SeriesServiceImpl seriesService;

    @MockBean
    private AuthorsBooksRelationService authorsBooksRelationService;

    @MockBean
    private BookSeriesRelationService bookSeriesRelationService;

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
    void shouldUpdate() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("bookId", 2);
        requestBody.put("readListId", 3);
        requestBody.put("title", "Title");
        requestBody.put("authorId", 6);
        requestBody.put("status", 1);
        requestBody.put("seriesId", 5);
        requestBody.put("order", 7);
        requestBody.put("lastChapter", 4);
        requestBody.put("insertDateUTC", "2022-10-01T00:00:00");
        requestBody.put("bookTypeId",2);
        requestBody.put("note","test note 2");

        RestAssuredMockMvc
                .given()
                .attribute("username", "Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .put("/api/v0.2/books/2")
                .then().statusCode(204);

        Mockito.verify(readListService).updateBook(
                2L,
                new BookUpdateView(
                        3L,
                        "Title",
                        6L,
                        5L,
                        7L,
                        4,
                        LocalDateTime.of(2022, 10, 1, 0, 0, 0),
                        2,
                        "test note 2",
                        null,
                        new ArrayList<>(),
                        new ArrayList<>()
                )
        );
    }

//    @Test
//    void shouldUpdateNoChapter() throws Exception {
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("bookId", 2);
//        requestBody.put("readListId", 3);
//        requestBody.put("title", "Title");
//        requestBody.put("authorId", 6);
//        requestBody.put("status", 1);
//        requestBody.put("seriesId", 5);
//        requestBody.put("order", 7);
//        requestBody.put("insertDateUTC", "2022-10-01T00:00:00");
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username", "Test")
//                .header("Content-Type", "application/json")
//                .body(requestBody.toString())
//                .when()
//                .put("/api/v0.2/books/2")
//                .then().statusCode(204);
//
//        Mockito.verify(readListService).updateBook(
//                2L,
//                new BookUpdateView(
//                        3L,
//                        "Title",
//                        6L,
//                        1,
//                        5L,
//                        7L,
//                        null,
//                        LocalDateTime.of(2020, 10, 1, 0, 0, 0),
//                        null
//                )
//        );
//    }
//
//    @Test
//    void shouldUpdateNoAuthor() throws Exception {
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("bookId", 2);
//        requestBody.put("readListId", 3);
//        requestBody.put("title", "Title");
//        requestBody.put("status", 1);
//        requestBody.put("seriesId", 5);
//        requestBody.put("order", 7);
//        requestBody.put("lastChapter", 4);
//        requestBody.put("insertDateUTC", "2022-10-01T00:00:00");
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username", "Test")
//                .header("Content-Type", "application/json")
//                .body(requestBody.toString())
//                .when()
//                .put("/api/v0.2/books/2")
//                .then().statusCode(204);
//
//        Mockito.verify(readListService).updateBook(
//                2L,
//                new BookUpdateView(
//                        3L,
//                        "Title",
//                        null,
//                        1,
//                        5L,
//                        7L,
//                        4,
//                        LocalDateTime.of(2022, 10, 1, 0, 0, 0),
//                        null
//                )
//        );
//    }
//
//    @Test
//    void shouldUpdateNoSeries() throws Exception {
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("bookId", 2);
//        requestBody.put("readListId", 3);
//        requestBody.put("title", "Title");
//        requestBody.put("authorId", 6);
//        requestBody.put("status", 1);
//        requestBody.put("lastChapter", 4);
//        requestBody.put("insertDateUTC", "2022-10-01T00:00:00");
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username", "Test")
//                .header("Content-Type", "application/json")
//                .body(requestBody.toString())
//                .when()
//                .put("/api/v0.2/books/2")
//                .then().statusCode(204);
//
//        Mockito.verify(readListService).updateBook(
//                2L,
//                new BookUpdateView(
//                        3L,
//                        "Title",
//                        6L,
//                        1,
//                        null,
//                        null,
//                        4,
//                        LocalDateTime.of(2022, 10, 1, 0, 0, 0),
//                        null
//                )
//        );
//    }

//    @Test
//    void shouldUpdateType() throws Exception {
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("bookId", 2);
//        requestBody.put("readListId", 3);
//        requestBody.put("title", "Title");
//        requestBody.put("bookType",2);
//        requestBody.put("status", 1);
//        requestBody.put("insertDateUTC", "2022-10-01T00:00:00");
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username", "Test")
//                .header("Content-Type", "application/json")
//                .body(requestBody.toString())
//                .when()
//                .put("/api/v0.2/books/2")
//                .then().statusCode(204);
//
//        Mockito.verify(readListService).updateBook(
//                2L,
//                new BookUpdateView(
//                        3L,
//                        "Title",
//                        null,
//                        1,
//                        null,
//                        null,
//                        null,
//                        LocalDateTime.of(2022, 10, 1, 0, 0, 0),
//                        2
//                )
//        );
//    }

}