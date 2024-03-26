package book.shop.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import book.shop.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findBookById(UUID id);

}
