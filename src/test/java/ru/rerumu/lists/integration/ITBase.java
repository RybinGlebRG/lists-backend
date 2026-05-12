package ru.rerumu.lists.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.aspects.Loggable;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.postgresql.PostgreSQLContainer;
import ru.rerumu.lists.controller.book.view.out.BookListView;
import ru.rerumu.lists.controller.book.view.out.BookView;
import ru.rerumu.lists.controller.series.views.out.SeriesListView;
import ru.rerumu.lists.controller.series.views.out.SeriesView;
import ru.rerumu.lists.crosscut.Profiles;

import java.util.Objects;


@ActiveProfiles(Profiles.TEST)
@Slf4j
public class ITBase {

    protected static PostgreSQLContainer postgres;
    private static JdbcDatabaseDelegate databaseDelegate;

    static {
        postgres = new PostgreSQLContainer("postgres:16-alpine");
        postgres.start();
        databaseDelegate = new JdbcDatabaseDelegate(postgres, "");
    }

    @Autowired
    private ObjectMapper objectMapper;

    protected void cleanSQL() {
        ScriptUtils.runInitScript(databaseDelegate, "ru/rerumu/lists/integration/clean.sql");
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    protected SeriesListView getSeriesList() throws Exception{
        String body = RestAssuredMockMvc
                .given()
                    .header("Accept-Type", "application/json")
                    .attribute("authUserId", 0L)
                    .log().all()
                .when()
                    .get("/api/v1/users/0/series")
                .then()
                    .log().all()
                    .statusCode(200)
                .extract().body().asString();

        return objectMapper.readValue(body, SeriesListView.class);
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    protected SeriesView getSeriesByTitle(@NonNull String title) throws Exception{
        SeriesListView seriesListView = getSeriesList();

        return seriesListView.items().stream()
                .filter(item -> item.title().equals(title))
                .findAny()
                .orElse(null);
    }

    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    protected BookListView getBooksList() throws Exception {
        String body = RestAssuredMockMvc
                .given()
                    .header("Accept-Type", "application/json")
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                    .body("""
                            {
                                "sort": [
                                    {
                                        "field": "readingRecords.updateDate",
                                        "ordering": "DESC"
                                    }
                                ],
                                "isChainBySeries": false,
                                "filters": [
                                    {
                                        "field": "bookStatusIds",
                                        "values": [
                                            "1",
                                            "2",
                                            "3",
                                            "4"
                                        ]
                                    }
                                ]
                            }
                            """)
                    .log().all()
                .when()
                    .post("/api/v1/users/{userId}/books/search", "0")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().body().asString();

        return objectMapper.readValue(body, BookListView.class);
    }

    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    protected BookView getBookByTitle(@NonNull String title) throws Exception {
        BookListView bookListView = getBooksList();

        return bookListView.getItems().stream()
                .filter(item -> item.getTitle().equals(title))
                .findAny()
                .orElse(null);
    }

    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    @NonNull
    protected BookView addBook(
            @NonNull String title,
            Long seriesId,
            Long authorId
    ) throws Exception {
        String body = RestAssuredMockMvc
                .given()
                    .body(String.format("""
                                {
                                    "title": "%s",
                                    "authorId": %d,
                                    "status": 1,
                                    "seriesId": %d,
                                    "lastChapter": 123,
                                    "bookTypeId": null,
                                    "insertDate": null,
                                    "note": "test note",
                                    "URL": null
                                }
                                """,
                            title,
                            authorId,
                            seriesId
                    ))
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .post("/api/v1/users/{userId}/books", "0")
                .then()
                    .statusCode(200)
                    .extract().body().asString();

        BookView bookView = objectMapper.readValue(body, BookView.class);
        Objects.requireNonNull(bookView);

        return bookView;
    }

    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    @NonNull
    public SeriesView addSeries(@NonNull String title) throws Exception {
        String body = RestAssuredMockMvc
                .given()
                    .body(String.format("""
                                {
                                    "title": "%s"
                                }
                                """, title))
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .post("/api/v1/users/0/series")
                .then()
                    .statusCode(200)
                    .extract().body().asString();

        SeriesView seriesView = objectMapper.readValue(body, SeriesView.class);
        Objects.requireNonNull(seriesView);

        return seriesView;
    }

}
