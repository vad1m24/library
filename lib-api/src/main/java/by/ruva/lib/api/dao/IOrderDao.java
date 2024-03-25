package by.ruva.lib.api.dao;

import java.util.List;

import by.ruva.lib.entities.Order;

public interface IOrderDao extends IAGenericDao<Order> {

    List<Order> getAllOrdersByUserId(Long id);
}
