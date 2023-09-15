package ru.rerumu.lists.services;

import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.views.GameAddView;

import java.util.List;

public interface GameService {

    List<Game> getAll(User user, Search search);

    void addGame(User user, GameAddView gameAddView);

    // TODO: Implement
    void deleteGame(Integer gameId);
}
