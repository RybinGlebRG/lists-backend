package ru.rerumu.lists.domain.book;

import java.util.HashMap;

public record BookChain(HashMap<Book,Integer> map) {

    public BookChain{
        map = new HashMap<>(map);
    }

    @Override
    public HashMap<Book, Integer> map() {
        return new HashMap<>(map);
    }

}
