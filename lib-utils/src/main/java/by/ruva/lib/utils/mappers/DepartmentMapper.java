package by.ruva.lib.utils.mappers;

import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.entities.Department;

import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper extends AMapper<Department, DepartmentDto> {

    public DepartmentMapper() {
        super(Department.class, DepartmentDto.class);
    }
}