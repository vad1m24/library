package by.ruva.lib.api.dao;

import java.util.List;

import by.ruva.lib.entities.Feedback;

public interface IFeedbackDao extends IAGenericDao<Feedback> {

    List<Feedback> getAllFeedbacksByBookId(Long id);

    List<Feedback> getAllFeedbacksByUserId(Long id);
}
