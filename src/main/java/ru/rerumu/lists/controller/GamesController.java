package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.domain.factories.UserServiceProxyFactory;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.game.GameService;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.GameAddView;
import ru.rerumu.lists.views.GameListView;

import java.util.List;

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

        User user = userService.findById(userId);
        gameService.addGame(user, gameAddView);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/api/v0.2/users/{userId}/games/search",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> searchBooks(
            @PathVariable Long userId,
            @RequestBody Search search
    ) throws EntityNotFoundException {
        User user = userService.findById(userId);
        List<Game> gamesList = gameService.getAll(user, search);
        GameListView gameListView = new GameListView.Builder()
                .gamesList(gamesList)
                .build();

        return new ResponseEntity<>(gameListView.toString(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/v0.2/games/{gameId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteOne(
            @PathVariable Integer gameId
    ) {
        gameService.deleteGame(gameId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
