package ru.rerumu.lists.controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.services.ReadListService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AuthorsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorsControllerTest {

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
    void shouldGetAll(){
        Mockito.when(readListService.getAuthors(2L))
                .thenReturn(List.of(
                   new Author(1L,2L,"Test1"),
                   new Author(2L,2L,"Test2"),
                   new Author(3L,2L,"Test3")
                ));

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/authors")
                .then().statusCode(200)
                .body("items.size()",is(3))
                .and().body("items[0].authorId",equalTo(1))
                .and().body("items[1].authorId",equalTo(2))
                .and().body("items[2].authorId",equalTo(3));


    }

//    @Test
//    void shouldGetOne(){
//        Mockito.when(readListService.getBook(2L,1L))
//                .thenReturn(
//                        new Book(
//                                1L,
//                                2L,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null
//                        )
//                );
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/authors/1")
//                .then().statusCode(200)
//                .and().body("bookId",equalTo(1))
//                .and().body("readListId",equalTo(2));
//    }

}