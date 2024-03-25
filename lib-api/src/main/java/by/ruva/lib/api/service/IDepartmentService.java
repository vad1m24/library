package by.ruva.lib.api.service;

import java.util.List;

import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.exceptions.DepartmentIsNotEmptyException;
import by.ruva.lib.api.exceptions.EntityNotFoundException;

public interface IDepartmentService {

    List<DepartmentDto> getAllDepartments();

    DepartmentDto getDepartmentById(Long id) throws EntityNotFoundException;

    void deleteDepartmentById(Long id) throws EntityNotFoundException, DepartmentIsNotEmptyException;

    DepartmentDto updateDepartment(Long id, DepartmentDto dto) throws EntityNotFoundException;

    DepartmentDto createDepartment(DepartmentDto departmentDto);

    DepartmentDto getDepartmentByName(String name);
}
