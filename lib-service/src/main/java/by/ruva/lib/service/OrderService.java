package by.ruva.lib.service;

import by.ruva.lib.api.dao.IAGenericDao;
import by.ruva.lib.api.dao.IOrderDao;
import by.ruva.lib.api.dto.BookDto;
import by.ruva.lib.api.dto.OrderDto;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.IsAlreadyClosedException;
import by.ruva.lib.api.exceptions.IsAlreadyProlongedException;
import by.ruva.lib.api.exceptions.NoBooksAvailableException;
import by.ruva.lib.api.service.IBookService;
import by.ruva.lib.api.service.IOrderService;
import by.ruva.lib.api.service.IUserService;
import by.ruva.lib.api.utils.IEmailSender;
import by.ruva.lib.entities.Book;
import by.ruva.lib.entities.Order;
import by.ruva.lib.entities.User;
import by.ruva.lib.utils.mappers.AMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@EnableScheduling
@Transactional
public class OrderService implements IOrderService {

    private static final String ORDER = "Order";

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IBookService bookService;

    @Autowired
    private AMapper<Book, BookDto> bookMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private AMapper<User, UserDto> userMapper;

    @Autowired
    private IEmailSender emailSender;

    @Autowired
    private AMapper<Order, OrderDto> orderMapper;

    public IAGenericDao<Order> getOrderDao() {
        return orderDao;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderMapper.toListDto(getOrderDao().getAll());
    }

    @Override
    public OrderDto createOrder(Long bookId, String userName)
            throws NoBooksAvailableException, EntityNotFoundException {
        Order order = new Order();
        Book book = bookMapper.toEntity(bookService.getBookById(bookId));
        if (book.getQuantityAvailable() != 0) {
            book.setQuantityAvailable(book.getQuantityAvailable() - 1);
            order.setBook(book).setUser(userMapper.toEntity(userService.getUserByEmail(userName)))
                    .setOrderDate(LocalDate.now()).setDueDate(order.getOrderDate().plusDays(10)).setProlonged(false)
                    .setFinished(false);
        } else {
            throw new NoBooksAvailableException();
        }
        return orderMapper.toDto(getOrderDao().create(order));
    }

    @Override
    public OrderDto getOrderById(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(orderMapper.toDto(getOrderDao().get(id)))
                .orElseThrow(() -> new EntityNotFoundException(ORDER));
    }

    @Override
    public List<OrderDto> getAllOrdersByUserId(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(orderMapper.toListDto(orderDao.getAllOrdersByUserId(id)))
                .orElseThrow(() -> new EntityNotFoundException(ORDER));
    }

    @Override
    public void deleteOrderById(Long id) {
        getOrderDao().delete(getOrderDao().get(id));
    }

    @Override
    public OrderDto prolongOrder(Long id)
            throws EntityNotFoundException, IsAlreadyClosedException, IsAlreadyProlongedException {
        Order existingOrder = Optional.ofNullable(getOrderDao().get(id))
                .orElseThrow(() -> new EntityNotFoundException(ORDER));
        if (existingOrder.isFinished()) {
            throw new IsAlreadyClosedException();
        } else {
            if (existingOrder.isProlonged()) {
                throw new IsAlreadyProlongedException();
            } else {
                getOrderDao().update(existingOrder.setProlonged(true).setDueDate(LocalDate.now().plusDays(10)));
            }
        }
        return orderMapper.toDto(existingOrder);
    }

    @Override
    public OrderDto closeOrder(Long id) throws IsAlreadyClosedException, EntityNotFoundException {
        Order existingOrder = Optional.ofNullable(getOrderDao().get(id))
                .orElseThrow(() -> new EntityNotFoundException(ORDER));
        if (existingOrder.isFinished()) {
            throw new IsAlreadyClosedException();
        } else {
            incrementQuantity(existingOrder.setFinished(true));
            getOrderDao().update(existingOrder);
        }
        return orderMapper.toDto(existingOrder);
    }

    @Scheduled(cron = "0 40 16 * * *")
    public void checkIfOrderIsExpired() {
        for (Order order : getOrderDao().getAll()) {
            if (order.getDueDate().isBefore(LocalDate.now()) && !order.isFinished()) {
                try {
                    emailSender.sendEmailsFromAdminAboutDebts(order);
                } catch (MessagingException e) {
                    log.info("Mail not sent!");
                }
            }
            if (order.getDueDate().isEqual(LocalDate.now().plusDays(1)) && !order.isFinished()) {
                try {
                    emailSender.sendEmailsFromAdminDueDateTomorrow(order);
                } catch (MessagingException e) {
                    log.info("Mail not sent!");
                }
            }
        }
    }

    private void incrementQuantity(Order existingOrder) throws EntityNotFoundException {
        Book book = getBookByOrder(existingOrder);
        book.setQuantityAvailable(book.getQuantityAvailable() + 1);
    }

    private Book getBookByOrder(Order existingOrder) throws EntityNotFoundException {
        return bookMapper.toEntity(bookService.getBookById(existingOrder.getBook().getId()));
    }
}