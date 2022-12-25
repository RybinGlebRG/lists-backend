package ru.rerumu.lists.model;


public enum BookTypeOld {
    BOOK(1,"Book"),
    LIGHT_NOVEL(2,"Light Novel"),
    WEBTOON(3,"Webtoon");

    private final int id;
    private final String nice;

    private BookTypeOld(int id, String nice){
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

        public BookTypeOld build(){
            switch (typeId){
                case 1:
                    return BookTypeOld.BOOK;
                case 2:
                    return BookTypeOld.LIGHT_NOVEL;
                case 3:
                    return BookTypeOld.WEBTOON;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
