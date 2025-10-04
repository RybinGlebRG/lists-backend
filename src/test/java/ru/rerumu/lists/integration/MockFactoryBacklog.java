package ru.rerumu.lists.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockFactoryBacklog {

    public static void addBacklogItem(
        @NonNull String title,
        String note
    ) {

        String actualNote = null;
        if ( note != null) {
            actualNote = String.format("\"%s\"", note);
        }

        String responseBody = RestAssuredMockMvc
                .given()
                .body(String.format("""
                        {
                            "title": "%s",
                            "type": 0,
                            "creationDate": "2025-10-04T01:01:00",
                            "note": %s
                        }
                        """,
                        title,
                        actualNote
                ))
                .header("Content-Type", "application/json")
                .attribute("authUserId", 0L)
                .when()
                .post("/api/v1/users/0/backlogItems")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        log.info("responseBody: {}", responseBody);
    }

}
