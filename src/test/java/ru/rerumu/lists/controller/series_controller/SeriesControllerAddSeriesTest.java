package ru.rerumu.lists.controller.series_controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.SeriesController;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.BookSeriesAddView;

import static org.mockito.Mockito.*;

@WebMvcTest(SeriesController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeriesControllerAddSeriesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean
    private SeriesService seriesService;

    @MockBean(name="UserService")
    private UserService userService1;

    @MockBean(name="UserServiceProtectionProxy")
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
    void shouldAdd() throws Exception{

        BookSeriesAddView bookSeriesAddView = new BookSeriesAddView("Series");

        JSONObject requestBody = new JSONObject();
        requestBody.put("title","Series");

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/series")
                .then().statusCode(204);

        verify(seriesService).add(2,bookSeriesAddView);

    }

}