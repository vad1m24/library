package by.ruva.lib.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.exceptions.DepartmentIsNotEmptyException;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.NoSuchBookException;
import by.ruva.lib.dao.DepartmentDao;
import by.ruva.lib.entities.Department;
import by.ruva.lib.utils.mappers.DepartmentMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    @InjectMocks
    DepartmentService departmentService;

    @Mock
    DepartmentDao departmentDao;

    @Mock
    BookService bookService;

    @Mock
    DepartmentMapper departmentMapper;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(departmentDao).isNotNull();
        assertThat(departmentMapper).isNotNull();
    }

    @Test
    public void getAllDepartmentTest() {
        List<Department> listDepartment = new ArrayList<>();
        listDepartment.add(createDepartment("1"));
        listDepartment.add(createDepartment("2"));
        listDepartment.add(createDepartment("3"));
        when(departmentDao.getAll()).thenReturn(listDepartment);
        departmentService.getAllDepartments();
        verify(departmentMapper, times(1)).toListDto(listDepartment);
    }

    @Test
    public void getDepartmentByIdTest() throws EntityNotFoundException {
        Department department = createDepartment("name");
        when(departmentDao.get(1L)).thenReturn(department);
        when(departmentMapper.toDto(any(Department.class))).thenReturn(toDto(department));
        DepartmentDto newdepartment = departmentService.getDepartmentById(1L);
        verify(departmentMapper, times(1)).toDto(any(Department.class));
        assertThat(newdepartment.getName() == "name").isTrue();
    }

    @Test
    public void getDepartmentByNameTest() throws EntityNotFoundException {
        Department department = createDepartment("name");
        when(departmentDao.getByName("name")).thenReturn(department);
        when(departmentMapper.toDto(any(Department.class))).thenReturn(toDto(department));
        DepartmentDto newdepartment = departmentService.getDepartmentByName("name");
        verify(departmentMapper, times(1)).toDto(any(Department.class));
        assertThat(newdepartment.getName() == "name").isTrue();
    }

    @Test
    public void updateDepartment() throws EntityNotFoundException {
        Department department = createDepartment("name");
        when(departmentDao.get(any(Long.class))).thenReturn(department);
        departmentService.updateDepartment(1L, toDto(department));
        verify(departmentDao, times(1)).update(department);
    }

    @Test
    public void createDepartmentTest() throws NoSuchBookException {
        Department department = createDepartment("1L");
        when(departmentDao.create(any(Department.class))).thenReturn(department);
        departmentService.createDepartment(toDto(department));
        verify(departmentDao, times(1)).create(any(Department.class));
        verify(departmentMapper, times(1)).toDto(any(Department.class));
    }

    @Test
    public void deleteDepartmentByIdTest() throws EntityNotFoundException, DepartmentIsNotEmptyException {
        Department department = createDepartment("1L");
        when(departmentDao.get(1L)).thenReturn(department);
        when(bookService.getBooksByDepartmentId(1L)).thenReturn(null);
        departmentService.deleteDepartmentById(1L);
        verify(departmentDao, times(1)).delete(department);
    }

    private Department createDepartment(String name) {
        Department department = new Department();
        department.setId(1L);
        department.setName(name);
        return department;
    }

    private DepartmentDto toDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }
}