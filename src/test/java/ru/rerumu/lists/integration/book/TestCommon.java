package ru.rerumu.lists.integration.book;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestCommon {

    public static void addBook(
            @NonNull String title
    ) {
        String responseBody = RestAssuredMockMvc
                .given()
                .body(String.format("""
                        {
                            "title": "%s",
                            "authorId": null,
                            "status": 1,
                            "seriesId": null,
                            "lastChapter": 123,
                            "bookTypeId": null,
                            "insertDate": null,
                            "note": "test note",
                            "URL": null
                        }
                        """, title))
                .header("Content-Type", "application/json")
                .attribute("authUserId", 0L)
                .when()
                .post("/api/v1/users/0/books")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        log.info("responseBody: {}", responseBody);
    }

    public static void addSeries(@NonNull String title) {
        String responseBody = RestAssuredMockMvc
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
                .extract()
                .body()
                .asString();
        log.info("responseBody: {}", responseBody);
    }

}
