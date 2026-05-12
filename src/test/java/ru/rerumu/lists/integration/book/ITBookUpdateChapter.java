package ru.rerumu.lists.integration.book;

import com.jcabi.aspects.Loggable;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
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

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class ITBookUpdateChapter extends ITBase {

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

        TestCommon.addAuthor("TestAuthor");
        TestCommon.addBook("TestBook1", seriesView.seriesId(), 0L);

        TestCommon.addBook("TestBook2", seriesView.seriesId(), 0L);
        BookView bookView = getBookByTitle("TestBook2");
        Objects.requireNonNull(bookView);

        RestAssuredMockMvc
                .given()
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                    .body(
                            String.format(
                                """
                                {
                                    "title": "TestBook2",
                                    "authorId": 0,
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
                                            "readingRecordId": 1,
                                            "statusId": 1,
                                            "startDate": "2025-08-27T17:12:00",
                                            "endDate": null,
                                            "lastChapter": 123
                                        }
                                    ],
                                    "tagIds": []
                                }
                                """,
                                seriesView.seriesId()
                            )
                    )
                    .log().all()
                .when()
                    .put("/api/v1/users/{userId}/books/{bookId}", "0", bookView.getBookId().toString())
                .then()
                    .log().all()
                    .statusCode(200);

        log.info("Getting book");
        RestAssuredMockMvc
                .given()
                    .attribute("authUserId", 0L)
                    .log().all()
                .when()
                    .get("/api/v1/users/{userId}/books/{bookId}", "0", bookView.getBookId().toString())
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("seriesList", hasSize(1));

    }
}