package by.ruva.lib.api.dao;

import java.util.List;

import by.ruva.lib.entities.User;

public interface IUserDao extends IAGenericDao<User> {
// методы генерятся автоматически из названия
    User getByEmail(String email);

    List<User> getByDepartment(String department);

    User getByName(String name);
}
