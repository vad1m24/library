package by.ruva.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import by.ruva.lib.api.dao.IDepartmentDao;
import by.ruva.lib.entities.Department;

@DataJpaTest
@RunWith(SpringRunner.class)
public class DepartmentDaoTest {

	private static final String TEST_NAME = "TestName";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IDepartmentDao departmentDao;

	@Test
	public void injectedComponentsAreNotNull() {
		assertThat(departmentDao).isNotNull();
		assertThat(entityManager).isNotNull();
	}

	@Test
	public void getById() {
		Long id = entityManager.persistAndGetId(createDepartment(TEST_NAME), Long.class);
		Department departmentInRep = departmentDao.get(id);
		assertThat(departmentInRep.getId().equals(id)).isTrue();
	}

	@Test
	public void getAll() {
		entityManager.persist(createDepartment(TEST_NAME));
		entityManager.persist(createDepartment(TEST_NAME + 1));
		entityManager.persist(createDepartment(TEST_NAME + 2));
		List<Department> allDepartmentsInRep = departmentDao.getAll();
		assertThat(allDepartmentsInRep.size() == 3).isTrue();
	}

	@Test
	public void update() {
		Long id = entityManager.persistAndGetId(createDepartment(TEST_NAME), Long.class);
		Department departmentInRep = departmentDao.get(id);
		departmentInRep.setName(TEST_NAME + 1);
		entityManager.merge(departmentInRep);
		assertThat(departmentInRep.getName() == (TEST_NAME + 1)).isTrue();
	}

	@Test
	public void delete() {
		Department department = entityManager.persist(createDepartment(TEST_NAME));
		entityManager.remove(department);
		assertThat(departmentDao.getAll().isEmpty()).isTrue();
	}

	@Test
	public void getByName() {
		entityManager.persist(createDepartment(TEST_NAME));
		Department departmentInRep = departmentDao.getByName(TEST_NAME);
		assertThat(departmentInRep.getName().equals(TEST_NAME)).isTrue();
	}

	private Department createDepartment(String name) {
		Department department = new Department();
		department.setName(name);
		return department;
	}
}