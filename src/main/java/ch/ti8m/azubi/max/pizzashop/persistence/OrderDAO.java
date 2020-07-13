package ch.ti8m.azubi.max.pizzashop.persistence;

import ch.ti8m.azubi.max.pizzashop.dto.Order;
import ch.ti8m.azubi.max.pizzashop.dto.PizzaOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Connection connection;
    private PizzaDAO pizzaDAO;
    public int numberOfDeletedRecords;

    public OrderDAO(Connection connection) {
        this.connection = connection;
        pizzaDAO = new PizzaDAO(connection);
    }

    /**
     * Return a List of all Orders.
     */
    public List<Order> getOrderList() throws Exception {

        List<Order> orderList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from order2");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            orderList.add(new Order(resultSet.getInt("id"), resultSet.getString("address"), resultSet.getString("phone"), resultSet.getDate("date"), getPizzaOrders(resultSet.getInt("id"))));
        }
        return orderList;
    }

    public Order getOrderByID(int id) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("select * from order2 where id =" + id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new Order(resultSet.getInt("id"), resultSet.getString("address"), resultSet.getString("phone"), resultSet.getDate("date"), getPizzaOrders(resultSet.getInt("id")));
        }
        return null;
    }

    /**
     * Create a new Order
     */
    public Order createOrder(Order order) throws Exception {
        validateOrder(order);

        PreparedStatement preparedStatement = connection.prepareStatement("insert into order2 (address, phone, totalPrice, date) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, order.getAddress());
        preparedStatement.setString(2, order.getPhone());
        preparedStatement.setDouble(3, order.getTotal());
        preparedStatement.setDate(4, order.getDate());
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        order.setId(resultSet.getInt(1));

        PreparedStatement preparedStatement2 = connection.prepareStatement("insert into pizza_order (orderIDFS, pizzaIDFS, amount) values (?, ?, ?)");
        List<PizzaOrder> pizzaOrderList = order.getPizzaOrders();
        for (int i = 0; i < pizzaOrderList.size(); i++) {
            preparedStatement2.setInt(1, order.getId());
            preparedStatement2.setInt(2, pizzaOrderList.get(i).getPizza().getId());
            preparedStatement2.setInt(3, pizzaOrderList.get(i).getAmount());
            preparedStatement2.execute();
        }
        return order;
    }

    /**
     * Update an Order
     */
    public void updateOrder(Order order) throws Exception {
        validateOrder(order);
        doesOrderExist(order);

        PreparedStatement preparedStatement = connection.prepareStatement("update order2 set address = (?), phone = (?) where id =" + order.getId());
        preparedStatement.setString(1, order.getAddress());
        preparedStatement.setString(2, order.getPhone());
        preparedStatement.executeUpdate();

        PreparedStatement preparedStatement1 = connection.prepareStatement("delete from pizza_order where orderIDFS = " + order.getId());
        preparedStatement1.executeUpdate();

        PreparedStatement preparedStatement2 = connection.prepareStatement("insert into pizza_order (orderIDFS, pizzaIDFS, amount) values (?, ?, ?)");
        List<PizzaOrder> pizzaOrderList = order.getPizzaOrders();
        for (int i = 0; i < pizzaOrderList.size(); i++) {
            preparedStatement2.setInt(1, order.getId());
            preparedStatement2.setInt(2, pizzaOrderList.get(i).getPizza().getId());
            preparedStatement2.setInt(3, pizzaOrderList.get(i).getAmount());
            preparedStatement2.executeUpdate();
        }
    }

    /**
     * Save an Order
     */
    public void saveOrder(Order order) throws Exception {

        if (order.getId() == null) {
            createOrder(order);
        } else {
            updateOrder(order);
        }
    }

    /**
     * Delete an Order by ID
     */
    public boolean deleteOrderByID(int id) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("delete from pizza_order where orderIDFS =" + id);
        preparedStatement.executeUpdate();

        PreparedStatement preparedStatement1 = connection.prepareStatement("delete from order2 where id =" + id);
        numberOfDeletedRecords = preparedStatement1.executeUpdate();

        if (numberOfDeletedRecords > 0) {
            return true;
        } else {
            throw new IllegalArgumentException("Order doesnt exist");
        }
    }

    /**
     * Validates an Order
     */
    public void validateOrder(Order order) {
        if (order.getAddress() == null) {
            throw new IllegalArgumentException("Address is required");
        }
        if (order.getPizzaOrders() == null) {
            throw new IllegalArgumentException("PizzaOrder is required");
        }
        if (order.getPhone() == null) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (order.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }
    }

    /**
     * Returns true or false if the Order exists
     */
    public boolean doesOrderExist(Order order) throws Exception {
        if (order.getId() == null || getOrderByID(order.getId()) == null) {
            throw new IllegalArgumentException("Order doesnt exist");
        }
        return true;
    }

    /**
     * Returns all pizzaOrder of an order by its id
     */
    public List<PizzaOrder> getPizzaOrders(int id) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("select * from pizza_order where orderIDFS =" + id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<PizzaOrder> pizzaOrderList = new ArrayList<>();
        while (resultSet.next()) {
            pizzaOrderList.add(new PizzaOrder(resultSet.getInt("amount"), pizzaDAO.getPizzaByID(resultSet.getInt("pizzaIDFS"))));
        }
        return pizzaOrderList;
    }
}
