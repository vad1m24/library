package by.ruva.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import by.ruva.lib.api.dao.IBookDao;
import by.ruva.lib.entities.Book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
public class BookDaoTest {

    private static final String TEST_ISBN = "9780333630457";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IBookDao bookDao;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(bookDao).isNotNull();
        assertThat(entityManager).isNotNull();
    }

    @Test
    public void getById() {
        Long id = entityManager.persistAndGetId(createBook(TEST_ISBN), Long.class);
        Book bookInRep = bookDao.get(id);
        assertThat(bookInRep.getId().equals(id)).isTrue();
    }

    @Test
    public void getAll() {
        entityManager.persist(createBook(TEST_ISBN));
        entityManager.persist(createBook(TEST_ISBN + 1));
        entityManager.persist(createBook(TEST_ISBN + 2));
        List<Book> allBooksInRep = bookDao.getAll();
        assertThat(allBooksInRep.size() == 3).isTrue();
    }

    @Test
    public void update() {
        Long id = entityManager.persistAndGetId(createBook(TEST_ISBN), Long.class);
        Book bookInRep = bookDao.get(id);
        bookInRep.setQuantityInLibrary(999);
        entityManager.merge(bookInRep);
        assertThat(bookInRep.getQuantityInLibrary() == 999).isTrue();
    }

    @Test
    public void delete() {
        Book book = entityManager.persist(createBook(TEST_ISBN));
        entityManager.remove(book);
        assertThat(bookDao.getAll().isEmpty()).isTrue();
    }

    @Test
    public void getByIsbn() {
        entityManager.persist(createBook(TEST_ISBN));
        Book bookInRep = bookDao.getByIsbn(TEST_ISBN);
        assertThat(bookInRep.getIsbn().equals(TEST_ISBN)).isTrue();
    }

    private Book createBook(String isbn) {
        Book book = new Book();
        book.setIsbn(isbn);
        return book;
    }
}
