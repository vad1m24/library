package by.ruva.lib.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import by.ruva.lib.api.dto.BookDetailsDto;
import by.ruva.lib.api.exceptions.NoSuchBookException;
import by.ruva.lib.entities.Book;

public interface IBookDetailsService {

    List<BookDetailsDto> getAllBookDetails();

    BookDetailsDto getBookDetailsById(Long id);

    void deleteBookDetailsById(Long id);

    BookDetailsDto createBookDetails(String isbn) throws NoSuchBookException;

    void updateBookDetails(Book existingBook, BookDetailsDto newbookDetailsDto, MultipartFile file);
}