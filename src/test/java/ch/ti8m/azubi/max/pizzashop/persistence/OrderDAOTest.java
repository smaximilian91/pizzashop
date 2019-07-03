package ch.ti8m.azubi.max.pizzashop.dto;

import ch.ti8m.azubi.max.pizzashop.persistence.OrderDAO;
import ch.ti8m.azubi.max.pizzashop.persistence.PizzaDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class OrderDAOTest {

    public OrderDAOTest() throws SQLException {}

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
        String expected = "[OrderID: #1 Adress: \"Milckyway 123\" Order: [Pizza: Caprese Amount: 3, Pizza: Prosciutto Amount: 4] Total Price: 131.5, OrderID: #2 Adress: \"Hello World\" Order: [Pizza: Biancaneve Amount: 2] Total Price: 41.0, OrderID: #3 Adress: \"Hello Earth\" Order: [Pizza: Hawaii Amount: 1] Total Price: 21.0]";
        Assert.assertEquals(expected, orderDAO.getOrderList().toString());
    }

    @Test
    // Person mit gegebener ID existiert nicht, muss null zurückgeben
    public void getOrderWithNonExistentID() throws Exception {
        Assert.assertNull(orderDAO.getOrderByID(111));
    }

    @Test
    // Person mit gegebener ID existiert nicht, muss null zurückgeben
    public void getOrderByExistentID() throws Exception {
        Assert.assertEquals("OrderID: #1 Adress: \"Milckyway 123\" Order: [Pizza: Caprese Amount: 3, Pizza: Prosciutto Amount: 4] Total Price: 131.5", orderDAO.getOrderByID(1).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    // Person updaten die nicht existiert, gibt Exception zurück
    public void updateOrderThatDoesntExist() throws Exception {
        orderDAO.updateOrder(new Order(20, "Margherita", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(10)))));

    }

    @Test
    // Person updaten die existiert, muss gleich sein
    public void updateOrderThatExists() throws Exception {
        orderDAO.updateOrder(new Order(1, "updated Order", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(2)))));
        Assert.assertEquals("OrderID: #1 Adress: \"updated Order\" Order: [Pizza: Caprese Amount: 3, Pizza: Prosciutto Amount: 4] Total Price: 131.5", orderDAO.getOrderByID(1).toString());
    }

    @Test
    // Person die nicht existiert saven (createPerson) und person saven die existiert (update), muss gleich sein
    public void saveOrder() throws Exception {
        orderDAO.saveOrder(new Order("createdOrder", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
        orderDAO.saveOrder(new Order(4, "updatedOrder", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
        Assert.assertEquals("OrderID: #4 Adress: \"updatedOrder\" Order: [Pizza: Margherita Amount: 3] Total Price: 49.5", orderDAO.getOrderByID(4).toString());
    }

    @Test
    // Person finden die nicht existiert, gibt null zurück
    public void findOrderThatDoesntExist() throws Exception {
        Assert.assertNull(orderDAO.findOrder("Hallo"));
    }

    @Test
    // Person finden die existiert, gibt nicht null zurück
    public void findOrderThatExists() throws Exception {
        Assert.assertNotNull((orderDAO.findOrder("Milckyway 123")));
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierende Person übergeben, muss Exception zurückgeben
    public void deleteNonExistentOrder() throws Exception {
        orderDAO.deleteOrderByID(99);
    }

    @Test
    // Existierende Person löschen, muss true zurückgeben
    public void deleteExistentOrder() throws Exception {
        orderDAO.deleteOrderByID(1);
        Assert.assertTrue(orderDAO.numberOfDeletedRecords > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierende Person validieren, gibt Exception zurück
    public void validateNotFullOrder() {
        orderDAO.validateOrder(new Order());
    }

    @Test
    // Ganze Person validieren, gibt nichts zurück
    public void validateFullOrder() throws Exception {
        orderDAO.validateOrder(orderDAO.getOrderByID(1));
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierende Person übergeben, muss Exception zurückgeben
    public void doesNonExistentOrderExist() throws Exception {
        orderDAO.doesOrderExist(new Order(100, "hello", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
    }

    @Test
    // Existierende Person übergeben, muss nichts zurückgeben
    public void doesExistentOrderExist() throws Exception {
        Assert.assertTrue(orderDAO.doesOrderExist(orderDAO.getOrderByID(1)));
    }

    @Test
    // Pizza erstellen und überprüfen ob sie anschliessen existiert
    public void createOrder() throws Exception {
        orderDAO.createOrder(new Order("New Adress", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
        Assert.assertEquals("OrderID: #4 Adress: \"New Adress\" Order: [Pizza: Margherita Amount: 3] Total Price: 49.5", orderDAO.getOrderByID(4).toString());
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
                " orderID int not null auto_increment,\n" +
                " adress VARCHAR(255) not null,\n" +
                " totalPrice double not null,\n" +
                " primary key (orderID))");
        PreparedStatement preparedStatement3 = connection.prepareStatement(sql);
        preparedStatement3.execute();

        String sql2 = String.format("create table pizza (\n" +
                " pizzaID int not null auto_increment,\n" +
                " pizzaName VARCHAR(255) not null,\n" +
                " pizzaPrice double not null,\n" +
                " primary key (pizzaID))");
        PreparedStatement preparedStatement4 = connection.prepareStatement(sql2);
        preparedStatement4.execute();

        String sql3 = String.format("create table pizza_order (\n" +
                " orderIDFS int not null,\n" +
                " pizzaIDFS int not null,\n" +
                " amount int not null,\n" +
                " price double not null,\n" +
                " foreign key (pizzaIDFS) references pizza(pizzaID),\n" +
                " foreign key (orderIDFS) references order2(orderID))");
        PreparedStatement preparedStatement5 = connection.prepareStatement(sql3);
        preparedStatement5.execute();

        String sql4 = String.format("insert into order2 (adress, totalPrice) values " +
                "('Milckyway 123', 0)," +
                "('Hello World', 0)," +
                "('Hello Earth', 0);");
        PreparedStatement preparedStatement6 = connection.prepareStatement(sql4);
        preparedStatement6.execute();

        String sql5 = String.format("insert into pizza (pizzaName, pizzaPrice) values " +
                "('Margherita', 16.50)," +
                "('Caprese', 18.50)," +
                "('Prosciutto', 19)," +
                "('Biancaneve', 20.50)," +
                "('Hawaii', 21)," +
                "('Funghi', 22.50)," +
                "('Pesto', 23);");
        PreparedStatement preparedStatement7 = connection.prepareStatement(sql5);
        preparedStatement7.execute();

        PreparedStatement preparedStatement8 = connection.prepareStatement("insert into pizza_order (orderIDFS, pizzaIDFS, amount, price) values " +
                "(1, 2, 3, 18.50)," +
                "(2, 4, 2, 20.50)," +
                "(3, 5, 1, 20.50)," +
                "(1, 3, 4, 19);");
        preparedStatement8.execute();
    }
}
