package ru.rerumu.lists.integration.backlog;

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
import ru.rerumu.lists.crosscut.Profiles;
import ru.rerumu.lists.integration.MockFactoryBacklog;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(Profiles.TEST)
@ExtendWith(SpringExtension.class)
@Slf4j
public class ITBacklogItemsGetAll {

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
    public void shouldGet(TestInfo testInfo) throws Exception {
        log.info("Test: {}", testInfo.getDisplayName());

        MockFactoryBacklog.addBacklogItem(
                "Test Backlog Item 1",
                null
        );
        MockFactoryBacklog.addBacklogItem(
                "Test Backlog Item 2",
                null
        );

        String responseBody = RestAssuredMockMvc
                .given()
                .attribute("authUserId", 0L)
                .when()
                .get("/api/v1/users/0/backlogItems")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        log.info("responseBody: {}", responseBody);

        JSONAssert.assertEquals(
                "Incorrect response",
                """
                        {
                            "items": [
                                {
                                    "id": 0,
                                    "title": "Test Backlog Item 1",
                                    "type": 0,
                                    "note": null,
                                    "creationDate": "2025-10-04T01:01:00"
                                },
                                {
                                    "id": 1,
                                    "title": "Test Backlog Item 2",
                                    "type": 0,
                                    "note": null,
                                    "creationDate": "2025-10-04T01:01:00"
                                }
                            ]
                        }
                        """,
                responseBody,
                true
        );

    }

}
