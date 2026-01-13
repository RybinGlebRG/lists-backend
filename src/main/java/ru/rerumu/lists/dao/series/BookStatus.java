package ru.rerumu.lists.dao.series;

public enum BookStatus {
    IN_PROGRESS(1,"In Progress"),
    COMPLETED(2,"Completed");

    private final int id;
    private final String nice;

    private BookStatus(int id, String nice){
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
