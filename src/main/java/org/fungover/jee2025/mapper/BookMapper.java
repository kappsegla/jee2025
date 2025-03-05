package org.fungover.jee2025.mapper;

import org.fungover.jee2025.dto.BookResponse;
import org.fungover.jee2025.dto.CreateBook;
import org.fungover.jee2025.entity.Book;

public class BookMapper {

    private BookMapper() {}

    public static BookResponse map(Book book) {
        if( null == book )
            return null;
        return new BookResponse(book.getId(), book.getTitle(), book.getPageCount());
    }

    public static Book map(CreateBook book) {
        if( null == book )
            return null;
        Book newBook = new Book();
        newBook.setTitle(book.title());
        newBook.setPageCount(book.pageCount());
        return newBook;
    }
}
