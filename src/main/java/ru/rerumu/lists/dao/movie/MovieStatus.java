package ru.rerumu.lists.dao.movie;

public record MovieStatus(int statusId, String statusName) {
    public MovieStatus {
        if (statusName == null){
            throw new IllegalArgumentException();
        }
    }
}
