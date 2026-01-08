package ru.rerumu.lists.domain.movie;

public record MovieStatus(int statusId, String statusName) {
    public MovieStatus {
        if (statusName == null){
            throw new IllegalArgumentException();
        }
    }
}
