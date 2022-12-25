package ru.rerumu.lists.model;

import org.json.JSONObject;

public class BookType {

    private final int id;
    private final String name;

    public BookType(int id, String name){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("name", name);
        return jsonObject;
    }
}
