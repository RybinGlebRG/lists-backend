package ru.rerumu.lists.model;

import org.json.JSONObject;

public class Author {

    private final Long authorId;
    private final Long readListId;
    private String name;

    public Author(Long authorId, Long readListId, String name){
        this.authorId = authorId;
        this.readListId = readListId;
        this.name = name;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getReadListId() {
        return readListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("authorId", authorId);
        obj.put("readListId", readListId);
        obj.put("name", name);

        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    public static class Builder{
        private  Long authorId;
        private  Long readListId;
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder authorId(Long authorId){
            this.authorId = authorId;
            return this;
        }

        public Builder readListId(Long readListId) {
            this.readListId = readListId;
            return this;
        }

        public Author build(){
            return  new Author(authorId,readListId,name);
        }
    }
}
