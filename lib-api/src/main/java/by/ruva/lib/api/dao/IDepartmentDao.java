package by.ruva.lib.api.dao;

import by.ruva.lib.entities.Department;

public interface IDepartmentDao extends IAGenericDao<Department> {

    Department getByName(String name);
}
