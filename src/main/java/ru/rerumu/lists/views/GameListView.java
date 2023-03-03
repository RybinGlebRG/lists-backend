package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Game;

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
            return new GameListView(gamesList);
        }

    }
}
