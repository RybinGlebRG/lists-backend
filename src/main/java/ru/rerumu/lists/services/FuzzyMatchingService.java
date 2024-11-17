package ru.rerumu.lists.services;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.book.BookImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FuzzyMatchingService {
    private final static int LIMIT = 10;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public Stream<BookImpl> findMatchingBooksByTitle(List<String> titles, Stream<BookImpl> books){
        List<BookImpl> bookList = books.collect(Collectors.toCollection(ArrayList::new));
        Map<BookImpl, Float> booksDistances = new HashMap<>();

        for (BookImpl book: bookList){
            for (String title: titles){
                float score = getScore(book.getTitle(), title);
                 if (!booksDistances.containsKey(book) || (booksDistances.containsKey(book) && booksDistances.get(book) <= score)){
                    booksDistances.put(book,score);
                }
            }
        }

        Comparator<Map.Entry<BookImpl, Float>> mapComparator= (e1, e2)->Float.compare(e1.getValue(), e2.getValue());
        mapComparator = mapComparator.reversed();

        return booksDistances.entrySet().stream()
                .sorted(mapComparator)
                .limit(LIMIT)
                .map(Map.Entry::getKey);
    }

    private float getScore(String bookTitle, String searchString){
        if (bookTitle.equalsIgnoreCase(searchString)){
            return 1f;
        }

        List<String> titleSubstrings = new ArrayList<>();
        titleSubstrings.add(bookTitle);
        titleSubstrings.addAll(Arrays.asList(bookTitle.split(" ")));

        List<Float> scores = new ArrayList<>();
        for(String item: titleSubstrings){
            Integer distance = levenshteinDistance.apply(item.toUpperCase(), searchString.toUpperCase());
            Float score = (float) (item.length() - distance) / item.length();
            scores.add(score);
        }

        Float res = scores.stream().max(Float::compareTo).orElseThrow();
        return res;
    }
}
