package org.fungover.jee2025;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import org.fungover.jee2025.dto.BookResponse;
import org.fungover.jee2025.dto.CreateBook;
import org.fungover.jee2025.dto.UpdateBook;
import org.fungover.jee2025.entity.Book;
import org.fungover.jee2025.exceptions.NotFound;
import org.fungover.jee2025.mapper.BookMapper;

import java.util.List;
import java.util.Objects;

@Path("books")
@Log
public class BookResource {

    private BookRepository repository;

    @Inject
    public BookResource(BookRepository repository) {
        this.repository = repository;
    }


    public BookResource() {

    }

    //"http://localhost:8080/api/books"
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookResponse> getBooks() {
        return repository.findAll()
                .map(BookResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }

    //"http://localhost:8080/api/books/4"
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookResponse getOneBook(@PathParam("id") Long id) {
        return repository.findById(id)
                .map(BookResponse::new)
                .orElseThrow(
                        () -> new NotFound("Book with id " + id + " not found")
                );
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewBook(CreateBook book) {
        if( book == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Book cannot be null").build();
        Book newBook = BookMapper.map(book);
        newBook = repository.insert(newBook);
        return Response
                .status(Response.Status.CREATED)
                .header("Location", "/api/books/" + newBook.getId())
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(UpdateBook book, @PathParam("id") Long id) {
        var oldBook = repository.findById(id).orElseThrow(() -> new NotFound("Book with id " + id + " not found"));
        oldBook.setTitle(book.title());
        oldBook.setPageCount(book.pageCount());
        repository.update(oldBook);
        return Response.noContent().build();
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBookFieldByField(UpdateBook book, @PathParam("id") Long id) {
        var oldBook = repository.findById(id).orElseThrow(() -> new NotFound("Book with id " + id + " not found"));
        if( book.title() != null)
            oldBook.setTitle(book.title());
        if( book.pageCount() != 0)
            oldBook.setPageCount(book.pageCount());
        repository.update(oldBook);
        return Response.noContent().build();
    }
}
