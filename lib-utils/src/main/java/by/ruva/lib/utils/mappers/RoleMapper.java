package by.ruva.lib.utils.mappers;

import by.ruva.lib.api.dto.RoleDto;
import by.ruva.lib.entities.Role;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper extends AMapper<Role, RoleDto> {

    public RoleMapper() {
        super(Role.class, RoleDto.class);
    }
}