package by.ruva.lib.api.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDto extends ADto {

    private UserDto userDto;
    private BookDto bookDto;
    private LocalDate orderDate;
    private LocalDate dueDate;
    private boolean isFinished;
    private boolean isProlonged;

    public boolean getIsFinished() {
        return isFinished;
    }

    public boolean getIsProlonged() {
        return isProlonged;
    }
}
