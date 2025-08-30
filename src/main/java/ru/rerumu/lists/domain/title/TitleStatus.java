package ru.rerumu.lists.domain.title;

public record TitleStatus(int statusId, String statusName) {
    public TitleStatus {
        if (statusName == null){
            throw new IllegalArgumentException();
        }
    }
}
