package by.ruva.lib.api.service;

import java.util.List;

import by.ruva.lib.api.dto.RoleDto;

public interface IRoleService {

    List<RoleDto> getAllRoles();

    RoleDto getRoleById(Long id);

    void deleteRoleById(Long id);

    RoleDto updateRole(Long id, RoleDto dto);

    RoleDto createRole(RoleDto roleDto);

    RoleDto getRoleByName(String name);
}
