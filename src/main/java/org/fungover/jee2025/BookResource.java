package org.fungover.jee2025;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.fungover.jee2025.dto.BookResponse;

import java.util.ArrayList;
import java.util.List;

@Path("books")
public class BookResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BookResponse firstTest() {
        return new BookResponse("Title",123);
    }

    @GET
    @Path("many")
    @Produces(MediaType.APPLICATION_JSON)
    public Books secondTest() {
        List<BookResponse> books = new ArrayList<BookResponse>();
        books.add(new BookResponse("Title",123));
        books.add(new BookResponse("Title 2",456));

        return new Books(books,2);
    }

    public record Books(List<BookResponse> values, int totalBooks){

    }

}
