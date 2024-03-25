package by.ruva.lib.dao;

import by.ruva.lib.api.dao.IOrderDao;
import by.ruva.lib.entities.AEntity_;
import by.ruva.lib.entities.Order;
import by.ruva.lib.entities.Order_;
import by.ruva.lib.entities.User;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Repository
public class OrderDao extends AGenericDao<Order> implements IOrderDao {

    public OrderDao() {
        super(Order.class);
    }

    public List<Order> getAllOrdersByUserId(Long userId) {
        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cBuilder.createQuery(Order.class);
        Root<Order> linkRoot = criteria.from(Order.class);
        Join<Order, User> userJoin = linkRoot.join(Order_.user);
        criteria.select(linkRoot);
        criteria.where(cBuilder.equal(userJoin.get(AEntity_.id), userId));
        TypedQuery<Order> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }
}
