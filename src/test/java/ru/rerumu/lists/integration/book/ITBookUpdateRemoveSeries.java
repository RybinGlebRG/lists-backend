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

import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Slf4j
class ITBookUpdateRemoveSeries extends ITBase {

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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldUpdateBook(TestInfo testInfo) throws Exception{

        SeriesView seriesView = addSeries("TestSeries");
        SeriesView seriesView2 = addSeries("TestSeries 2");
        BookView bookView = addBook("TestBook", null, null);

        /*
        Add series
         */
        String responseBody = RestAssuredMockMvc
                .given()
                    .body(String.format(
                            """
                            {
                                "title": "TestBook",
                                "authorId": null,
                                "status": 1,
                                "seriesIds": [%d, %d],
                                "order": null,
                                "lastChapter": null,
                                "bookTypeId": 1,
                                "insertDateUTC": "2025-08-27T05:12:00.000Z",
                                "note": "123123",
                                "URL": null,
                                "readingRecords": [
                                    {
                                        "readingRecordId": %d,
                                        "statusId": 1,
                                        "startDate": "2025-08-27T17:12:00",
                                        "endDate": null,
                                        "lastChapter": null
                                    }
                                ],
                                "tagIds": []
                            }
                            """,
                            seriesView.seriesId(),
                            seriesView2.seriesId(),
                            bookView.getReadingRecords().get(0).getRecordId()
                    ))
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .put("/api/v1/users/0/books/{bookId}", bookView.getBookId().toString())
                .then()
                    .statusCode(200)
                    .extract().body().asString();
        log.info("responseBody: {}", responseBody);


        JSONAssert.assertEquals(
                "Incorrect response",
                String.format(
                        """
                        {
                            "bookId": %d,
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
                                    "recordId": %d,
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
                                },
                                {
                                    "seriesId": %d,
                                    "title": "TestSeries 2"
                                }
                            ],
                            "url": null
                        }
                        """,
                        bookView.getBookId(),
                        bookView.getReadingRecords().get(0).getRecordId(),
                        bookView.getBookId(),
                        seriesView.seriesId(),
                        seriesView2.seriesId()
                ),
                responseBody,
                false
        );


        /*
        Remove series
         */
        responseBody = RestAssuredMockMvc
                .given()
                    .body(String.format(
                            """
                            {
                                "title": "TestBook",
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
                                        "readingRecordId": %d,
                                        "statusId": 1,
                                        "startDate": "2025-08-27T17:12:00",
                                        "endDate": null,
                                        "lastChapter": null
                                    }
                                ],
                                "tagIds": []
                            }
                            """,
                            seriesView2.seriesId(),
                            bookView.getReadingRecords().get(0).getRecordId()
                    ))
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .put("/api/v1/users/0/books/{bookId}", bookView.getBookId().toString())
                .then()
                    .statusCode(200)
                    .body("seriesList.size()", is(1))
                    .extract().body().asString();
        log.info("responseBody: {}", responseBody);


        JSONAssert.assertEquals(
                "Incorrect response",
                String.format(
                        """
                        {
                            "bookId": %d,
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
                                    "recordId": %d,
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
                                    "title": "TestSeries 2"
                                }
                            ],
                            "url": null
                        }
                        """,
                        bookView.getBookId(),
                        bookView.getReadingRecords().get(0).getRecordId(),
                        bookView.getBookId(),
                        seriesView2.seriesId()
                ),
                responseBody,
                false
        );

    }
}