package org.fungover.jee2025;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.fungover.jee2025.entity.Book;


//https://thorben-janssen.com/getting-started-with-jakarta-data/

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    // Grundläggande CRUD-operationer finns redan via CrudRepository
}
