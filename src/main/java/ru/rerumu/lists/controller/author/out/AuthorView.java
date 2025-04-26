package ru.rerumu.lists.controller.author.out;

import org.json.JSONObject;
import ru.rerumu.lists.model.author.Author;
import ru.rerumu.lists.model.author.AuthorDTO;

public class AuthorView {

    private final Author author;

    public AuthorView(Author author) {
        this.author = author;
    }

    public JSONObject toJSONObject(){
        AuthorDTO authorDTO = author.toDTO();

        JSONObject obj = new JSONObject();
        obj.put("authorId", authorDTO.getAuthorId());
        obj.put("name", authorDTO.getName());
        obj.put("user", authorDTO.getUser());

        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

}
