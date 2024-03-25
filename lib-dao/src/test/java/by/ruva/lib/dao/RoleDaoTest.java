package by.ruva.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import by.ruva.lib.api.dao.IRoleDao;
import by.ruva.lib.entities.Role;

@DataJpaTest
@RunWith(SpringRunner.class)
public class RoleDaoTest {

	private static final String TEST_NAME = "TestName";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IRoleDao roleDao;

	@Test
	public void injectedComponentsAreNotNull() {
		assertThat(roleDao).isNotNull();
		assertThat(entityManager).isNotNull();
	}

	@Test
	public void getById() {
		Long id = entityManager.persistAndGetId(createRole(TEST_NAME), Long.class);
		Role roleInRep = roleDao.get(id);
		assertThat(roleInRep.getId().equals(id)).isTrue();
	}

	@Test
	public void getAll() {
		entityManager.persist(createRole(TEST_NAME));
		entityManager.persist(createRole(TEST_NAME + 1));
		entityManager.persist(createRole(TEST_NAME + 2));
		List<Role> allRolesInRep = roleDao.getAll();
		assertThat(allRolesInRep.size() == 3).isTrue();
	}

	@Test
	public void update() {
		Long id = entityManager.persistAndGetId(createRole(TEST_NAME), Long.class);
		Role roleInRep = roleDao.get(id);
		roleInRep.setName("User");
		entityManager.merge(roleInRep);
		assertThat(roleInRep.getName() == "User").isTrue();
	}

	@Test
	public void delete() {
		Role role = entityManager.persist(createRole(TEST_NAME));
		entityManager.remove(role);
		assertThat(roleDao.getAll().isEmpty()).isTrue();
	}

	private Role createRole(String name) {
		Role role = new Role();
		role.setName(name);
		return role;
	}
}
