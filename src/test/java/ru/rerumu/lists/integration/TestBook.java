package ru.rerumu.lists.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestBook {

    public static void addBook(
            @NonNull String title,
            Long seriesId
    ) {
        String responseBody = RestAssuredMockMvc
                .given()
                .body(String.format("""
                        {
                            "title": "%s",
                            "authorId": null,
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
                        seriesId
                ))
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

}
