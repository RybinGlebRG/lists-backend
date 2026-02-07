package ru.rerumu.lists.integration;

import com.jcabi.aspects.Loggable;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestCommon {

    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public static void addBook(
            @NonNull String title,
            Long seriesId,
            Long authorId
    ) {
        RestAssuredMockMvc
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
                    .log().all()
                .when()
                    .post("/api/v1/users/{userId}/books", "0")
                .then()
                    .log().all()
                    .statusCode(200);
    }

    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public static void addSeries(@NonNull String title) {
        RestAssuredMockMvc
                .given()
                    .body(String.format("""
                            {
                                "title": "%s"
                            }
                            """, title))
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                    .log().all()
                .when()
                    .post("/api/v1/users/0/series")
                .then()
                    .log().all()
                    .statusCode(200);
    }

    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public static void addAuthor(@NonNull String name) {
        RestAssuredMockMvc
                .given()
                    .body(String.format("""
                            {
                                "name": "%s"
                            }
                            """, name))
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                    .log().all()
                .when()
                    .post("/api/v1/users/{userId}/authors", "0")
                .then()
                    .log().all()
                    .statusCode(204);
    }

}
