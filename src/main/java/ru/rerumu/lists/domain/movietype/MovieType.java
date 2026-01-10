package ru.rerumu.lists.domain.movietype;

import org.json.JSONObject;

// TODO: To record
public class MovieType {
    private final Long typeId;
    private final String name;

    public MovieType(Long typeId, String name){
        this.typeId = typeId;
        this.name = name;
    }

    public Long getTypeId() {
        return typeId;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("typeId", typeId);
        obj.put("name", name);

        return obj;
    }
}
