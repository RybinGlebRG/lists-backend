package ru.rerumu.lists.integration.book;

import com.jcabi.aspects.Loggable;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.controller.series.views.out.SeriesView;
import ru.rerumu.lists.integration.ITBase;
import ru.rerumu.lists.integration.TestCommon;

import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class ITBookAdd extends ITBase {

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
    public void shouldAddBook(TestInfo testInfo) throws Exception{

        TestCommon.addSeries("TestSeries 1");
        SeriesView seriesView = getSeriesByTitle("TestSeries 1");
        Objects.requireNonNull(seriesView);

        String addBookRequestBody = String.format(
                """
                {
                    "title": "TestBook",
                    "authorId": null,
                    "status": 1,
                    "seriesId": %d,
                    "lastChapter": 123,
                    "bookTypeId": 1,
                    "insertDate": null,
                    "note": "test note",
                    "URL": null
                }""",
                seriesView.seriesId()
                );

        String expectedSchema = """
                {
                  "$schema": "http://json-schema.org/draft-04/schema#",
                  "type": "object",
                  "properties": {
                    "bookId": {
                      "type": "integer"
                    },
                    "readListId": {
                      "type": "null"
                    },
                    "title": {
                      "type": "string"
                    },
                    "bookStatus": {
                      "type": "object",
                      "properties": {
                        "statusId": {
                          "type": "integer"
                        },
                        "statusName": {
                          "type": "string"
                        }
                      },
                      "required": [
                        "statusId",
                        "statusName"
                      ]
                    },
                    "insertDate": {
                      "type": "string"
                    },
                    "lastChapter": {
                      "type": "null"
                    },
                    "note": {
                      "type": "string"
                    },
                    "bookType": {
                      "type": "object",
                      "properties": {
                        "typeId": {
                            "type": "number"
                        },
                        "typeName": {
                            "type": "string"
                        }
                      }
                    },
                    "itemType": {
                      "type": "string"
                    },
                    "chain": {
                      "type": "array",
                      "items": {}
                    },
                    "readingRecords": {
                      "type": "array",
                      "items": [
                        {
                          "type": "object",
                          "properties": {
                            "recordId": {
                              "type": "integer"
                            },
                            "bookId": {
                              "type": "integer"
                            },
                            "bookStatus": {
                              "type": "object",
                              "properties": {
                                "statusId": {
                                  "type": "integer"
                                },
                                "statusName": {
                                  "type": "string"
                                }
                              },
                              "required": [
                                "statusId",
                                "statusName"
                              ]
                            },
                            "startDate": {
                              "type": "string"
                            },
                            "endDate": {
                              "type": "null"
                            },
                            "isMigrated": {
                              "type": "boolean"
                            },
                            "lastChapter": {
                              "type": "integer"
                            }
                          },
                          "required": [
                            "recordId",
                            "bookId",
                            "bookStatus",
                            "startDate",
                            "endDate",
                            "isMigrated",
                            "lastChapter"
                          ]
                        }
                      ]
                    },
                    "tags": {
                      "type": "array",
                      "items": {}
                    },
                    "textAuthors": {
                      "type": "array",
                      "items": {}
                    },
                    "url": {
                      "type": "null"
                    },
                    "seriesList": {
                        "type": "array",
                        "items": [
                            {
                                "type": "object",
                                "properties": {
                                    "seriesId": {
                                        "type": "integer"
                                    },
                                    "title": {
                                        "type": "string"
                                    }
                                }
                            }
                        ]
                    }
                  },
                  "required": [
                    "bookId",
                    "readListId",
                    "title",
                    "bookStatus",
                    "insertDate",
                    "lastChapter",
                    "note",
                    "bookType",
                    "itemType",
                    "chain",
                    "readingRecords",
                    "tags",
                    "textAuthors",
                    "url",
                    "seriesList"
                  ]
                }
                """;

        String responseBody = RestAssuredMockMvc
                .given()
                    .body(addBookRequestBody)
                    .header("Content-Type", "application/json")
                    .attribute("authUserId", 0L)
                .when()
                    .post("/api/v1/users/0/books")
                .then()
                    .statusCode(200)
                    .body(JsonSchemaValidator.matchesJsonSchema(expectedSchema))
                .extract().body().asString();
        log.info("responseBody: {}", responseBody);


        String expectedResponseBodyWithoutDatesAndId = """
                {
                    "readListId": null,
                    "title": "TestBook",
                    "bookStatus": {
                        "statusId": 1,
                        "statusName": "In progress"
                    },
                    "lastChapter": null,
                    "note": "test note",
                    "bookType": {
                        "typeId": 1,
                        "typeName": "Book"
                    },
                    "itemType": "BOOK",
                    "chain": [],
                    "readingRecords": [{
                            "bookStatus": {
                                "statusId": 1,
                                "statusName": "In progress"
                            },
                            "endDate": null,
                            "isMigrated": false,
                            "lastChapter": 123
                        }
                    ],
                    "tags": [],
                    "textAuthors": [],
                    "seriesList": [
                        {
                            "seriesId": 1,
                            "title": "TestSeries 1"
                        }
                    ],
                    "url": null
                }
                """;

        JSONAssert.assertEquals(
                "Incorrect response",
                expectedResponseBodyWithoutDatesAndId,
                responseBody,
                false
        );

    }
}