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

import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Slf4j
class ITBookUpdateRemoveSeries {

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
        TestCommon.addSeries("TestSeries 2");
        TestCommon.addBook("TestBook", null, null);

        /*
        Add series
         */
        String requestBody = """
                {
                    "title": "TestBook",
                    "authorId": null,
                    "status": 1,
                    "seriesIds": [1, 2],
                    "order": null,
                    "lastChapter": null,
                    "bookTypeId": 1,
                    "insertDateUTC": "2025-08-27T05:12:00.000Z",
                    "note": "123123",
                    "URL": null,
                    "readingRecords": [
                        {
                            "readingRecordId": 0,
                            "statusId": 1,
                            "startDate": "2025-08-27T17:12:00",
                            "endDate": null,
                            "lastChapter": null
                        }
                    ],
                    "tagIds": []
                }
                """;

        String responseBody = RestAssuredMockMvc
                .given()
                .body(requestBody)
                .header("Content-Type", "application/json")
                .attribute("authUserId", 0L)
                .when()
                .put("/api/v1/users/0/books/0")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        log.info("responseBody: {}", responseBody);


        String expectedResponseBodyWithoutDates = """
                {
                    "bookId": 0,
                    "readListId": null,
                    "title": "TestBook",
                    "bookStatus": {
                        "statusId": 1,
                        "statusName": "In progress"
                    },
                    "lastChapter": null,
                    "note": "123123",
                    "bookType": {
                        "typeId": 1,
                        "typeName": "Book"
                    },
                    "itemType": "BOOK",
                    "chain": [],
                    "readingRecords": [
                        {
                            "recordId": 0,
                            "bookId": 0,
                            "bookStatus": {
                                "statusId": 1,
                                "statusName": "In progress"
                            },
                            "endDate": null,
                            "isMigrated": false,
                            "lastChapter": null
                        }
                    ],
                    "tags": [],
                    "textAuthors": [],
                    "seriesList": [
                        {
                            "seriesId": 1,
                            "title": "TestSeries"
                        },
                        {
                            "seriesId": 2,
                            "title": "TestSeries 2"
                        }
                    ],
                    "url": null
                }
                """;

        JSONAssert.assertEquals(
                "Incorrect response",
                expectedResponseBodyWithoutDates,
                responseBody,
                false
        );


        /*
        Remove series
         */
        requestBody = """
                {
                    "title": "TestBook",
                    "authorId": null,
                    "status": 1,
                    "seriesIds": [2],
                    "order": null,
                    "lastChapter": null,
                    "bookTypeId": 1,
                    "insertDateUTC": "2025-08-27T05:12:00.000Z",
                    "note": "123123",
                    "URL": null,
                    "readingRecords": [
                        {
                            "readingRecordId": 0,
                            "statusId": 1,
                            "startDate": "2025-08-27T17:12:00",
                            "endDate": null,
                            "lastChapter": null
                        }
                    ],
                    "tagIds": []
                }
                """;

        responseBody = RestAssuredMockMvc
                .given()
                .body(requestBody)
                .header("Content-Type", "application/json")
                .attribute("authUserId", 0L)
                .when()
                .put("/api/v1/users/0/books/0")
                .then()
                .statusCode(200)
                .body("seriesList.size()", is(1))
                .extract()
                .body()
                .asString();
        log.info("responseBody: {}", responseBody);


        expectedResponseBodyWithoutDates = """
                {
                    "bookId": 0,
                    "readListId": null,
                    "title": "TestBook",
                    "bookStatus": {
                        "statusId": 1,
                        "statusName": "In progress"
                    },
                    "lastChapter": null,
                    "note": "123123",
                    "bookType": {
                        "typeId": 1,
                        "typeName": "Book"
                    },
                    "itemType": "BOOK",
                    "chain": [],
                    "readingRecords": [
                        {
                            "recordId": 0,
                            "bookId": 0,
                            "bookStatus": {
                                "statusId": 1,
                                "statusName": "In progress"
                            },
                            "endDate": null,
                            "isMigrated": false,
                            "lastChapter": null
                        }
                    ],
                    "tags": [],
                    "textAuthors": [],
                    "seriesList": [
                        {
                            "seriesId": 2,
                            "title": "TestSeries 2"
                        }
                    ],
                    "url": null
                }
                """;

        JSONAssert.assertEquals(
                "Incorrect response",
                expectedResponseBodyWithoutDates,
                responseBody,
                false
        );

    }
}