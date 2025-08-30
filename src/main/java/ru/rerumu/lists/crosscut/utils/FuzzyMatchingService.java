package ru.rerumu.lists.crosscut.utils;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.domain.book.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FuzzyMatchingService {
    private final static int LIMIT = 10;

    public Stream<Book> findMatchingBooksByTitle(List<String> titles, Stream<Book> books){
        List<Book> bookList = books.collect(Collectors.toCollection(ArrayList::new));
        Map<Book, Float> booksDistances = new HashMap<>();

        for (Book book: bookList){
            for (String title: titles){
                float score = book.getTitleFuzzyMatchScore(title);
                 if (!booksDistances.containsKey(book) || (booksDistances.containsKey(book) && booksDistances.get(book) <= score)){
                    booksDistances.put(book,score);
                }
            }
        }

        Comparator<Map.Entry<Book, Float>> mapComparator= (e1, e2)->Float.compare(e1.getValue(), e2.getValue());
        mapComparator = mapComparator.reversed();

        return booksDistances.entrySet().stream()
                .sorted(mapComparator)
                .limit(LIMIT)
                .map(Map.Entry::getKey);
    }
}
