package ru.rerumu.lists.services.book.type;

import org.springframework.stereotype.Service;
import ru.rerumu.lists.dao.booktype.BookTypeRepository;
import ru.rerumu.lists.domain.booktype.BookType;

import java.util.List;

@Service
public class BookTypesService {

    private final BookTypeRepository bookTypeRepository;

    public BookTypesService(BookTypeRepository bookTypeRepository){
        this.bookTypeRepository = bookTypeRepository;
    }

    public List<BookType> findAll(){
        return bookTypeRepository.findAll();
    }

    public BookType findById(Long bookTypeId){
        return bookTypeRepository.findById(bookTypeId);
    }
}
