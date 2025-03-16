package ru.rerumu.lists.controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.services.author.AuthorsService;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.AddAuthorView;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@WebMvcTest(AuthorsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadListService readListService;

    @MockBean
    private AuthorsService authorsService;

    @MockBean(name="UserServiceProtectionProxy")
    private UserService userService;

    @MockBean(name="UserService")
    private UserService userService1;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void shouldGetAll(){
        Mockito.when(readListService.getAuthors(2L))
                .thenReturn(List.of(
                   new Author(1L,2L,"Test1"),
                   new Author(2L,2L,"Test2"),
                   new Author(3L,2L,"Test3")
                ));

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .get("/api/v0.2/readLists/2/authors")
                .then().statusCode(200)
                .body("items.size()",is(3))
                .and().body("items[0].authorId",equalTo(1))
                .and().body("items[1].authorId",equalTo(2))
                .and().body("items[2].authorId",equalTo(3));


    }

    @Test
    void shouldAddOne()throws  Exception{
        Mockito.when(authorsService.addAuthor(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new Author(1L,2L,"TestAuthor"));

        JSONObject requestBody = new JSONObject();
        requestBody.put("name","TestAuthor");

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/authors")
                .then().statusCode(201)
                .body("authorId",equalTo(1))
                .and().body("readListId",equalTo(2))
                .and().body("name",equalTo("TestAuthor"));

        Mockito.verify(authorsService).addAuthor(2L,new AddAuthorView("TestAuthor"));

    }

    @Test
    void shouldNotAddNotOwner()throws  Exception{
        Mockito.doThrow(new UserIsNotOwnerException())
                .when(userService).checkOwnershipList(Mockito.any(), Mockito.anyLong());

        JSONObject requestBody = new JSONObject();
        requestBody.put("name","TestAuthor");

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/api/v0.2/readLists/2/authors")
                .then().statusCode(403);

        Mockito.verify(authorsService, Mockito.never())
                .addAuthor(Mockito.anyLong(),Mockito.any());
        Mockito.verify(userService).checkOwnershipList("Test",2L);

    }

    @Test
    void shouldDelete()throws  Exception{

        RestAssuredMockMvc
                .given()
                .attribute("username","Test")
                .when()
                .delete("/api/v0.2/authors/1111")
                .then().statusCode(204);

        Mockito.verify(userService).checkOwnershipAuthor("Test",1111L);
        Mockito.verify(authorsService).deleteAuthor(1111L);

    }

//    @Test
//    void shouldGetOne(){
//        Mockito.when(readListService.getBook(2L,1L))
//                .thenReturn(
//                        new Book(
//                                1L,
//                                2L,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null
//                        )
//                );
//
//        RestAssuredMockMvc
//                .given()
//                .attribute("username","Test")
//                .when()
//                .get("/api/v0.2/readLists/2/authors/1")
//                .then().statusCode(200)
//                .and().body("bookId",equalTo(1))
//                .and().body("readListId",equalTo(2));
//    }

}