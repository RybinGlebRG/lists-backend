package ru.rerumu.lists.services.game;

import ru.rerumu.lists.model.game.Game;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.views.GameAddView;

import java.util.List;
import java.util.Optional;

public interface GameService {

    List<Game> getAll(User user, Search search);

    void addGame(User user, GameAddView gameAddView);

    void deleteGame(Integer gameId);

    Optional<Game> findById(Integer gameId);
}
