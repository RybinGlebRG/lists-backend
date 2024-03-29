package ru.rerumu.lists.services;

import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public class BookTypesService {

    private final CrudRepository<BookType,Integer> crudRepository;

    public BookTypesService(CrudRepository<BookType,Integer> crudRepository){
        this.crudRepository = crudRepository;
    }

    public List<BookType> findAll(){
        return crudRepository.findAll();
    }

    public Optional<BookType> findById(int bookTypeId){
        return crudRepository.findById(bookTypeId);
    }
}
