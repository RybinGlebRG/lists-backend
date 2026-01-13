package ru.rerumu.lists.domain.booktype.impl;

import org.json.JSONObject;
import ru.rerumu.lists.domain.booktype.BookType;

import java.util.Objects;

public class BookTypeImpl implements BookType {

    private final Long id;
    private final String name;

    public BookTypeImpl(Long id, String name){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("name", name);
        return jsonObject;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookTypeImpl bookType = (BookTypeImpl) o;
        return id == bookType.id && Objects.equals(name, bookType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
