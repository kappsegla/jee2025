package org.fungover.jee2025.persistence;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;
import org.fungover.jee2025.entity.Book;

import java.util.Optional;


//https://thorben-janssen.com/getting-started-with-jakarta-data/

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    // Grundläggande CRUD-operationer finns redan via CrudRepository
    @Find
    Optional<Book> findByTitle(String title);
}
