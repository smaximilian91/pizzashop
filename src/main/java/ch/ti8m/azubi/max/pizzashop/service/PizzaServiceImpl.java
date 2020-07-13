package ch.ti8m.azubi.max.pizzashop.service;

import ch.ti8m.azubi.max.pizzashop.dto.Pizza;
import ch.ti8m.azubi.max.pizzashop.persistence.PizzaDAO;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class PizzaServiceImpl implements PizzaService {

    private final PizzaDAO pizzaDAO = new PizzaDAO(ConnectionUtil.createDBConnection());

    @Override
    public Pizza get(int pizzaID) throws Exception {
        Pizza pizza = pizzaDAO.getPizzaByID(pizzaID);
        if (pizza == null) {
            throw new NoSuchElementException("No Pizza with id " + pizzaID + " exists");
        }
        return pizza;
    }

    @Override
    public List<Pizza> list() throws Exception {
        List<Pizza> pizzaList = pizzaDAO.getPizzaList();
        pizzaList.sort(Comparator.comparing(Pizza::getId));
        return pizzaList;
    }

    @Override
    public Pizza create(Pizza pizza) throws Exception {
        return pizzaDAO.createPizza(pizza);
    }

    @Override
    public void update(Pizza pizza) throws Exception {
        pizzaDAO.updatePizza(pizza);
    }

    @Override
    public void remove(int pizzaID) throws Exception {
        pizzaDAO.deletePizzaByID(pizzaID);
    }
}
