package by.ruva.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import by.ruva.lib.api.dao.IOrderDao;
import by.ruva.lib.entities.Order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OrderDaoTest {

    private static final LocalDate TEST_DATE = LocalDate.now();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IOrderDao orderDao;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(orderDao).isNotNull();
        assertThat(entityManager).isNotNull();
    }

    @Test
    public void getById() {
        Long id = entityManager.persistAndGetId(createOrder(TEST_DATE), Long.class);
        Order orderInRep = orderDao.get(id);
        assertThat(orderInRep.getId().equals(id)).isTrue();
    }

    @Test
    public void getAll() {
        entityManager.persist(createOrder(TEST_DATE));
        entityManager.persist(createOrder(TEST_DATE));
        entityManager.persist(createOrder(TEST_DATE));
        List<Order> allOrdersInRep = orderDao.getAll();
        assertThat(allOrdersInRep.size() == 3).isTrue();
    }

    @Test
    public void update() {
        Long id = entityManager.persistAndGetId(createOrder(TEST_DATE), Long.class);
        Order orderInRep = orderDao.get(id);
        orderInRep.setProlonged(true);
        entityManager.merge(orderInRep);
        assertThat(orderInRep.isProlonged() == true).isTrue();
    }

    @Test
    public void delete() {
        Order order = entityManager.persist(createOrder(TEST_DATE));
        entityManager.remove(order);
        assertThat(orderDao.getAll().isEmpty()).isTrue();
    }

    private Order createOrder(LocalDate testDate) {
        Order order = new Order();
        order.setOrderDate(testDate);
        return order;
    }
}
