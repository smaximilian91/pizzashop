package ch.ti8m.azubi.max.pizzashop.service;

import ch.ti8m.azubi.max.pizzashop.dto.Order;

import java.util.List;

public interface OrderService {

    Order get(int orderID) throws Exception;

    List<Order> list() throws Exception;

    Order create(Order order) throws Exception;

    void update(Order order) throws Exception;

    void remove(int orderID) throws Exception;
}
