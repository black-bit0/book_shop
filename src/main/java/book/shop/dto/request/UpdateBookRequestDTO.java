package book.shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateBookRequestDTO {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private int quantity;
}
