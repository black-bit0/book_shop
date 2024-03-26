package book.shop.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import book.shop.generated.Book;

public interface BookService {
    List<book.shop.models.Book> getAllBooks();

    Optional<book.shop.models.Book> getBookById(UUID id);

}
