package by.ruva.lib.dao;

import by.ruva.lib.api.dao.IFeedbackDao;
import by.ruva.lib.entities.AEntity_;
import by.ruva.lib.entities.Book;
import by.ruva.lib.entities.Feedback;
import by.ruva.lib.entities.Feedback_;
import by.ruva.lib.entities.User;

import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import java.util.List;

@Repository
public class FeedbackDao extends AGenericDao<Feedback> implements IFeedbackDao {

    public FeedbackDao() {
        super(Feedback.class);
    }

    public List<Feedback> getAllFeedbacksByBookId(Long id) {
        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Feedback> criteria = cBuilder.createQuery(Feedback.class);
        Root<Feedback> linkRoot = criteria.from(Feedback.class);
        Join<Feedback, Book> bookJoin = linkRoot.join(Feedback_.book);
        criteria.select(linkRoot).where(cBuilder.equal(bookJoin.get(AEntity_.id), id));
        TypedQuery<Feedback> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    public List<Feedback> getAllFeedbacksByUserId(Long id) {
        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Feedback> criteria = cBuilder.createQuery(Feedback.class);
        Root<Feedback> linkRoot = criteria.from(Feedback.class);
        Join<Feedback, User> userJoin = linkRoot.join(Feedback_.user);
        criteria.select(linkRoot).where(cBuilder.equal(userJoin.get(AEntity_.id), id));
        TypedQuery<Feedback> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }
}
