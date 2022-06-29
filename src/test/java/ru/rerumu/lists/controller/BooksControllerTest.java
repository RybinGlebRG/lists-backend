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
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.services.ReadListService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

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
    void shouldGetOne(){
        Mockito.when(readListService.getBook(2L,1L))
                        .thenReturn(
                                new Book(
                                        1L,
                                        2L,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                        )
                        );

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/books/1")
                .then().statusCode(200)
                .and().body("bookId",equalTo(1))
                .and().body("readListId",equalTo(2));
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

}