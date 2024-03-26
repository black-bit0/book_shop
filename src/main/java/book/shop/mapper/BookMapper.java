package book.shop.mapper;

import book.shop.dto.request.CreateBookRequestDTO;
import book.shop.dto.request.DeleteBookRequestDTO;
import book.shop.dto.request.UpdateBookRequestDTO;
import book.shop.dto.response.BookResponseDTO;
import book.shop.generated.CreateBookRequest;
import book.shop.generated.DeleteBookRequest;
import book.shop.generated.UpdateBookRequest;
import book.shop.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    CreateBookRequest map(CreateBookRequestDTO dto);

    UpdateBookRequest map(UpdateBookRequestDTO dto);

    BookResponseDTO map(Book entity);

    DeleteBookRequest map(DeleteBookRequestDTO dto);
}