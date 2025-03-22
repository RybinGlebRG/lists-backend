package ru.rerumu.lists.controller.book.view.out;

import lombok.NonNull;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.books.Filter;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;

import java.util.Comparator;
import java.util.List;

public class BookViewBuilder {

    public BookView buildBookView(@NonNull BookDTO bookDTO, @NonNull Search search) {
        BookView bookView = new BookView(bookDTO);

        Comparator<Book> comparator = Comparator.comparing(book -> 0);


    }

    public List<BookView> buildBookListView(List<BookDTO> bookDTOList, Search search) {
        boolean isSearch = false;
        if (search.filters() != null) {
            for (Filter filter : search.filters()) {
                if (filter.field().equals("titles")) {
                    isSearch = true;
                }
            }
        }
        if (isSearch){
            return bookView;
        }
        Comparator<Book> comparator = Comparator.comparing(book -> 0);

        for (SortItem sortItem : search.getSortItemList()) {
            if (sortItem.getSortField().equals("createDate")) {

                comparator = comparator.thenComparing(book -> ((BookImpl)book).getInsertDate());

                if (sortItem.getSearchOrder() == SearchOrder.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }

        comparator = comparator.thenComparing(Book::getId);

        this.bookList.sort(comparator);
    }
}
