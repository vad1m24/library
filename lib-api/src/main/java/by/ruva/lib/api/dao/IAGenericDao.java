package by.ruva.lib.api.dao;

import java.util.List;

import by.ruva.lib.entities.AEntity;
// супер класс по принципу сущностей
public interface IAGenericDao<T extends AEntity> {

    Class<T> getGenericClass();

    T create(T entity);

    T get(Long id);

    void update(T entity);

    List<T> getAll();

    void delete(T entity);
}
