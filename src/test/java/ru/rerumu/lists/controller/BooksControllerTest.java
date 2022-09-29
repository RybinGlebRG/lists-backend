package ru.rerumu.lists.controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.services.AuthorsService;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.AddAuthorView;
import ru.rerumu.lists.views.BookAddView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorsService authorsService;

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
    void shouldGetOne() throws EmptyMandatoryParameterException {
        Date dt = new Date();
        Mockito.when(readListService.getBook(2L,1L))
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
                                        .authorId(6L)
                                        .seriesOrder(7L)
                                        .build()
                        );

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/1")
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
                .and().body("authorId",equalTo(6))
                .and().body("seriesOrder",equalTo(7))
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

    @Test
    void shouldGetBooks(){
//        Mockito.when(readListService.getAllBooks(Mockito.anyLong()))
//                        .thenReturn(List.of(
//                           new Book()
//                        ));

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books")
                .then().statusCode(200);
    }


    @Test
    void shouldAdd() throws Exception{
        Date dt = new Date();
        Mockito.when(readListService.addBook(Mockito.anyLong(), Mockito.any()))
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
                                        .authorId(6L)
                                        .seriesOrder(7L)
                                        .build()
                        );

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
                .and().body("authorId",equalTo(6))
                .and().body("seriesOrder",equalTo(7))
        ;

        Mockito.verify(readListService).addBook(
                2L,
                new BookAddView("Title",6L,1,5L,7L, 4));
    }

}