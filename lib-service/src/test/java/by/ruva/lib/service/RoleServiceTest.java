package by.ruva.lib.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.ruva.lib.api.dto.RoleDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.dao.RoleDao;
import by.ruva.lib.entities.Role;
import by.ruva.lib.utils.mappers.RoleMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    private static final String TEST_ROLE = "ROLE_USER";

    @InjectMocks
    RoleService roleService;

    @Mock
    RoleDao roleDao;

    @Mock
    RoleMapper roleMapper;
    
    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(roleDao).isNotNull();
        assertThat(roleMapper).isNotNull();
    }

    @Test
    public void createRoleTest() {
        Role role = createRole(TEST_ROLE);
        when(roleDao.create(any(Role.class))).thenReturn(role);
        roleService.createRole(toDto(role));
        verify(roleDao, times(1)).create(any(Role.class));
        verify(roleMapper, times(1)).toDto(any(Role.class));
    }

    @Test
    public void getAllRoleTest() {
        List<Role> listRole = new ArrayList<>();
        listRole.add(createRole("1"));
        listRole.add(createRole("2"));
        listRole.add(createRole("3"));
        when(roleDao.getAll()).thenReturn(listRole);
        roleService.getAllRoles();
        verify(roleMapper, times(1)).toListDto(listRole);
    }

    @Test
    public void getRoleByIdTest() throws EntityNotFoundException {
        Role role = createRole(TEST_ROLE);
        when(roleDao.get(1L)).thenReturn(role);
        roleService.getRoleById(1L);
        verify(roleMapper, times(1)).toDto(any(Role.class));
    }

    @Test
    public void getRoleByNameTest() throws EntityNotFoundException {
        Role role = createRole(TEST_ROLE);
        when(roleDao.getByName(TEST_ROLE)).thenReturn(role);
        roleService.getRoleByName(TEST_ROLE);
        verify(roleMapper, times(1)).toDto(any(Role.class));
    }

    @Test
    public void updateRole() throws EntityNotFoundException {
        Role role = createRole(TEST_ROLE);
        when(roleDao.get(1L)).thenReturn(role);
        RoleDto dtoToUpddate = new RoleDto();
        dtoToUpddate.setName("new");
        roleService.updateRole(role.getId(), dtoToUpddate);
        verify(roleDao, times(1)).update(role);
        assertThat(roleDao.get(1L).getName() == "new").isTrue();
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setId(1L);
        role.setName(name);
        return role;
    }

    private RoleDto toDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }
}