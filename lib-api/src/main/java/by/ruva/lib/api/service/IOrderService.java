package by.ruva.lib.api.service;

import java.util.List;

import by.ruva.lib.api.dto.OrderDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.IsAlreadyClosedException;
import by.ruva.lib.api.exceptions.IsAlreadyProlongedException;
import by.ruva.lib.api.exceptions.NoBooksAvailableException;

public interface IOrderService {

    List<OrderDto> getAllOrders();

    OrderDto getOrderById(Long id) throws EntityNotFoundException;

    void deleteOrderById(Long id);

    OrderDto createOrder(Long bookId, String userName) throws NoBooksAvailableException, EntityNotFoundException;

    OrderDto closeOrder(Long id) throws IsAlreadyClosedException, EntityNotFoundException;

    OrderDto prolongOrder(Long id)
            throws EntityNotFoundException, IsAlreadyClosedException, IsAlreadyProlongedException;

    List<OrderDto> getAllOrdersByUserId(Long id) throws EntityNotFoundException;
}
