package ch.ti8m.azubi.max.pizzashop.service;

import ch.ti8m.azubi.max.pizzashop.dto.Pizza;

import java.util.List;

public interface PizzaService {

    Pizza get(int pizzaID) throws Exception;

    List<Pizza> list() throws Exception;

    Pizza create(Pizza pizza) throws Exception;

    void update(Pizza pizza) throws Exception;

    void remove(int pizzaID) throws Exception;
}
