package by.ruva.lib.api.service;

import java.util.List;

import by.ruva.lib.api.dto.FeedbackDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;

public interface IFeedbackService {

    List<FeedbackDto> getAllFeedbacks();

    FeedbackDto getFeedbackById(Long id) throws EntityNotFoundException;

    void deleteFeedbackById(Long id) throws EntityNotFoundException;

    FeedbackDto updateFeedback(Long id, FeedbackDto dto) throws EntityNotFoundException;

    FeedbackDto createFeedback(FeedbackDto feedbackDto, Long id) throws EntityNotFoundException;

    List<FeedbackDto> getAllFeedbacksByBookId(Long bookId) throws EntityNotFoundException;

    List<FeedbackDto> getAllFeedbacksByUserId(Long principalId) throws EntityNotFoundException;
}