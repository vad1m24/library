package by.ruva.lib.utils.mappers;

import by.ruva.lib.api.dto.BookDetailsDto;
import by.ruva.lib.entities.BookDetails;

import org.springframework.stereotype.Component;

@Component
public class BookDetailsMapper extends AMapper<BookDetails, BookDetailsDto> {

    public BookDetailsMapper() {
        super(BookDetails.class, BookDetailsDto.class);
    }
}