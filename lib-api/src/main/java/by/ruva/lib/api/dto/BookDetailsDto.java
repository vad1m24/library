package by.ruva.lib.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDetailsDto extends ADto {

    private String name;
    private String author;
    private String description;
    private String picture;
}
