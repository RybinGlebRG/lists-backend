package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.services.GameService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.BookTypesListView;
import ru.rerumu.lists.views.GameListView;

import java.util.List;
import java.util.Optional;

@RestController
public class GamesController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GameService gameService;
    private final UserService userService;

    public GamesController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping(
            value = "/api/v0.2/users/{userId}/games",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> getAll(
            @PathVariable Long userId,
            @RequestAttribute("username") String username
    ) throws EntityNotFoundException {
        Optional<User> user = userService.getOne(userId);
        user.orElseThrow(EntityNotFoundException::new);

        List<Game> gamesList = gameService.getAll(user.get());
        GameListView gameListView = new GameListView.Builder()
                .gamesList(gamesList)
                .build();

        return new ResponseEntity<>(gameListView.toString(), HttpStatus.OK);
    }
}
