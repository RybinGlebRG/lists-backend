package ru.rerumu.lists.services;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.repository.BookTypeRepository;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Component
public class BookTypesService {

    private final BookTypeRepository bookTypeRepository;

    public BookTypesService(BookTypeRepository bookTypeRepository){
        this.bookTypeRepository = bookTypeRepository;
    }

    public List<BookType> findAll(){
        return bookTypeRepository.findAll();
    }
}
