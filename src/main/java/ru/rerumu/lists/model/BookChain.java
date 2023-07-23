package ru.rerumu.lists.model;

import org.json.JSONArray;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
}
