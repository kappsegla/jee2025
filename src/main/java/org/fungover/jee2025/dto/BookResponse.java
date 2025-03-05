package org.fungover.jee2025.dto;

import org.fungover.jee2025.entity.Book;

/**
 * DTO for {@link Book}
 */
public record BookResponse(Long id, String title, int pageCount) {

    public BookResponse(Book book) {
        this(book.getId(), book.getTitle(), book.getPageCount());
    }

    public static BookResponse map(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getPageCount());
    }
}
