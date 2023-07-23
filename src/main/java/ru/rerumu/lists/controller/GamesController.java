package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.factories.UserServiceProxyFactory;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.services.GameService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.services.UserServiceImpl;
import ru.rerumu.lists.views.BookListView;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.views.GameAddView;
import ru.rerumu.lists.views.GameListView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class GamesController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GameService gameService;
    @Deprecated
    private final UserService userService;

    private final UserServiceProxyFactory userServiceProxyFactory;

    public GamesController(GameService gameService, @Qualifier("UserServiceProtectionProxy") UserService userService, UserServiceProxyFactory userServiceProxyFactory) {
        this.gameService = gameService;
        this.userService = userService;
        this.userServiceProxyFactory = userServiceProxyFactory;
    }

    @PostMapping(
            value = "/api/v0.2/users/{userId}/games",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> addOne(
            @PathVariable Long userId,
            @RequestBody GameAddView gameAddView
    ) throws EntityNotFoundException {

        Optional<User> user = userService.getOne(userId);
        gameService.addGame(user.orElseThrow(),gameAddView);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/api/v0.2/users/{userId}/games/search",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> searchBooks(
            @PathVariable Long userId,
            @RequestBody Search search
    ) throws EntityNotFoundException {
        Optional<User> user = userService.getOne(userId);
        List<Game> gamesList = gameService.getAll(user.orElseThrow(EntityNotFoundException::new), search);
        GameListView gameListView = new GameListView.Builder()
                .gamesList(gamesList)
                .build();

        return new ResponseEntity<>(gameListView.toString(), HttpStatus.OK);
    }
}
