package ch.ti8m.azubi.max.pizzashop.service;

import ch.ti8m.azubi.max.pizzashop.dto.Order;
import ch.ti8m.azubi.max.pizzashop.persistence.OrderDAO;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO = new OrderDAO(ConnectionUtil.createDBConnection());

    @Override
    public Order get(int orderID) throws Exception {
        Order order = orderDAO.getOrderByID(orderID);
        if (order == null) {
            throw new NoSuchElementException("No Order with id " + orderID + " exists");
        }
        return order;
    }

    @Override
    public List<Order> list() throws Exception {
        List<Order> orderList = orderDAO.getOrderList();
        orderList.sort(Comparator.comparing(Order::getId));
        return orderList;
    }

    @Override
    public Order create(Order order) throws Exception {
        return orderDAO.createOrder(order);
    }

    @Override
    public void update(Order order) throws Exception {
        orderDAO.updateOrder(order);
    }

    @Override
    public void remove(int orderID) throws Exception {
        orderDAO.deleteOrderByID(orderID);
    }
}
