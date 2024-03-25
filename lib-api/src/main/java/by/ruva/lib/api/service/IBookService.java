package by.ruva.lib.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import by.ruva.lib.api.dto.BookDto;
import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.NoSuchBookException;

public interface IBookService {

    List<BookDto> getAllBooks();

    BookDto getBookById(Long id) throws EntityNotFoundException;

    BookDto createBook(BookDto bookDto, DepartmentDto departmentDto) throws EntityNotFoundException, NoSuchBookException;

    BookDto updateBook(BookDto bookDto, MultipartFile file) throws EntityNotFoundException;

    void deleteBookById(Long id, DepartmentDto departmentDto) throws EntityNotFoundException;

    BookDto getBookByIsbn(String isbn) throws EntityNotFoundException;

    List<BookDto> getBooksByDepartmentId(Long id) throws EntityNotFoundException;

    void countAndSetAvgRatingForBook(Long bookId);
}
