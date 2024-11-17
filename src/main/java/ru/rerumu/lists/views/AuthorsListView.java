package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.Author;

import java.util.List;

public class AuthorsListView {

    private final List<Author> authorList;

    public AuthorsListView(List<Author> authorList){
        this.authorList = authorList;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        JSONArray authorArray = new JSONArray();
        for (Author item: this.authorList){
            authorArray.put(item.toJSONObject());
        }
        obj.put("items",authorArray);
        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }
}
