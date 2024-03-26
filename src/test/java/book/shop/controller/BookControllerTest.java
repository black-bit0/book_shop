package book.shop.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import book.shop.dto.request.CreateBookRequestDTO;
import book.shop.dto.request.UpdateBookRequestDTO;
import book.shop.dto.response.BookResponseDTO;
import book.shop.models.Book;
import book.shop.services.impl.BookServiceImpl;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.junit.jupiter.CitrusExtension;
import com.consol.citrus.http.client.HttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({SpringExtension.class, CitrusExtension.class})
@TestPropertySource(locations = "classpath:test.properties")
@Testcontainers
class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    @MockBean
    private HttpClient bookClient;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("root");

    @Test
    void testGetAllBooksEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/books/get-all", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @CitrusTest
    void testCreateBookEndpoint() {
        // Define request payload
        String requestPayload = "{\"title\": \"Test Book\", \"author\": \"Test Author\", \"isbn\": \"your_isbn_value_here\"}";

        // Prepare HTTP request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare HTTP request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload, headers);

        // Define the endpoint URL
        String endpointUrl = "http://localhost:" + 8080 + "/books/create";

        // Send HTTP POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endpointUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Validate the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllBooks() {
        // Arrange
        BookServiceImpl bookService = mock(BookServiceImpl.class);
        BookController controller = new BookController(bookService);
        List<Book> mockBooks = Arrays.asList(new Book(), new Book());
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        // Act
        List<BookResponseDTO> result = controller.getAllBooks();

        // Assert
        assertEquals(mockBooks.size(), result.size());
    }

    @Test
    public void testGetBookById() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        BookServiceImpl bookService = mock(BookServiceImpl.class);
        BookController controller = new BookController(bookService);
        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setTitle("Test Title");
        mockBook.setAuthor("Test Author");
        mockBook.setIsbn("Test ISBN");
        mockBook.setQuantity(10);
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(mockBook));

        // Act
        BookResponseDTO result = controller.getBookById(bookId);

        // Assert
        assertEquals(mockBook.getTitle(), result.getTitle());
        assertEquals(mockBook.getAuthor(), result.getAuthor());
        assertEquals(mockBook.getIsbn(), result.getIsbn());
        assertEquals(mockBook.getQuantity(), result.getQuantity());
    }

    @Test
    public void testCreateBook() {
        // Arrange
        BookServiceImpl bookService = mock(BookServiceImpl.class);
        BookController controller = new BookController(bookService);
        CreateBookRequestDTO requestDTO = new CreateBookRequestDTO("Test Title", "Test Author", "Test ISBN", 10);
        ResponseEntity<HttpStatus> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        // Act
        ResponseEntity<HttpStatus> result = controller.createBook(requestDTO);

        // Assert
        assertEquals(expectedResponse.getStatusCode(), result.getStatusCode());
    }

    @Test
    public void testUpdateBook() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        BookServiceImpl bookService = mock(BookServiceImpl.class);
        BookController controller = new BookController(bookService);
        UpdateBookRequestDTO requestDTO = UpdateBookRequestDTO.builder()
                .id(bookId.toString())
                .title("Test Title")
                .author("Test Author")
                .isbn("Test ISBN")
                .quantity(10).build();
        ResponseEntity<HttpStatus> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        // Act
        ResponseEntity<HttpStatus> result = controller.updateBook(requestDTO);

        // Assert
        assertEquals(expectedResponse.getStatusCode(), result.getStatusCode());
    }
}
