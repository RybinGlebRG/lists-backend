package ru.rerumu.lists.model;

public record TitleStatus(int statusId, String statusName) {
    public TitleStatus {
        if (statusName == null){
            throw new IllegalArgumentException();
        }
    }
}
