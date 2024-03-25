package by.ruva.lib.service;

import by.ruva.lib.api.dao.IAGenericDao;
import by.ruva.lib.api.dao.IFeedbackDao;
import by.ruva.lib.api.dto.FeedbackDto;
import by.ruva.lib.api.dto.OrderDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.service.IBookService;
import by.ruva.lib.api.service.IFeedbackService;
import by.ruva.lib.api.service.IOrderService;
import by.ruva.lib.entities.Feedback;
import by.ruva.lib.entities.Order;
import by.ruva.lib.utils.mappers.AMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService implements IFeedbackService {

    private static final String FEEDBACK = "Feedback";

    @Autowired
    private IFeedbackDao feedbackDao;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private AMapper<Order, OrderDto> orderMapper;

    @Autowired
    private IBookService bookService;

    @Autowired
    private AMapper<Feedback, FeedbackDto> feedbackMapper;

    public IAGenericDao<Feedback> getFeedbackDao() {
        return feedbackDao;
    }

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackMapper.toListDto(getFeedbackDao().getAll());
    }

    @Override
    public FeedbackDto createFeedback(FeedbackDto feedbackDto, Long orderId) throws EntityNotFoundException {
        Order order = orderMapper.toEntity(orderService.getOrderById(orderId));
        Feedback feedback = new Feedback().setBook(order.getBook()).setUser(order.getUser())
                .setRating(feedbackDto.getRating()).setUserName(order.getUser().getUsername())
                .setBookName(order.getBook().getBookDetails().getName()).setComment(feedbackDto.getComment());
        getFeedbackDao().create(feedback);
        bookService.countAndSetAvgRatingForBook(order.getBook().getId());
        return feedbackMapper.toDto(feedback);
    }

    @Override
    public FeedbackDto getFeedbackById(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(feedbackMapper.toDto(getFeedbackDao().get(id)))
                .orElseThrow(() -> new EntityNotFoundException(FEEDBACK));
    }

    @Override
    public FeedbackDto updateFeedback(Long id, FeedbackDto feedbackDto) throws EntityNotFoundException {
        Feedback existingFeedback = Optional.ofNullable(getFeedbackDao().get(id))
                .orElseThrow(() -> new EntityNotFoundException(FEEDBACK));
        existingFeedback.setRating(feedbackDto.getRating()).setComment(feedbackDto.getComment());
        getFeedbackDao().update(existingFeedback);
        bookService.countAndSetAvgRatingForBook(getBookIdByFeedbackId(id));
        return feedbackMapper.toDto(existingFeedback);
    }

    @Override
    public void deleteFeedbackById(Long id) throws EntityNotFoundException {
        Long bookId = getBookIdByFeedbackId(id);
        getFeedbackDao().delete(getFeedbackDao().get(id));
        bookService.countAndSetAvgRatingForBook(bookId);
    }

    @Override
    public List<FeedbackDto> getAllFeedbacksByBookId(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(feedbackMapper.toListDto(feedbackDao.getAllFeedbacksByBookId(id)))
                .orElseThrow(() -> new EntityNotFoundException(FEEDBACK));
    }

    @Override
    public List<FeedbackDto> getAllFeedbacksByUserId(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(feedbackMapper.toListDto(feedbackDao.getAllFeedbacksByUserId(id)))
                .orElseThrow(() -> new EntityNotFoundException(FEEDBACK));
    }

    private Long getBookIdByFeedbackId(Long id) {
        return getFeedbackDao().get(id).getBook().getId();
    }
}