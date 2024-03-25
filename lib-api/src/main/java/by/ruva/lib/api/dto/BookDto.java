package by.ruva.lib.api.dto;

import java.util.List;

import by.ruva.lib.entities.Department;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDto extends ADto {

    private String isbn;
    private int quantityAvailable;
    private int quantityInLibrary;
    private double rating;
    private List<Department> departments;
    private BookDetailsDto bookDetailsDto;
}
