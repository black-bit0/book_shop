package book.shop.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import book.shop.dto.request.CreateBookRequestDTO;
import book.shop.dto.request.DeleteBookRequestDTO;
import book.shop.dto.request.UpdateBookRequestDTO;
import book.shop.dto.response.BookResponseDTO;
import book.shop.generated.Book;
import book.shop.generated.CreateBookRequest;
import book.shop.generated.DeleteBookRequest;
import book.shop.generated.DeleteBookResponse;
import book.shop.generated.UpdateBookRequest;
import book.shop.observer.UniversalStreamObserver;
import book.shop.services.impl.BookServiceImpl;
import io.grpc.stub.StreamObserver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/get-all")
    public List<BookResponseDTO> getAllBooks() {
        List<book.shop.models.Book> allBooks = bookService.getAllBooks();
        return allBooks.stream()
                .map(book -> BookResponseDTO.builder()
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .isbn(book.getIsbn())
                        .quantity(book.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable UUID id) {
        Optional<book.shop.models.Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            book.shop.models.Book book = bookOptional.get();
            return BookResponseDTO.builder()
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .quantity(book.getQuantity())
                    .build();
        }
        return BookResponseDTO.builder().build();

    }


    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createBook(@RequestBody CreateBookRequestDTO requestDTO) {
        CreateBookRequest.Builder builder = CreateBookRequest.newBuilder();
        builder.setTitle(requestDTO.getTitle());
        builder.setAuthor(requestDTO.getAuthor());
        builder.setIsbn(requestDTO.getIsbn());
        builder.setQuantity(requestDTO.getQuantity());
        CreateBookRequest request = builder.build();

        StreamObserver<Book> universalObserver = new UniversalStreamObserver<Book>();
        bookService.createBook(request, universalObserver);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateBook(@RequestBody UpdateBookRequestDTO requestDTO) {
        UpdateBookRequest.Builder builder = UpdateBookRequest.newBuilder();
        builder.setId(requestDTO.getId());
        builder.setTitle(requestDTO.getTitle());
        builder.setAuthor(requestDTO.getAuthor());
        builder.setIsbn(requestDTO.getIsbn());
        builder.setQuantity(requestDTO.getQuantity());
        UpdateBookRequest request = builder.build();

        StreamObserver<Book> universalObserver = new UniversalStreamObserver<Book>();
        bookService.updateBook(request, universalObserver);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable DeleteBookRequestDTO requestDTO) {
        DeleteBookRequest.Builder builder = DeleteBookRequest.newBuilder();
        DeleteBookRequest id = builder.setId(requestDTO.getId()).build();

        StreamObserver<DeleteBookResponse> universalObserver = new UniversalStreamObserver<DeleteBookResponse>();
        bookService.deleteBook(id, universalObserver);
    }
}
