package ru.rerumu.lists.model;

import org.json.JSONArray;
import ru.rerumu.lists.model.book.BookImpl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public record BookChain(HashMap<BookImpl,Integer> map) {

    public BookChain{
        map = new HashMap<>(map);
    }

    @Override
    public HashMap<BookImpl, Integer> map() {
        return new HashMap<>(map);
    }

    public JSONArray toJSONArray() {
        Comparator<Map.Entry<BookImpl,Integer>> comparator = Map.Entry.comparingByValue();
        comparator = comparator.reversed();

        JSONArray chainArray = map.entrySet()
                .stream()
                .sorted(comparator)
                .map(Map.Entry::getKey)
                .map(BookImpl::toJSONObject)
                .collect(JSONArray::new, JSONArray::put, JSONArray::putAll);

        return chainArray;
    }
}
