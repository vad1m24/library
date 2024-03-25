package by.ruva.lib.api.dao;

import by.ruva.lib.entities.Role;

public interface IRoleDao extends IAGenericDao<Role> {

    Role getByName(String name);
}
