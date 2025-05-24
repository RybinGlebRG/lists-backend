package ru.rerumu.lists.controller.author.out;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
public class AuthorsListView {

    private final List<AuthorView2> items;

    public AuthorsListView(List<AuthorView2> items) {
        this.items = items;
    }
}
