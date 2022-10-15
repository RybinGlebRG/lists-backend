package ru.rerumu.lists.model;

public enum BookType {
    BOOK(1,"Book"),
    LIGHT_NOVEL(2,"Light Novel"),
    WEBTOON(3,"Webtoon");

    private final int id;
    private final String nice;

    private BookType(int id, String nice){
        this.id = id;
        this.nice = nice;
    }

    public int getId() {
        return this.id;
    }

    public String getNice() {
        return nice;
    }
}
