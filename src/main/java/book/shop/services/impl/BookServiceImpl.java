package book.shop.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import book.shop.generated.Book;
import book.shop.generated.BookServiceGrpc;
import book.shop.generated.CreateBookRequest;
import book.shop.generated.DeleteBookRequest;
import book.shop.generated.DeleteBookResponse;
import book.shop.generated.ReadBookRequest;
import book.shop.generated.UpdateBookRequest;
import book.shop.repositories.BookRepository;
import book.shop.services.BookService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("bookServiceImpl")
public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase implements BookService {
    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<Book> responseObserver) {
        book.shop.models.Book entity = book.shop.models.Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .quantity(request.getQuantity())
                .build();

        repository.save(entity);

        Book response = Book.newBuilder()
                .setId(entity.getId().toString())
                .setTitle(entity.getTitle())
                .setAuthor(entity.getAuthor())
                .setIsbn(entity.getIsbn())
                .setQuantity(entity.getQuantity())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void readBook(ReadBookRequest request, StreamObserver<Book> responseObserver) {
        String bookId = request.getId();

        Optional<book.shop.models.Book> optionalBook = repository.findById(bookId);

        if (optionalBook.isPresent()) {
            book.shop.models.Book bookEntity = optionalBook.get();
            Book book = Book.newBuilder()
                    .setId(bookEntity.getId().toString())
                    .setTitle(bookEntity.getTitle())
                    .setAuthor(bookEntity.getAuthor())
                    .setIsbn(bookEntity.getIsbn())
                    .setQuantity(bookEntity.getQuantity())
                    .build();

            responseObserver.onNext(book);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asException());
        }
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<Book> responseObserver) {
        String bookId = request.getId();
        Optional<book.shop.models.Book> optionalBook = repository.findById(bookId);

        if (optionalBook.isPresent()) {
            book.shop.models.Book bookEntity = optionalBook.get();
            bookEntity.setTitle(request.getTitle());
            bookEntity.setAuthor(request.getAuthor());
            bookEntity.setIsbn(request.getIsbn());
            bookEntity.setQuantity(request.getQuantity());

            repository.save(bookEntity);

            Book updatedBook = Book.newBuilder()
                    .setId(bookEntity.getId().toString())
                    .setTitle(bookEntity.getTitle())
                    .setAuthor(bookEntity.getAuthor())
                    .setIsbn(bookEntity.getIsbn())
                    .setQuantity(bookEntity.getQuantity())
                    .build();

            responseObserver.onNext(updatedBook);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asException());
        }
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {
        String bookId = request.getId();
        Optional<book.shop.models.Book> optionalBook = repository.findById(bookId);

        if (optionalBook.isPresent()) {
            repository.deleteById(bookId);
            responseObserver.onNext(DeleteBookResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(DeleteBookResponse.newBuilder().setSuccess(false).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public List<book.shop.models.Book> getAllBooks() {
        return repository.findAll();
    }

    @Override
    public Optional<book.shop.models.Book> getBookById(UUID id) {
        return repository.findBookById(id);
    }
}
