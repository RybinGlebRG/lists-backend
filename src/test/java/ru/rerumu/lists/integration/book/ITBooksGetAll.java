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

import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Slf4j
public class ITBooksGetAll extends ITBase {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        log.info("jdbcUrl: {}", postgres.getJdbcUrl());
        log.info("username: {}", postgres.getUsername());
        log.info("password: {}", postgres.getPassword());

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
    public void shouldGetAll(TestInfo testInfo) throws Exception {

        SeriesView seriesView = addSeries("TestSeries 1");

        BookView bookView1 = addBook("TestBook 1", null, null);
        BookView bookView2 = addBook("TestBook 2", seriesView.seriesId(), null);
        BookView bookView3 = addBook("TestBook 3", seriesView.seriesId(), null);
        BookView bookView4 = addBook("TestBook 4", null, null);

        String responseBody = RestAssuredMockMvc
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
                    .extract()
                    .body()
                    .asString();
        log.info("responseBody: {}", responseBody);

        String expectedResponseBodyWithoutDates = String.format(
                """
                {
                    "items": [{
                            "bookId": %d,
                            "readListId": null,
                            "title": "TestBook 4",
                            "bookStatus": {
                                "statusId": 1,
                                "statusName": "In progress"
                            },
                            "lastChapter": null,
                            "note": "test note",
                            "bookType": null,
                            "itemType": "BOOK",
                            "chain": [],
                            "readingRecords": [{
                                    "recordId": %d,
                                    "bookId": %d,
                                    "bookStatus": {
                                        "statusId": 1,
                                        "statusName": "In progress"
                                    },
                                    "endDate": null,
                                    "isMigrated": false,
                                    "lastChapter": 123
                                }
                            ],
                            "tags": [],
                            "textAuthors": [],
                            "seriesList": [],
                            "url": null
                        }, {
                            "bookId": %d,
                            "readListId": null,
                            "title": "TestBook 3",
                            "bookStatus": {
                                "statusId": 1,
                                "statusName": "In progress"
                            },
                            "lastChapter": null,
                            "note": "test note",
                            "bookType": null,
                            "itemType": "BOOK",
                            "chain": [{
                                    "bookId": %d,
                                    "readListId": null,
                                    "title": "TestBook 2",
                                    "bookStatus": {
                                        "statusId": 1,
                                        "statusName": "In progress"
                                    },
                                    "lastChapter": null,
                                    "note": "test note",
                                    "bookType": null,
                                    "itemType": "BOOK",
                                    "chain": [],
                                    "readingRecords": [{
                                            "recordId": %d,
                                            "bookId": %d,
                                            "bookStatus": {
                                                "statusId": 1,
                                                "statusName": "In progress"
                                            },
                                            "endDate": null,
                                            "isMigrated": false,
                                            "lastChapter": 123
                                        }
                                    ],
                                    "tags": [],
                                    "textAuthors": [],
                                    "seriesList": [
                                        {
                                            "seriesId": %d,
                                            "title": "TestSeries 1"
                                        }
                                    ],
                                    "url": null
                                }
                            ],
                            "readingRecords": [{
                                    "recordId": %d,
                                    "bookId": %d,
                                    "bookStatus": {
                                        "statusId": 1,
                                        "statusName": "In progress"
                                    },
                                    "endDate": null,
                                    "isMigrated": false,
                                    "lastChapter": 123
                                }
                            ],
                            "tags": [],
                            "textAuthors": [],
                            "seriesList": [{
                                    "seriesId": %d,
                                    "title": "TestSeries 1"
                                }
                            ],
                            "url": null
                        }, {
                            "bookId": %d,
                            "readListId": null,
                            "title": "TestBook 1",
                            "bookStatus": {
                                "statusId": 1,
                                "statusName": "In progress"
                            },
                            "lastChapter": null,
                            "note": "test note",
                            "bookType": null,
                            "itemType": "BOOK",
                            "chain": [],
                            "readingRecords": [{
                                    "recordId": %d,
                                    "bookId": %d,
                                    "bookStatus": {
                                        "statusId": 1,
                                        "statusName": "In progress"
                                    },
                                    "endDate": null,
                                    "isMigrated": false,
                                    "lastChapter": 123
                                }
                            ],
                            "tags": [],
                            "textAuthors": [],
                            "seriesList": [],
                            "url": null
                        }
                    ]
                }
                """,
                bookView4.getBookId(),
                bookView4.getReadingRecords().get(0).getRecordId(),
                bookView4.getBookId(),

                bookView3.getBookId(),
                bookView2.getBookId(),
                bookView2.getReadingRecords().get(0).getRecordId(),
                bookView2.getBookId(),
                seriesView.seriesId(),
                bookView3.getReadingRecords().get(0).getRecordId(),
                bookView3.getBookId(),
                seriesView.seriesId(),

                bookView1.getBookId(),
                bookView1.getReadingRecords().get(0).getRecordId(),
                bookView1.getBookId()
        );
        JSONAssert.assertEquals(
                "Incorrect response",
                expectedResponseBodyWithoutDates,
                responseBody,
                false
        );
    }

    /**
     * Only one book in series
     */
    @Test
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldGetSingleInSeries() throws Exception {

        addSeries("TestSeries 1");

        SeriesView seriesView = getSeriesByTitle("TestSeries 1");
        Objects.requireNonNull(seriesView);

        addBook("TestBook 1", seriesView.seriesId(), null);

        RestAssuredMockMvc
                .given()
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
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
                            """
                    )
                    .log().all()
                .when()
                    .post("/api/v1/users/{userId}/books/search", "0")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("items", hasSize(1));
    }
}
