package ru.rerumu.lists.domain;

import org.json.JSONObject;

@Deprecated
public class BookStatus_ {

    private final Long statusId;
    private final String name;

    public BookStatus_(Long statusId, String name){
        this.statusId = statusId;
        this.name = name;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("statusId", statusId);
        obj.put("name", name);

        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

}
