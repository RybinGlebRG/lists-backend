package ru.rerumu.lists.integration.backlog;

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
import ru.rerumu.lists.integration.ITBase;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class ITBacklogItemAdd extends ITBase {

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

    /**
     * Should add backlog item
     */
    @Test
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public void shouldAdd(TestInfo testInfo) throws Exception {

        String responseBody = RestAssuredMockMvc
                .given()
                    .body("""
                            {
                                "title": "Test Backlog Item",
                                "type": 0,
                                "creationDate": "2025-10-04T01:01:00"
                            }
                            """)
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

        JSONAssert.assertEquals(
                "Incorrect response",
                """
                        {
                            "id": 0,
                            "title": "Test Backlog Item",
                            "type": 0,
                            "note": null,
                            "creationDate": "2025-10-04T01:01:00"
                        }
                        """,
                responseBody,
                true
        );

    }

}
