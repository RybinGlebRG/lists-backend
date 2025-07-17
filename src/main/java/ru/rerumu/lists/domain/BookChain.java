package ru.rerumu.lists.domain;

import org.json.JSONArray;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.dto.BookOrderedDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record BookChain(HashMap<Book,Integer> map) {

    public BookChain{
        map = new HashMap<>(map);
    }

    @Override
    public HashMap<Book, Integer> map() {
        return new HashMap<>(map);
    }

    public JSONArray toJSONArray() {
        Comparator<Map.Entry<Book,Integer>> comparator = Map.Entry.comparingByValue();
        comparator = comparator.reversed();

        JSONArray chainArray = map.entrySet()
                .stream()
                .sorted(comparator)
                .map(Map.Entry::getKey)
                .map(Book::toJSONObject)
                .collect(JSONArray::new, JSONArray::put, JSONArray::putAll);

        return chainArray;
    }

    public List<BookOrderedDTO> toDTO(){
        return map.entrySet().stream()
                .map(entry -> new BookOrderedDTO(entry.getKey().toDTO(), entry.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));

    }
}
