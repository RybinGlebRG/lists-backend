package ru.rerumu.lists.integration.series;

import com.jcabi.aspects.Loggable;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.series.views.out.SeriesListView;
import ru.rerumu.lists.integration.ITBase;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class ITSeriesAdd extends ITBase {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        log.info("jdbcUrl: {}", postgres.getJdbcUrl());

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public static void beforeAll() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @BeforeEach
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    void beforeEach() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        cleanSQL();
    }

    @Test
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public void shouldAdd(TestInfo testInfo) throws Exception{

        SeriesListView seriesListView = getSeriesList();

        String responseBody = RestAssuredMockMvc
                .given()
                    .body("""
                            {
                                "title": "TestSeries"
                            }
                            """)
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .post("/api/v1/users/0/series")
                .then()
                    .statusCode(200)
                    .body(JsonSchemaValidator.matchesJsonSchema(
                            """
                            {
                              "$schema": "http://json-schema.org/draft-04/schema#",
                              "type": "object",
                              "properties": {
                                "seriesId": {
                                  "type": "integer"
                                },
                                "userId": {
                                  "type": "integer"
                                },
                                "title": {
                                  "type": "string"
                                },
                                "items": {
                                    "type": "array",
                                    "items": []
                                }
                              },
                              "additionalProperties": false,
                              "required": [
                                "seriesId",
                                "userId",
                                "title",
                                "items"
                              ]
                            }
                            """
                    ))
                    .body("userId", equalTo(0L))
                    .body("title", equalTo("TestSeries"))
                    .extract().body().asString();
        log.info("responseBody: {}", responseBody);
    }

}
