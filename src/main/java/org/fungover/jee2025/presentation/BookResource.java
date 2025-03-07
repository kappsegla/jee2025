package org.fungover.jee2025.presentation;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import org.fungover.jee2025.business.BookService;
import org.fungover.jee2025.dto.BookResponse;
import org.fungover.jee2025.dto.CreateBook;
import org.fungover.jee2025.dto.ResponseDto;
import org.fungover.jee2025.dto.UpdateBook;
import org.fungover.jee2025.entity.Book;

@Path("books")
@Log
public class BookResource {

    private BookService bookService;

    @Inject
    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }


    public BookResource() {

    }

    //"http://localhost:8080/api/books"
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDto getBooks() {
        return new ResponseDto(bookService.getAllBooks());
    }

    //"http://localhost:8080/api/books/4"
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookResponse getOneBook(@PathParam("id") Long id) {
        return bookService.getBookById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewBook( @Valid @NotNull CreateBook book) {
        if( book == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Book cannot be null").build();
        Book newBook = bookService.createBook(book);
        log.info("Creating new book: " + newBook);
        return Response
                .status(Response.Status.CREATED)
                .header("Location", "/api/books/" + newBook.getId())
                .build();
    }

//    @PUT
//    @Path("{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response updateBook(UpdateBook book, @PathParam("id") Long id) {
//        var oldBook = repository.findById(id).orElseThrow(() -> new NotFound("Book with id " + id + " not found"));
//        oldBook.setTitle(book.title());
//        oldBook.setPageCount(book.pageCount());
//        repository.update(oldBook);
//        return Response.noContent().build();
//    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBookFieldByField(@Valid UpdateBook book, @PathParam("id") Long id) {
        bookService.updateBook(book, id);
        return Response.noContent().build();
    }

}
