package ru.rerumu.lists.controller.author.out;

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

    public AuthorsListView buildAuthorsListView(List<Author> authors) {

        List<AuthorView2> views = authors.stream()
                .map(Author::toDTO)
                .sorted(Comparator.comparing(authorDTO -> authorDTO.getName().toLowerCase().strip()))
                .map(AuthorView2::new)
                .collect(Collectors.toCollection(ArrayList::new));

        return new AuthorsListView(views);
    }
}
