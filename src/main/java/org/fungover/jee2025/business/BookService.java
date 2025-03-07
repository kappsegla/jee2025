package org.fungover.jee2025.business;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.fungover.jee2025.persistence.BookRepository;
import org.fungover.jee2025.dto.BookResponse;
import org.fungover.jee2025.dto.CreateBook;
import org.fungover.jee2025.dto.UpdateBook;
import org.fungover.jee2025.entity.Book;
import org.fungover.jee2025.exceptions.NotFound;

import java.util.List;
import java.util.Objects;

import static org.fungover.jee2025.mapper.BookMapper.*;

@ApplicationScoped
public class BookService {

    private BookRepository repository;

    @Inject
    public BookService(BookRepository bookRepository) {
        this.repository = bookRepository;

    }

    public BookService(){}


    public List<BookResponse> getAllBooks() {
        return repository.findAll()
                .map(BookResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }

    public BookResponse getBookById(Long id) {
        return repository.findById(id)
                .map(BookResponse::new)
                .orElseThrow(
                        () -> new NotFound("Book with id " + id + " not found")
                );
    }

    public Book createBook(CreateBook book) {
        var newBook = map(book);
        newBook = repository.insert(newBook);
        return newBook;
    }

    public void updateBook(UpdateBook book, Long id) {
        var oldBook = repository.findById(id).orElseThrow(() -> new NotFound("Book with id " + id + " not found"));
        if(book.title() != null)
            oldBook.setTitle(book.title());
        if(book.pageCount() != 0)
            oldBook.setPageCount(book.pageCount());
        repository.update(oldBook);
    }
}
