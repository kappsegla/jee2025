package org.fungover.jee2025.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.fungover.jee2025.business.BookService;
import org.fungover.jee2025.dto.CreateBook;
import org.fungover.jee2025.entity.Book;
import org.fungover.jee2025.exceptions.NotFound;
import org.fungover.jee2025.exceptions.mappers.NotFoundMapper;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookResourceTest {

    @Mock
    BookService bookService;

    Dispatcher dispatcher;

    @BeforeEach
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        BookResource bookResource = new BookResource(bookService);
        dispatcher.getRegistry().addSingletonResource(bookResource);
        // Create your custom ExceptionMapper
        ExceptionMapper<NotFound> mapper = new NotFoundMapper();
        // Register your custom ExceptionMapper
        dispatcher.getProviderFactory().registerProviderInstance(mapper);
    }

    @Test
    void getAllBooks() throws URISyntaxException, UnsupportedEncodingException {
        Mockito.when(bookService.getAllBooks()).thenReturn(List.of());

        MockHttpRequest request = MockHttpRequest.get("/books");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        // Assert the response status code and content
        assertEquals(200, response.getStatus());
        assertEquals("[]", response.getContentAsString());
    }

    @Test
    void postPerson() throws Exception {
        Book book = new Book();
        book.setId(1L);
        Mockito.when(bookService.createBook(Mockito.any())).thenReturn(book);

        // Create a mock request and response
        MockHttpRequest request = MockHttpRequest.post("/books");

        String json = new ObjectMapper().writeValueAsString(new CreateBook("My life as a duck", 500));
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        // Assert the response status code and content
        assertEquals(201, response.getStatus());
    }
}
