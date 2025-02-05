package ru.rerumu.lists.model.book.type;

import org.json.JSONObject;

import java.util.Objects;

public class BookType {

    private final int id;
    private final String name;

    public BookType(Integer id, String name){
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

    public BookTypeDTO toDTO(){
        BookTypeDTO bookTypeDTO = new BookTypeDTO(
            id,
            name
        );

        return bookTypeDTO;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookType bookType = (BookType) o;
        return id == bookType.id && Objects.equals(name, bookType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
