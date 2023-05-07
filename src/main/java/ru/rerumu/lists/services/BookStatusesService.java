package ru.rerumu.lists.services;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public class BookStatusesService {

    private final CrudRepository<BookStatusRecord,Integer> crudRepository;

    public BookStatusesService(CrudRepository<BookStatusRecord,Integer> crudRepository){
        this.crudRepository = crudRepository;
    }

    public List<BookStatusRecord> findAll(){
        return crudRepository.findAll();
    }

    public Optional<BookStatusRecord> findById(int bookTypeId){
        return crudRepository.findById(bookTypeId);
    }
}
