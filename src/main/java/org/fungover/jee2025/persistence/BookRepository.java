package org.fungover.jee2025.persistence;

import jakarta.data.repository.*;
import org.fungover.jee2025.entity.Book;

import java.util.Optional;
import java.util.stream.Stream;


//https://thorben-janssen.com/getting-started-with-jakarta-data/

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    // Grundläggande CRUD-operationer finns redan via CrudRepository
    @Find
    Optional<Book> findByTitle(String title);

    @Query("select b from Book b where pageCount > :pageCount")
    Stream<Book> findByPageCountGreaterThan(int pageCount);

}
