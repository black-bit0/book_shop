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
import book.shop.mapper.BookMapper;
import book.shop.observer.UniversalStreamObserver;
import book.shop.services.impl.BookServiceImpl;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Qualifier("bookServiceImpl")
    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/get-all")
    public List<BookResponseDTO> getAllBooks() {
        List<book.shop.models.Book> allBooks = bookService.getAllBooks();
        return allBooks.stream()
                .map(BookMapper.INSTANCE::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable UUID id) {
        Optional<book.shop.models.Book> bookOptional = bookService.getBookById(id);
        return bookOptional.map(BookMapper.INSTANCE::map).orElse(null);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createBook(@RequestBody CreateBookRequestDTO requestDTO) {
        try {
            CreateBookRequest request = BookMapper.INSTANCE.map(requestDTO);
            StreamObserver<Book> universalObserver = new UniversalStreamObserver<>();
            bookService.createBook(request, universalObserver);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateBook(@RequestBody UpdateBookRequestDTO requestDTO) {
        try {
            UpdateBookRequest request = BookMapper.INSTANCE.map(requestDTO);
            StreamObserver<Book> universalObserver = new UniversalStreamObserver<>();
            bookService.updateBook(request, universalObserver);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable UUID id) {
        DeleteBookRequestDTO requestDTO = new DeleteBookRequestDTO(id.toString());
        DeleteBookRequest request = BookMapper.INSTANCE.map(requestDTO);
        StreamObserver<DeleteBookResponse> universalObserver = new UniversalStreamObserver<>();
        bookService.deleteBook(request, universalObserver);
    }
}
