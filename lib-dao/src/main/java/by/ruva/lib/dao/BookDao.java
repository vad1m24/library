package by.ruva.lib.dao;

import by.ruva.lib.api.dao.IBookDao;
import by.ruva.lib.entities.AEntity_;
import by.ruva.lib.entities.Book;
import by.ruva.lib.entities.Book_;
import by.ruva.lib.entities.Department;

import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import java.util.Collections;
import java.util.List;

@Repository
public class BookDao extends AGenericDao<Book> implements IBookDao {

    public BookDao() {
        super(Book.class);
    }

    @Override
    public Book getByIsbn(String isbn) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(getGenericClass());
            Root<Book> rootEntry = cq.from(Book.class);
            CriteriaQuery<Book> all = cq.select(rootEntry).where(cb.equal(rootEntry.get(Book_.isbn), isbn));
            TypedQuery<Book> result = entityManager.createQuery(all);
            return result.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Book> getBooksByDepartmentId(Long id) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);
            Root<Book> rootEntry = cq.from(Book.class);
            Join<Book, Department> departmentJoin = rootEntry.join(Book_.departments);
            cq.select(rootEntry).where(cb.equal(departmentJoin.get(AEntity_.id), id));
            TypedQuery<Book> query = entityManager.createQuery(cq);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}