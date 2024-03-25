package by.ruva.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import by.ruva.lib.api.dao.IUserDao;
import by.ruva.lib.entities.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserDaoTest {

    private static final String TEST_EMAIL = "TestEmail";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IUserDao userDao;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(userDao).isNotNull();
        assertThat(entityManager).isNotNull();
    }

    @Test
    public void getById() {
        Long id = entityManager.persistAndGetId(createUser(TEST_EMAIL), Long.class);
        User userInRep = userDao.get(id);
        assertThat(userInRep.getId().equals(id)).isTrue();
    }

    @Test
    public void getAll() {
        entityManager.persist(createUser(TEST_EMAIL));
        entityManager.persist(createUser(TEST_EMAIL + 1));
        entityManager.persist(createUser(TEST_EMAIL + 2));
        List<User> allUsersInRep = userDao.getAll();
        assertThat(allUsersInRep.size() == 3).isTrue();
    }

    @Test
    public void update() {
        Long id = entityManager.persistAndGetId(createUser(TEST_EMAIL), Long.class);
        User userInRep = userDao.get(id);
        userInRep.setEmail(TEST_EMAIL);
        entityManager.merge(userInRep);
        assertThat(userInRep.getEmail() == TEST_EMAIL).isTrue();
    }

    @Test
    public void delete() {
        User user = entityManager.persist(createUser(TEST_EMAIL));
        entityManager.remove(user);
        assertThat(userDao.getAll().isEmpty()).isTrue();
    }

    @Test
    public void getByEmail() {
        entityManager.persist(createUser(TEST_EMAIL));
        User userInRep = userDao.getByEmail(TEST_EMAIL);
        assertThat(userInRep.getEmail().equals(TEST_EMAIL)).isTrue();
    }

    private User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        return user;
    }
}
