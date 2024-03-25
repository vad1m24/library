package by.ruva.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import by.ruva.lib.api.dao.IFeedbackDao;
import by.ruva.lib.entities.Feedback;

@DataJpaTest
@RunWith(SpringRunner.class)
public class FeedbackDaoTest {

	private static final String TEST_COMMENT = "I love java";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IFeedbackDao feedbackDao;

	@Test
	public void injectedComponentsAreNotNull() {
		assertThat(feedbackDao).isNotNull();
		assertThat(entityManager).isNotNull();
	}

	@Test
	public void getById() {
		Long id = entityManager.persistAndGetId(createFeedback(TEST_COMMENT), Long.class);
		Feedback feedbackInRep = feedbackDao.get(id);
		assertThat(feedbackInRep.getId().equals(id)).isTrue();
	}

	@Test
	public void getAll() {
		entityManager.persist(createFeedback(TEST_COMMENT));
		entityManager.persist(createFeedback(TEST_COMMENT + 1));
		entityManager.persist(createFeedback(TEST_COMMENT + 2));
		List<Feedback> allFeedbacksInRep = feedbackDao.getAll();
		assertThat(allFeedbacksInRep.size() == 3).isTrue();
	}

	@Test
	public void update() {
		Long id = entityManager.persistAndGetId(createFeedback(TEST_COMMENT), Long.class);
		Feedback feedbackInRep = feedbackDao.get(id);
		feedbackInRep.setUserName("Nastia");
		entityManager.merge(feedbackInRep);
		assertThat(feedbackInRep.getUserName() == "Nastia").isTrue();
	}

	@Test
	public void delete() {
		Feedback feedback = entityManager.persist(createFeedback(TEST_COMMENT));
		entityManager.remove(feedback);
		assertThat(feedbackDao.getAll().isEmpty()).isTrue();
	}

	private Feedback createFeedback(String comment) {
		Feedback feedback = new Feedback();
		feedback.setComment(comment);
		return feedback;
	}
}