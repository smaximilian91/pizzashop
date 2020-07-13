package ch.ti8m.azubi.max.pizzashop.persistence;

import ch.ti8m.azubi.max.pizzashop.ConnectionFactory;
import ch.ti8m.azubi.max.pizzashop.dto.Pizza;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PizzaDAOTest {

    public PizzaDAOTest() throws SQLException {
    }

    public Date getDate() {
        return Date.valueOf(LocalDateTime.now().toLocalDate().toString());
    }

    ConnectionFactory connection = new ConnectionFactory();
    PizzaDAO pizzaDAO = new PizzaDAO(connection.testConnection());

    @Test
    // Liste darf nicht leer sein
    public void listeNotEmpty() throws Exception {
        Assert.assertTrue(!pizzaDAO.getPizzaList().isEmpty());
    }

    @Test
    // String ist gleich wie Liste
    public void listeIsTheSame() throws Exception {
        String expected = "[#1: Margherita, costs 16.5, #2: Caprese, costs 18.5, #3: Prosciutto, costs 19.0, #4: Biancaneve, costs 20.5, #5: Hawaii, costs 21.0, #6: Funghi, costs 22.5, #7: Pesto, costs 23.0]";
        Assert.assertEquals(expected, pizzaDAO.getPizzaList().toString());
    }

    @Test
    // Pizza mit gegebener ID existiert nicht, muss null zurückgeben
    public void getPizzaWithNonExistentID() throws Exception {
        Assert.assertNull(pizzaDAO.getPizzaByID(100));
    }

    @Test
    // Pizza mit gegebener ID existiert nicht, muss null zurückgeben
    public void getPizzaByExistentID() throws Exception {
        Assert.assertEquals("#1: Margherita, costs 16.5", pizzaDAO.getPizzaByID(1).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    // Pizza updaten die nicht existiert, gibt Exception zurück
    public void updatePizzaThatDoesntExist() throws Exception {
        pizzaDAO.doesPizzaExist(new Pizza(20, "Margherita", 176.50));
    }

    @Test
    // Pizza updaten die existiert, muss gleich sein
    public void updatePizzaThatExists() throws Exception {
        pizzaDAO.updatePizza(new Pizza(3, "updated Pizza", 15.50));
        Assert.assertEquals("#3: updated Pizza, costs 15.5", pizzaDAO.getPizzaByID(3).toString());
    }

    @Test
    // Pizza die nicht existiert saven (create) und Pizza saven die existiert (update), muss gleich sein
    public void savePizza() throws Exception {
        pizzaDAO.savePizza(new Pizza("New Pizza", 15.50));
        pizzaDAO.savePizza(new Pizza(8, "updated new Pizza", 17.50));
        Assert.assertEquals("#8: updated new Pizza, costs 17.5", pizzaDAO.getPizzaByID(8).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierende Pizza übergeben, muss Exception zurückgeben
    public void deleteNonExistentPizza() throws Exception {
        pizzaDAO.deletePizzaByID(99);
    }

    @Test
    // Existierende Pizza löschen, muss true zurückgeben
    public void deleteExistentPizza() throws Exception {
        pizzaDAO.deletePizzaByID(1);
        Assert.assertTrue(pizzaDAO.numberOfDeletedRecords > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierende Pizza validieren, gibt Exception zurück
    public void validateNotFullPizza() {
        pizzaDAO.validatePizza(new Pizza());
    }

    @Test
    // Ganze Pizza validieren, gibt nichts zurück
    public void validateFullPizza() throws Exception {
        pizzaDAO.validatePizza(pizzaDAO.getPizzaByID(1));
    }

    @Test(expected = IllegalArgumentException.class)
    // Nicht existierende Pizza übergeben, muss Exception zurückgeben
    public void doesNonExistentPizzaExist() throws Exception {
        pizzaDAO.doesPizzaExist(new Pizza(100, "hello", 10000.0));
    }

    @Test
    // Existierende Pizza übergeben, muss nichts zurückgeben
    public void doesExistentPizzaExist() throws Exception {
        Assert.assertTrue(pizzaDAO.doesPizzaExist(pizzaDAO.getPizzaByID(1)));
    }

    @Test
    // Pizza erstellen und überprüfen ob sie anschliessen existiert
    public void createPizza() throws Exception {
        pizzaDAO.createPizza(new Pizza("new Pizza", 18.50));
        Assert.assertEquals("#8: new Pizza, costs 18.5", pizzaDAO.getPizzaByID(8).toString());
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


