package ru.rerumu.lists.model.title;

public record TitleStatus(int statusId, String statusName) {
    public TitleStatus {
        if (statusName == null){
            throw new IllegalArgumentException();
        }
    }
}
