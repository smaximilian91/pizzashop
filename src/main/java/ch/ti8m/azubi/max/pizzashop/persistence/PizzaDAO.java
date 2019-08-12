package ch.ti8m.azubi.max.pizzashop.persistence;

import ch.ti8m.azubi.max.pizzashop.dto.Pizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PizzaDAO {

    private Connection connection;
    public int numberOfDeletedRecords;

    public PizzaDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Return a List of all Pizzas.
     */
    public List<Pizza> getPizzaList() throws Exception {

        List<Pizza> pizzaList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from pizza");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            pizzaList.add(new Pizza(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price")));
        }
        return pizzaList;
    }

    /**
     * Get the Pizza by ID. If no such Pizza is found, null is returned.
     */
    public Pizza getPizzaByID(int id) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("select * from pizza where id =" + id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new Pizza(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price"));
        } else {
            return null;
        }
    }

    /**
     * Create a new Pizza
     */
    public Pizza createPizza(Pizza pizza) throws Exception {
        validatePizza(pizza);

        PreparedStatement preparedStatement = connection.prepareStatement("insert into pizza (name, price) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, pizza.getName());
        preparedStatement.setDouble(2, pizza.getPrice());
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        pizza.setId(resultSet.getInt(1));

        return pizza;
    }

    /**
     * Update a Pizza
     */
    public void updatePizza(Pizza pizza) throws Exception {
        validatePizza(pizza);
        doesPizzaExist(pizza);

        PreparedStatement preparedStatement = connection.prepareStatement("update pizza set name = (?), price = (?) where id =" + pizza.getId());
        preparedStatement.setString(1, pizza.getName());
        preparedStatement.setDouble(2, pizza.getPrice());
        preparedStatement.executeUpdate();
    }

    /**
     * Save a Pizza
     */
    public void savePizza(Pizza pizza) throws Exception {

        if (pizza.getId() == null) {
            createPizza(pizza);
        } else {
            updatePizza(pizza);
        }
    }

    /**
     * Find a Pizza
     */
    public Pizza findPizza(int id) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("select * from pizza where id =" + id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return getPizzaByID(id);
        }
        return null;
    }

    /**
     * Delete a Pizza by ID
     */
    public boolean deletePizzaByID(int id) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("delete from pizza where id =" + id);
        numberOfDeletedRecords = preparedStatement.executeUpdate();

        if (numberOfDeletedRecords > 0) {
            return true;
        } else {
            throw new IllegalArgumentException("Pizza doesnt exist");
        }
    }

    /**
     * Validates a Pizza
     */
    public void validatePizza(Pizza pizza) {
        if (pizza.getName() == null) {
            throw new IllegalArgumentException("Name is required");
        }
        if (pizza.getPrice() == null) {
            throw new IllegalArgumentException("Price is required");
        }
    }

    /**
     * Returns true or false if the Pizza exists
     */
    public boolean doesPizzaExist(Pizza pizza) throws Exception {
        if (pizza.getId() == null || getPizzaByID(pizza.getId()) == null) {
            throw new IllegalArgumentException("Pizza doesnt exist");
        }
        return true;
    }
}