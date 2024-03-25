package by.ruva.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import by.ruva.lib.api.dao.IBookDetailsDao;
import by.ruva.lib.entities.BookDetails;

@DataJpaTest
@RunWith(SpringRunner.class)
public class BookDetailsDaoTest {

	private static final String TEST_NAME = "i love java";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IBookDetailsDao bookDetailsDao;

	@Test
	public void injectedComponentsAreNotNull() {
		assertThat(bookDetailsDao).isNotNull();
		assertThat(entityManager).isNotNull();
	}

	@Test
	public void getById() {
		Long id = entityManager.persistAndGetId(createBookDetails(TEST_NAME), Long.class);
		BookDetails bookDetailsInRep = bookDetailsDao.get(id);
		assertThat(bookDetailsInRep.getId().equals(id)).isTrue();
	}

	@Test
	public void getAll() {
		entityManager.persist(createBookDetails(TEST_NAME));
		entityManager.persist(createBookDetails(TEST_NAME + 1));
		entityManager.persist(createBookDetails(TEST_NAME + 2));
		List<BookDetails> allBookDetailssInRep = bookDetailsDao.getAll();
		assertThat(allBookDetailssInRep.size() == 3).isTrue();
	}

	@Test
	public void update() {
		Long id = entityManager.persistAndGetId(createBookDetails(TEST_NAME), Long.class);
		BookDetails bookDetailsInRep = bookDetailsDao.get(id);
		bookDetailsInRep.setAuthor(TEST_NAME);
		entityManager.merge(bookDetailsInRep);
		assertThat(bookDetailsInRep.getAuthor() == TEST_NAME).isTrue();
	}

	@Test
	public void delete() {
		BookDetails bookDetails = entityManager.persist(createBookDetails(TEST_NAME));
		entityManager.remove(bookDetails);
		assertThat(bookDetailsDao.getAll().isEmpty()).isTrue();
	}

	private BookDetails createBookDetails(String name) {
		BookDetails bookDetails = new BookDetails();
		bookDetails.setName(name);
		return bookDetails;
	}
}