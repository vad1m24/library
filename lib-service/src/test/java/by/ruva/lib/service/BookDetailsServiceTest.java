package by.ruva.lib.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.ruva.lib.api.dto.BookDetailsDto;
import by.ruva.lib.api.exceptions.NoSuchBookException;
import by.ruva.lib.dao.BookDetailsDao;
import by.ruva.lib.entities.Book;
import by.ruva.lib.entities.BookDetails;
import by.ruva.lib.utils.mappers.BookDetailsMapper;
import by.ruva.lib.web.WebScraper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BookDetailsServiceTest {

    private static final String TEST_NAME = "name";

    @InjectMocks
    BookDetailsService bookDetailsService;

    @Mock
    BookDetailsDao bookDetailsDao;

    @Mock
    WebScraper webScraper;

    @Mock
    BookDetailsMapper bookDetailsMapper;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(bookDetailsDao).isNotNull();
        assertThat(webScraper).isNotNull();
        assertThat(bookDetailsMapper).isNotNull();
    }

    @Test
    public void getAllBookDetailsTest() {
        List<BookDetails> listBookDetails = new ArrayList<>();
        BookDetails bookDetails = createBookDetails(1L);
        BookDetails bookDetails2 = createBookDetails(2L);
        BookDetails bookDetails3 = createBookDetails(3L);
        listBookDetails.add(bookDetails);
        listBookDetails.add(bookDetails2);
        listBookDetails.add(bookDetails3);
        when(bookDetailsDao.getAll()).thenReturn(listBookDetails);
        bookDetailsService.getAllBookDetails();
        verify(bookDetailsMapper, times(1)).toListDto(listBookDetails);
    }

    @Test
    public void createBookDetailsTest() throws NoSuchBookException {
        BookDetails bookDetails = createBookDetails(1L);
        when(webScraper.getBookDetailsFromWeb(TEST_NAME)).thenReturn(bookDetails);
        bookDetailsService.createBookDetails(TEST_NAME);
        verify(webScraper, times(1)).getBookDetailsFromWeb(TEST_NAME);
        verify(bookDetailsMapper, times(1)).toDto(any(BookDetails.class));
    }

    @Test
    public void getBookDetailsByIdTest() {
        BookDetails bookDetails = createBookDetails(1L);
        when(bookDetailsDao.get(1L)).thenReturn(bookDetails);
        bookDetailsService.getBookDetailsById(1L);
        verify(bookDetailsMapper, times(1)).toDto(any(BookDetails.class));
        assertThat(bookDetailsDao.get(1L).getName() == bookDetails.getName()).isTrue();
    }

    @Test
    public void deleteBookDetailsByIdTest() {
        BookDetails bookDetails = createBookDetails(1L);
        when(bookDetailsDao.get(1L)).thenReturn(bookDetails);
        bookDetailsService.deleteBookDetailsById(1L);
        verify(bookDetailsDao, times(1)).delete(bookDetails);
    }

    @Test
    public void updateBookDetails() {
        Book book = new Book();
        book.setId(1L);
        BookDetails bookDetails = createBookDetails(1L);
        book.setBookDetails(bookDetails);
        BookDetailsDto newbookDetailsDto = new BookDetailsDto();
        newbookDetailsDto.setName(TEST_NAME + "new");
        MultipartFile fichier = new MockMultipartFile("fileThatDoesNotExists.txt", "fileThatDoesNotExists.txt",
                "text/plain", "This is a dummy file content".getBytes(StandardCharsets.UTF_8));
        bookDetailsService.updateBookDetails(book, newbookDetailsDto, fichier);
        verify(bookDetailsDao, times(1)).update(bookDetails);
    }

    private BookDetails createBookDetails(Long id) {
        BookDetails bookDetails = new BookDetails();
        bookDetails.setId(id);
        bookDetails.setName("name");
        bookDetails.setAuthor("author");
        bookDetails.setDescription("description");
        return bookDetails;
    }
}