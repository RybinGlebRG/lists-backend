package ru.rerumu.lists.controller.books_controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.BooksController;
import ru.rerumu.lists.services.*;

import java.util.AbstractMap;
import java.util.Map;

@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerSearchBookTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean
    private UserServiceImpl userService;

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
    void shouldSearch()throws Exception{
        JSONObject requestBody = new JSONObject();
        JSONArray array = new JSONArray();

        array.put(new JSONObject(
                Map.ofEntries(
                        new AbstractMap.SimpleEntry<String,String>("field","createDate"),
                        new AbstractMap.SimpleEntry<String,String>("ordering","DESC")
                )

        ));
        requestBody.put("sort",array);

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/books/search")
                .then().statusCode(200);
    }

}