package by.ruva.lib.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackDto extends ADto {

    private Long bookId;
    private String bookName;
    private UserDto userDto;
    private int rating;
    private String userName;
    private String comment;
}
