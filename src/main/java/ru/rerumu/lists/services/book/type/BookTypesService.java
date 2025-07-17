package ru.rerumu.lists.services.book.type;

import ru.rerumu.lists.domain.book.type.BookType;
import ru.rerumu.lists.domain.book.type.BookTypeDTO;
import ru.rerumu.lists.dao.base.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookTypesService {

    private final CrudRepository<BookTypeDTO,Integer> crudRepository;

    public BookTypesService(CrudRepository<BookTypeDTO,Integer> crudRepository){
        this.crudRepository = crudRepository;
    }

    public List<BookType> findAll(){
        return crudRepository.findAll().stream()
                .map(BookTypeDTO::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Optional<BookType> findById(int bookTypeId){
        return crudRepository.findById(bookTypeId, null).map(BookTypeDTO::toDomain);
    }
}
