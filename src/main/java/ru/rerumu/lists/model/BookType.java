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

    public static class Builder{
        private Integer typeId;

        public Builder typeId(Integer typeId){
            this.typeId = typeId;
            return this;
        }

        public BookType build(){
            switch (typeId){
                case 1:
                    return BookType.BOOK;
                case 2:
                    return BookType.LIGHT_NOVEL;
                case 3:
                    return BookType.WEBTOON;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
