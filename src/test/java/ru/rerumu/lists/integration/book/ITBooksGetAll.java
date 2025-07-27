package ru.rerumu.lists.integration.book;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class ITBooksGetAll {

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
    public void shouldGetAll(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());

        TestCommon.addSeries("TestSeries 1");
        TestCommon.addBook("TestBook 1");
        TestCommon.addBook("TestBook 2");

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

    }
}
