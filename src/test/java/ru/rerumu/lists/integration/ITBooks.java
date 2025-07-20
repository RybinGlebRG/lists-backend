package ru.rerumu.lists.integration;

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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Slf4j
class ITBooks {

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
    public void shouldAddBook(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());

        /*
        Given
         */
        String addBookRequestBody = """
                {
                    "title": "TestBook",
                    "authorId": null,
                    "status": 1,
                    "seriesId": null,
                    "lastChapter": null,
                    "bookTypeId": null,
                    "insertDate": null,
                    "note": null,
                    "URL": null
                }""";

        String responseBody = RestAssuredMockMvc

                .given()
                .body(addBookRequestBody)
                .header("Content-Type", "application/json")
                .attribute("authUserId", 0L)


        /*
        When
         */
                .when()
                .post("/api/v1/users/0/books")


        /*
       Then
        */
                .then()
                .statusCode(204)

                .extract()
                .body()
                .asString();
        log.info("responseBody: {}", responseBody);


        String expectedResponseBody = """
                {}
                """;

        JSONAssert.assertEquals(
                "Incorrect response",
                expectedResponseBody,
                responseBody,
                false
        );

    }
}