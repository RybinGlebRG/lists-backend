package ru.rerumu.lists.model.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.rerumu.lists.model.book.BookDTO;

@NoArgsConstructor
@AllArgsConstructor
public class BookOrderedDTO {

    public BookDTO bookDTO;
    public Integer order;

    public BookDTO getBookDTO() {
        return bookDTO;
    }

    public Integer getOrder() {
        return order;
    }
}
