package ru.rerumu.lists.controller.games;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.controller.games.views.GameAddView;
import ru.rerumu.lists.controller.games.views.GameListView;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.services.game.GameService;

import java.util.List;

@RestController
public class GamesController {

    private final GameService gameService;

    public GamesController(
            GameService gameService
    ) {
        this.gameService = gameService;
    }

    @PostMapping(
            value = "/api/v1/users/{userId}/games",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> addOne(
            @PathVariable Long userId,
            @RequestBody GameAddView gameAddView
    ) throws EntityNotFoundException {

        gameService.addGame(userId, gameAddView);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/api/v1/users/{userId}/games",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(
            @PathVariable Long userId
    ) throws EntityNotFoundException {
        List<Game> gamesList = gameService.getAll(userId);
        GameListView gameListView = new GameListView.Builder()
                .gamesList(gamesList)
                .build();

        return new ResponseEntity<>(gameListView.toString(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/v1/users/{userId}/games/{gameId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteOne(
            @PathVariable Long userId,
            @PathVariable Long gameId
    ) {
        gameService.deleteGame(gameId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
