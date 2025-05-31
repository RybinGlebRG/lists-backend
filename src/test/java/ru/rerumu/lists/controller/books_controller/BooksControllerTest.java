package ru.rerumu.lists.controller.books_controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.book.BooksController;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.services.author.AuthorsService;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.series.impl.SeriesServiceImpl;
import ru.rerumu.lists.services.user.UserService;

@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerTest {

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
    @Disabled
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