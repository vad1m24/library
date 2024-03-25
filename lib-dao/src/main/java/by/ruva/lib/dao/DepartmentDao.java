package by.ruva.lib.dao;

import by.ruva.lib.api.dao.IDepartmentDao;
import by.ruva.lib.entities.Department;
import by.ruva.lib.entities.Department_;

import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class DepartmentDao extends AGenericDao<Department> implements IDepartmentDao {

    public DepartmentDao() {
        super(Department.class);
    }

    @Override
    public Department getByName(String name) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Department> cq = cb.createQuery(getGenericClass());
            Root<Department> rootEntry = cq.from(Department.class);
            CriteriaQuery<Department> all = cq.select(rootEntry).where(cb.equal(rootEntry.get(Department_.name), name));
            TypedQuery<Department> result = entityManager.createQuery(all);
            return result.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}