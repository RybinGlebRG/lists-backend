package ru.rerumu.lists.controller.games.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.domain.game.Game;

import java.util.Comparator;
import java.util.List;

public record GameListView(List<Game> gamesList) {

    public GameListView{
        if (gamesList == null){
            throw new IllegalArgumentException();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray gamesArray = new JSONArray();
        for (Game item : this.gamesList) {
            JSONObject bookObj = item.toJSONObject();
            gamesArray.put(bookObj);
        }
        obj.put("items", gamesArray);
        return obj;
    }

    private void sort(){
        Comparator<Game> gameComparator = Comparator
                .comparing(Game::getCreateDateUTC).reversed()
                .thenComparing(Game::getTitle);
        this.gamesList.sort(gameComparator);
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    public static final class Builder{
        private List<Game> gamesList;

        public Builder gamesList(List<Game> gamesList){
            this.gamesList = gamesList;
            return this;
        }

        public GameListView build(){
            GameListView gameListView = new GameListView(gamesList);
            gameListView.sort();
            return gameListView;
        }

    }
}
