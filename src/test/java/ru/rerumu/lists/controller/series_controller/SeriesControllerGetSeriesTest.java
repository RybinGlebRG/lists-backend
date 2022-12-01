package ru.rerumu.lists.controller.series_controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.SeriesController;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.services.*;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(SeriesController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeriesControllerGetSeriesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean
    private SeriesService seriesService;

    @MockBean
    private UserService userService;

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
    void shouldGetSeriesOne() throws Exception{
        Date dt = new Date();
        Book book = new Book.Builder()
                .readListId(2L)
                .bookId(88L)
                .title("Title")
                .bookStatus(BookStatus.IN_PROGRESS)
                .insertDate(dt)
                .lastUpdateDate(dt)
                .lastChapter(4)
                .build();
        Series series = new Series(3L,2L,"Test");
        SeriesBookRelation seriesBookRelation = new SeriesBookRelation(book,series,100L);

        when(readListService.getSeries(anyLong(),anyLong())).thenReturn(series);
        when(bookSeriesRelationService.getBySeriesId(anyLong())).thenReturn(List.of(seriesBookRelation));

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/series/3")
                .then().statusCode(200)
                .and().body("seriesId",equalTo(3))
                .and().body("readListId",equalTo(2))
                .and().body("title",equalTo("Test"))
                .and().body("books.findAll{i -> i.bookId == 88}", not(empty()))
                .and().body("$",not(hasKey("bookCount")));

    }

}