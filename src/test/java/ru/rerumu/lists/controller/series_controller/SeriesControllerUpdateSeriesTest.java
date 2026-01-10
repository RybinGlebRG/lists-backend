package ru.rerumu.lists.controller.series_controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.series.SeriesController;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.series.impl.SeriesServiceImpl;
import ru.rerumu.lists.services.user.UserService;

import static org.mockito.Mockito.any;

@WebMvcTest(SeriesController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeriesControllerUpdateSeriesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean
    private SeriesServiceImpl seriesService;

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

//    @Test
//    void shouldUpdateSeries() throws Exception{
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("title","Series");
//        JSONArray array = new JSONArray();
//        var tmp = new JSONObject();
//        tmp.put("itemType","BOOK");
//        tmp.put("itemId",5);
//        tmp.put("itemOrder",3);
//        array.put(tmp);
//        requestBody.put("items",array);
//
//        List<SeriesUpdateItem> seriesUpdateItemList = new ArrayList<>();
//        seriesUpdateItemList.add(new SeriesUpdateItem(SeriesItemType.BOOK,5L,3L));
//        SeriesUpdateView seriesUpdateView = new SeriesUpdateView("Series",seriesUpdateItemList);
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .header("Content-Type", "application/json")
//                .body(requestBody.toString())
//                .when()
//                .put("/api/v0.2/series/3")
//                .then().statusCode(204);
//
//        verify(seriesService).updateSeries(3L,seriesUpdateView);
//    }
}