package ru.rerumu.lists.integration.book;

import com.jcabi.aspects.Loggable;
import io.restassured.RestAssured;
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
import ru.rerumu.lists.controller.book.view.out.BookView;
import ru.rerumu.lists.controller.series.views.out.SeriesView;
import ru.rerumu.lists.integration.ITBase;
import ru.rerumu.lists.integration.TestCommon;

import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class ITBookUpdateParentInChain extends ITBase {

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
    public void shouldUpdateBook(TestInfo testInfo) throws Exception{

        TestCommon.addSeries("TestSeries");
        SeriesView seriesView = getSeriesByTitle("TestSeries");
        Objects.requireNonNull(seriesView);

        TestCommon.addSeries("TestSeries 2");
        TestCommon.addBook("TestBook 1", 1L, null);
        BookView bookView = getBookByTitle("TestBook 1");
        Objects.requireNonNull(bookView);


        String searchResponseBody = RestAssuredMockMvc
                .given()
                    .body("""
                            {
                                "sort": [
                                    {
                                        "field": "createDate",
                                        "ordering": "DESC"
                                    }
                                ],
                                "isChainBySeries": true,
                                "filters": [
                                    {
                                        "field": "bookStatusIds",
                                        "values": ["1", "2", "3", "4"]
                                    }
                                ]
                            }
                            """)
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .post("/api/v1/users/0/books/search")
                .then()
                    .statusCode(200)
                .extract().body().asString();
        log.info("searchResponseBody: {}", searchResponseBody);

        String requestBody = String.format(
                """
                {
                    "title": "TestBook 1",
                    "authorId": null,
                    "status": 1,
                    "seriesIds": [%d],
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
                """,
                seriesView.seriesId()
        );

        String responseBody = RestAssuredMockMvc
                .given()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .put("/api/v1/users/0/books/{bookId}", bookView.getBookId().toString())
                .then()
                    .statusCode(200)
                .extract().body().asString();
        log.info("responseBody: {}", responseBody);


        String expectedResponseBodyWithoutDates = String.format(
                """
                {
                    "bookId": %d,
                    "readListId": null,
                    "title": "TestBook 1",
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
                            "bookId": %d,
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
                            "seriesId": %d,
                            "title": "TestSeries"
                        }
                    ],
                    "url": null
                }
                """,
                bookView.getBookId(),
                bookView.getBookId(),
                seriesView.seriesId()
        );

        JSONAssert.assertEquals(
                "Incorrect response",
                expectedResponseBodyWithoutDates,
                responseBody,
                false
        );



    }
}