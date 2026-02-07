package ru.rerumu.lists.integration.book;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.rerumu.lists.integration.TestCommon;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Slf4j
class ITBookUpdateChapter {

    private static PostgreSQLContainer<?> postgres;

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
    public static void beforeAll() {
        log.info("beforeAll");

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

        postgres = new PostgreSQLContainer<>(
                "postgres:16-alpine"
        );
        postgres.start();
    }

    @BeforeEach
    void beforeEach() {
        log.info("beforeEach");

        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @AfterAll
    public static void afterAll() {
        log.info("afterAll");

        postgres.stop();
    }

    @Test
    public void shouldUpdateBook(TestInfo testInfo) throws Exception{
        log.info("Test: {}", testInfo.getDisplayName());


        TestCommon.addSeries("TestSeries");
        TestCommon.addAuthor("TestAuthor");
        TestCommon.addBook("TestBook1", 1L, 0L);
        TestCommon.addBook("TestBook2", 1L, 0L);

        RestAssuredMockMvc
                .given()
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                    .body("""
                        {
                            "title": "TestBook2",
                            "authorId": 0,
                            "status": 1,
                            "seriesIds": [1],
                            "order": null,
                            "lastChapter": null,
                            "bookTypeId": 1,
                            "insertDateUTC": "2025-08-27T05:12:00.000Z",
                            "note": "123123",
                            "URL": null,
                            "readingRecords": [
                                {
                                    "readingRecordId": 1,
                                    "statusId": 1,
                                    "startDate": "2025-08-27T17:12:00",
                                    "endDate": null,
                                    "lastChapter": 123
                                }
                            ],
                            "tagIds": []
                        }
                        """
                    )
                    .log().all()
                .when()
                    .put("/api/v1/users/{userId}/books/{bookId}", "0", "1")
                .then()
                    .log().all()
                    .statusCode(200);

        log.info("Getting book");
        RestAssuredMockMvc
                .given()
                    .attribute("authUserId", 0L)
                    .log().all()
                .when()
                    .get("/api/v1/users/{userId}/books/{bookId}", "0", "1")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("seriesList", hasSize(1));

    }
}