package ru.rerumu.lists.services;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.Book;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FuzzyMatchingService {
    private final static int LIMIT = 10;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public Stream<Book> findMatchingBooksByTitle(List<String> titles, Stream<Book> books){
        List<Book> bookList = books.collect(Collectors.toCollection(ArrayList::new));
        Map<Book, Float> booksDistances = new HashMap<>();

        for (Book book: bookList){
            for (String title: titles){
                float score;
                if (title.equals(book.title())){
                    score = 1f;
                } else {
                    Integer distance = levenshteinDistance.apply(book.title(), title);
                    score = (float) (book.title().length() - distance) / book.title().length();
                }
                 if (!booksDistances.containsKey(book) || (booksDistances.containsKey(book) && booksDistances.get(book) <= score)){
                    booksDistances.put(book,score);
                }
            }
        }

        Comparator<Map.Entry<Book, Float>> mapComparator= (e1,e2)->Float.compare(e1.getValue(), e2.getValue());
        mapComparator = mapComparator.reversed();

        return booksDistances.entrySet().stream()
                .sorted(mapComparator)
                .limit(LIMIT)
                .map(Map.Entry::getKey);
    }
}
