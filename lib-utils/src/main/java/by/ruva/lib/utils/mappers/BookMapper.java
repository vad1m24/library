package by.ruva.lib.utils.mappers;

import by.ruva.lib.api.dao.IBookDetailsDao;
import by.ruva.lib.api.dto.BookDetailsDto;
import by.ruva.lib.api.dto.BookDto;
import by.ruva.lib.entities.Book;
import by.ruva.lib.entities.BookDetails;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import javax.annotation.PostConstruct;

@Component
public class BookMapper extends AMapper<Book, BookDto> {

    @Autowired
    private IBookDetailsDao bookDetailsDao;
    @Autowired
    private AMapper<BookDetails, BookDetailsDto> bookDetailsMapper;

    public BookMapper(ModelMapper mapper, IBookDetailsDao bookDetailsDao,
            AMapper<BookDetails, BookDetailsDto> bookDetailsMapper) {
        super(Book.class, BookDto.class);
        this.bookDetailsDao = bookDetailsDao;
        this.bookDetailsMapper = bookDetailsMapper;
        this.mapper = mapper;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Book.class, BookDto.class).addMappings(m -> m.skip(BookDto::setBookDetailsDto))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(BookDto.class, Book.class).addMappings(m -> m.skip(Book::setBookDetails))
                .setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(Book source, BookDto destination) {
        destination.setBookDetailsDto(getBookDetails(source));
    }

    private BookDetailsDto getBookDetails(Book source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null
                : bookDetailsMapper.toDto(source.getBookDetails());
    }

    @Override
    void mapSpecificFields(BookDto source, Book destination) {
        destination.setBookDetails(bookDetailsDao.get(source.getBookDetailsDto().getId()));
    }
}