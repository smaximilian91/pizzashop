package ch.ti8m.azubi.max.pizzashop.persistence;

import ch.ti8m.azubi.max.pizzashop.ConnectionFactory;
import ch.ti8m.azubi.max.pizzashop.dto.Order;
import ch.ti8m.azubi.max.pizzashop.dto.PizzaOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class OrderDAOTest {

    public OrderDAOTest() throws SQLException {
    }

    public Date getDate() {
        return Date.valueOf(LocalDateTime.now().toLocalDate().toString());
    }

    ConnectionFactory connection = new ConnectionFactory();
    OrderDAO orderDAO = new OrderDAO(connection.testConnection());
    PizzaDAO pizzaDAO = new PizzaDAO(connection.testConnection());

    @Test
    // Liste darf nicht leer sein
    public void listeNotEmpty() throws Exception {
        Assert.assertTrue(!orderDAO.getOrderList().isEmpty());
    }

    @Test
    // String ist gleich wie Liste
    public void listeIsTheSame() throws Exception {
        String expected = "[" +
                "OrderID: #1 Adress: \"Milckyway 123\" , Phone: +00 000 00 00, " + getDate().toString() + ", " + "Order: [Pizza: Caprese Amount: 3, Pizza: Prosciutto Amount: 4] Total Price: 131.5, " +
                "OrderID: #2 Adress: \"Hello World\" , Phone: +11 111 11 11, " + getDate().toString() + ", " + "Order: [Pizza: Biancaneve Amount: 2] Total Price: 41.0, " +
                "OrderID: #3 Adress: \"Hello Earth\" , Phone: +22 222 22 22, " + getDate().toString() + ", " + "Order: [Pizza: Hawaii Amount: 1] Total Price: 21.0]";
        Assert.assertEquals(expected, orderDAO.getOrderList().toString());
    }

    @Test
    // Order mit gegebener ID existiert nicht, muss null zurückgeben
    public void getOrderWithNonExistentID() throws Exception {
        Assert.assertNull(orderDAO.getOrderByID(111));
    }

    @Test
    // Order mit gegebener ID existiert nicht, muss null zurückgeben
    public void getOrderByExistentID() throws Exception {
        Assert.assertEquals("OrderID: #1 Adress: \"Milckyway 123\" , Phone: +00 000 00 00, " + getDate().toString() + ", Order: [Pizza: Caprese Amount: 3, Pizza: Prosciutto Amount: 4] Total Price: 131.5", orderDAO.getOrderByID(1).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    // Order updaten der nicht existiert, gibt Exception zurück
    public void updateOrderThatDoesntExist() throws Exception {
        PizzaOrder pizzaOrder = new PizzaOrder(3, pizzaDAO.getPizzaByID(3));
        orderDAO.updateOrder(new Order(20, "New Adress", "+000 000 000", getDate(), Arrays.asList(pizzaOrder)));
    }

    @Test
    // Order updaten der existiert, muss gleich sein
    public void updateOrderThatExists() throws Exception {
        PizzaOrder pizzaOrder1 = new PizzaOrder(4, pizzaDAO.getPizzaByID(2));
        orderDAO.updateOrder(new Order(1, "updated Order", "+33 333 33 33", getDate(), Arrays.asList(pizzaOrder1)));
        Assert.assertEquals("OrderID: #1 Adress: \"updated Order\" , Phone: +33 333 33 33, " + getDate().toString() + ", Order: [Pizza: Caprese Amount: 4] Total Price: 74.0", orderDAO.getOrderByID(1).toString());
    }

    @Test
    // Order der nicht existiert saven (create) und Order saven der existiert (update), muss gleich sein
    public void saveOrder() throws Exception {
        orderDAO.saveOrder(new Order("createdOrder", "+33 333 33 33", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
        orderDAO.saveOrder(new Order(4, "updatedOrder", "+33 333 33 33", getDate(), Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
        Assert.assertEquals("OrderID: #4 Adress: \"updatedOrder\" , Phone: +33 333 33 33, " + getDate().toString() + ", Order: [Pizza: Margherita Amount: 3] Total Price: 49.5", orderDAO.getOrderByID(4).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierender Order übergeben, muss Exception zurückgeben
    public void deleteNonExistentOrder() throws Exception {
        orderDAO.deleteOrderByID(99);
    }

    @Test
    // Existierender Order löschen, muss true zurückgeben
    public void deleteExistentOrder() throws Exception {
        orderDAO.deleteOrderByID(1);
        Assert.assertTrue(orderDAO.numberOfDeletedRecords > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierender Order validieren, gibt Exception zurück
    public void validateNotFullOrder() {
        orderDAO.validateOrder(new Order());
    }

    @Test
    // Ganzer Order validieren, gibt nichts zurück
    public void validateFullOrder() throws Exception {
        orderDAO.validateOrder(orderDAO.getOrderByID(1));
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierender Order übergeben, muss Exception zurückgeben
    public void doesNonExistentOrderExist() throws Exception {
        orderDAO.doesOrderExist(new Order(100, "hello", "+000 000 000", getDate(), Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
    }

    @Test
    // Existierender Order übergeben, muss nichts zurückgeben
    public void doesExistentOrderExist() throws Exception {
        Assert.assertTrue(orderDAO.doesOrderExist(orderDAO.getOrderByID(1)));
    }

    @Test
    // Order erstellen und anschliessend überprüfen ob er existiert
    public void createOrder() throws Exception {
        Order order = orderDAO.createOrder(new Order("New Adress", "+11 111 11 11", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
        Assert.assertEquals("OrderID: #4 Adress: \"New Adress\" , Phone: +11 111 11 11, " + getDate().toString() + ", Order: [Pizza: Margherita Amount: 3] Total Price: 49.5", orderDAO.getOrderByID(order.getId()).toString());
    }

    @Before
    public void setup() throws Exception {

        Connection connection = ConnectionFactory.testConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("drop table if exists pizza_order");
        preparedStatement.execute();

        PreparedStatement preparedStatement1 = connection.prepareStatement("drop table if exists order2");
        preparedStatement1.execute();


        PreparedStatement preparedStatement2 = connection.prepareStatement("drop table if exists pizza");
        preparedStatement2.execute();


        String sql = String.format("create table order2 (\n" +
                " id int not null auto_increment,\n" +
                " address VARCHAR(255) not null,\n" +
                " phone VARCHAR(255) not null,\n" +
                " date datetime default null,\n" +
                " totalPrice double not null,\n" +
                " primary key (id))");
        PreparedStatement preparedStatement3 = connection.prepareStatement(sql);
        preparedStatement3.execute();

        String sql2 = String.format("create table pizza (\n" +
                " id int not null auto_increment,\n" +
                " name VARCHAR(255) not null,\n" +
                " price double not null,\n" +
                " primary key (id))");
        PreparedStatement preparedStatement4 = connection.prepareStatement(sql2);
        preparedStatement4.execute();

        String sql3 = String.format("create table pizza_order (\n" +
                " orderIDFS int not null,\n" +
                " pizzaIDFS int not null,\n" +
                " amount int not null,\n" +
                " foreign key (pizzaIDFS) references pizza(id),\n" +
                " foreign key (orderIDFS) references order2(id))");
        PreparedStatement preparedStatement5 = connection.prepareStatement(sql3);
        preparedStatement5.execute();

        String sql4 = String.format("insert into order2 (address, phone, date, totalPrice) values " +
                "('Milckyway 123', '+00 000 00 00', '" + getDate() + "', 0)," +
                "('Hello World', '+11 111 11 11', '" + getDate() + "',0)," +
                "('Hello Earth','+22 222 22 22', '" + getDate() + "', 0);");
        PreparedStatement preparedStatement6 = connection.prepareStatement(sql4);
        preparedStatement6.execute();

        String sql5 = String.format("insert into pizza (name, price) values " +
                "('Margherita', 16.50)," +
                "('Caprese', 18.50)," +
                "('Prosciutto', 19)," +
                "('Biancaneve', 20.50)," +
                "('Hawaii', 21)," +
                "('Funghi', 22.50)," +
                "('Pesto', 23);");
        PreparedStatement preparedStatement7 = connection.prepareStatement(sql5);
        preparedStatement7.execute();

        PreparedStatement preparedStatement8 = connection.prepareStatement("insert into pizza_order (orderIDFS, pizzaIDFS, amount) values " +
                "(1, 2, 3)," +
                "(2, 4, 2)," +
                "(3, 5, 1)," +
                "(1, 3, 4);");
        preparedStatement8.execute();
    }
}
