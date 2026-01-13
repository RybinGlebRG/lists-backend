package ru.rerumu.lists.controller.author.views.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.author.Author;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthorViewFactory {

    public AuthorView buildAuthorView(Author author) {
        return new AuthorView(author);
    }

    public AuthorsListView buildAuthorsListView(List<Author> authors) {

        List<AuthorView> views = authors.stream()
                .sorted(Comparator.comparing(author -> author.getName().toLowerCase().strip()))
                .map(AuthorView::new)
                .collect(Collectors.toCollection(ArrayList::new));

        return new AuthorsListView(views);
    }
}
