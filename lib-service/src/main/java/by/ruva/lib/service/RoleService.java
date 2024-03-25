package by.ruva.lib.service;

import by.ruva.lib.api.dao.IAGenericDao;
import by.ruva.lib.api.dao.IRoleDao;
import by.ruva.lib.api.dto.RoleDto;
import by.ruva.lib.api.service.IRoleService;
import by.ruva.lib.entities.Role;
import by.ruva.lib.utils.mappers.AMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService implements IRoleService {

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private AMapper<Role, RoleDto> roleMapper;

    public IAGenericDao<Role> getRoleDao() {
        return roleDao;
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleMapper.toListDto(getRoleDao().getAll());
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        return roleMapper.toDto(getRoleDao().create(role));
    }

    @Override
    public RoleDto getRoleById(Long id) {
        return roleMapper.toDto(getRoleDao().get(id));
    }

    @Override
    public RoleDto getRoleByName(String name) {
        return roleMapper.toDto(roleDao.getByName(name));
    }

    @Override
    public void deleteRoleById(Long id) {
        getRoleDao().delete(getRoleDao().get(id));
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role existingRole = Optional.ofNullable(getRoleDao().get(id)).orElse(new Role());
        Optional.ofNullable(roleDto.getName()).ifPresent(existingRole::setName);
        getRoleDao().update(existingRole);
        return roleMapper.toDto(existingRole);
    }
}