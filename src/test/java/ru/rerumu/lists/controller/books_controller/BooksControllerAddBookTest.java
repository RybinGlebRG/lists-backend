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
import ru.rerumu.lists.controller.BooksController;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.BookAddView;

@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerAddBookTest {

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
    private SeriesService seriesService;

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
    void shouldAdd() throws Exception{
        JSONObject requestBody = new JSONObject();
        requestBody.put("title","Title");
        requestBody.put("authorId",6);
        requestBody.put("status",1);
        requestBody.put("seriesId",5);
        requestBody.put("order",7);
        requestBody.put("lastChapter",4);
        requestBody.put("bookTypeId",3);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/books")
                .then().statusCode(204);

        Mockito.verify(readListService).addBook(
                2L,
                new BookAddView("Title",6L,1,5L,7L, 4,3));
    }

    @Test
    void shouldAddSimplest() throws Exception{
        JSONObject requestBody = new JSONObject();
        requestBody.put("title","Title");
        requestBody.put("status",1);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/books")
                .then().statusCode(204);

        Mockito.verify(readListService).addBook(
                2L,
                new BookAddView("Title",null,1,null,null, null,null));
    }

    @Test
    void shouldAddNoAuthor() throws Exception{
        JSONObject requestBody = new JSONObject();
        requestBody.put("title","Title");
        requestBody.put("status",1);
        requestBody.put("seriesId",5);
        requestBody.put("order",7);
        requestBody.put("lastChapter",4);
        requestBody.put("bookTypeId",3);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/books")
                .then().statusCode(204);

        Mockito.verify(readListService).addBook(
                2L,
                new BookAddView("Title",null,1,5L,7L, 4,3));
    }

    @Test
    void shouldAddNoSeries() throws Exception{
        JSONObject requestBody = new JSONObject();
        requestBody.put("title","Title");
        requestBody.put("authorId",6);
        requestBody.put("status",1);
        requestBody.put("lastChapter",4);
        requestBody.put("bookTypeId",3);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/books")
                .then().statusCode(204);

        Mockito.verify(readListService).addBook(
                2L,
                new BookAddView("Title",6L,1,null,null, 4,3));
    }

    @Test
    void shouldAddNoChapter() throws Exception{
        JSONObject requestBody = new JSONObject();
        requestBody.put("title","Title");
        requestBody.put("authorId",6);
        requestBody.put("status",1);
        requestBody.put("seriesId",5);
        requestBody.put("order",7);
        requestBody.put("bookTypeId",3);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/books")
                .then().statusCode(204);

        Mockito.verify(readListService).addBook(
                2L,
                new BookAddView("Title",6L,1,5L,7L, null,3));
    }

    @Test
    void shouldAddNoType() throws Exception{
        JSONObject requestBody = new JSONObject();
        requestBody.put("title","Title");
        requestBody.put("authorId",6);
        requestBody.put("status",1);
        requestBody.put("seriesId",5);
        requestBody.put("order",7);
        requestBody.put("lastChapter",4);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/books")
                .then().statusCode(204);

        Mockito.verify(readListService).addBook(
                2L,
                new BookAddView("Title",6L,1,5L,7L, 4,null));
    }

}