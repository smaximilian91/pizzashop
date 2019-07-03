package ch.ti8m.azubi.max.pizzashop.dto;

import ch.ti8m.azubi.max.pizzashop.persistence.OrderDAO;
import ch.ti8m.azubi.max.pizzashop.persistence.PizzaDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        OrderDAO orderDAO = new OrderDAO(createDBConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(createDBConnection());
        Main main = new Main();
        main.setup();

        List<PizzaOrder> pizzaOrderList = new ArrayList<>();

        pizzaOrderList.add(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)));
        pizzaOrderList.add(new PizzaOrder(3, pizzaDAO.getPizzaByID(2)));

        // 1. Liste von Pizzas ausgeben
        System.out.println("1. Liste von Pizzas:");
        System.out.println(pizzaDAO.getPizzaList());
        System.out.println(main.lineSeperator());

        // 2. Pizza mit der orderID ausgeben, wenn nicht vorhanden null rückgeben
        System.out.println("2. Pizza by ID:");
        System.out.println(pizzaDAO.getPizzaByID(2));
        System.out.println(main.lineSeperator());

        // 3. Pizza erstellen
        System.out.println("3. Pizza erstellen und ausgeben:");
        Pizza newPizza = pizzaDAO.createPizza(new Pizza("neue Pizza", 13.50));
        System.out.println(newPizza);
        System.out.println(main.lineSeperator());

        // 4. Pizza updaten
        System.out.println("4. Pizza updaten und Liste erneut ausgeben");
        newPizza.setPizzaName("geupdatete Pizza");
        newPizza.setPizzaPrice(17.50);
        pizzaDAO.updatePizza(newPizza);
        System.out.println(pizzaDAO.getPizzaList());
        System.out.println(main.lineSeperator());

        // 5. Pizza deleten
        System.out.println("5. Pizza löschen und Liste erneut ausgeben");
        System.out.println("Gelöscht: " + pizzaDAO.deletePizzaByID(newPizza.getPizzaID()));
        System.out.println(pizzaDAO.getPizzaList());
        System.out.println(main.lineSeperator());

        // 6. Pizza saven
        System.out.println("6. Pizza saven, 1x create, 1x updaten");
        pizzaDAO.savePizza(new Pizza("Coole Pizza", 15.50));
        System.out.println(pizzaDAO.getPizzaList());
        pizzaDAO.savePizza(new Pizza(9, "Noch coolere Pizza", 16.50));
        System.out.println(pizzaDAO.getPizzaList());
        System.out.println(main.lineSeperator());

        // 7. Pizza finden
        System.out.println("7. Pizza finden mit Suchtext: Margherita");
        System.out.println(pizzaDAO.findPizza("Margherita"));
        System.out.println(main.lineSeperator());

        // 1. Liste von Orders ausgeben
        System.out.println("1. Liste von Orders:");
        System.out.println(orderDAO.getOrderList());
        System.out.println(main.lineSeperator());

        // 2. Order mit der orderID ausgeben, wenn nicht vorhanden null rückgeben
        System.out.println("2. Order by ID:");
        System.out.println(orderDAO.getOrderByID(1));
        System.out.println(main.lineSeperator());

        // 3. Order erstellen
        System.out.println("3. Order erstellen und ausgeben:");
        System.out.println(orderDAO.createOrder(new Order("Milckyway 123", pizzaOrderList)));
        System.out.println(main.lineSeperator());

        // 4. Order updaten
        System.out.println("4. Order updaten und Liste erneut ausgeben");
        orderDAO.updateOrder(new Order(4, "Hello 123", pizzaOrderList));
        System.out.println(orderDAO.getOrderList());
        System.out.println(main.lineSeperator());

        // 5. Order deleten
        System.out.println("5. Order löschen und Liste erneut ausgeben");
        System.out.println("Gelöscht: " + orderDAO.deleteOrderByID(4));
        System.out.println(orderDAO.getOrderList());
        System.out.println(main.lineSeperator());

        // 6. Order saven
        System.out.println("6. Order saven, 1x create, 1x updaten");
        orderDAO.saveOrder(new Order("Hello world", pizzaOrderList));
        System.out.println(orderDAO.getOrderList());
        orderDAO.saveOrder(new Order(5, "Hello earth", pizzaOrderList));
        System.out.println(orderDAO.getOrderList());
        System.out.println(main.lineSeperator());

        // 7. Order finden
        System.out.println("7. Order erstellen und anschliessend finden mit Suchtext: Hello globe");
        orderDAO.createOrder(new Order("Hello globe", pizzaOrderList));
        System.out.println(orderDAO.findOrder("Hello globe"));
        System.out.println(main.lineSeperator());

    }

    private static Connection createDBConnection() throws SQLException {
        return createDBConnection("localhost", 3306, "pizzaShop", "root", "");
    }

    private static Connection createDBConnection(String host, int port, String dbName, String user, String password) throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found");
        }
        String connectionURL = String.format("jdbc:mysql://%s:%d/%s", host, port, dbName);
        return DriverManager.getConnection(connectionURL, user, password);
    }

    public String lineSeperator() {
        return "________________________________________________________";
    }

    public void setup() throws Exception {
        Connection dbConnection = createDBConnection();
        PizzaDAO pizzaDAO = new PizzaDAO(dbConnection);
        OrderDAO orderDAO = new OrderDAO(dbConnection);

        PreparedStatement preparedStatement = dbConnection.prepareStatement("drop table if exists pizza_order");
        preparedStatement.execute();

        PreparedStatement preparedStatement1 = dbConnection.prepareStatement("drop table if exists order2");
        preparedStatement1.execute();


        PreparedStatement preparedStatement2 = dbConnection.prepareStatement("drop table if exists pizza");
        preparedStatement2.execute();


        String sql = String.format("create table order2 (\n" +
                " orderID int not null auto_increment,\n" +
                " adress VARCHAR(255) not null,\n" +
                " totalPrice decimal not null,\n" +
                " primary key (orderID))");
        PreparedStatement preparedStatement3 = dbConnection.prepareStatement(sql);
        preparedStatement3.execute();

        String sql2 = String.format("create table pizza (\n" +
                " pizzaID int not null auto_increment,\n" +
                " pizzaName VARCHAR(255) not null,\n" +
                " pizzaPrice decimal not null,\n" +
                " primary key (pizzaID))");
        PreparedStatement preparedStatement4 = dbConnection.prepareStatement(sql2);
        preparedStatement4.execute();

        String sql3 = String.format("create table pizza_order (\n" +
                " orderIDFS int not null,\n" +
                " pizzaIDFS int not null,\n" +
                " amount int not null,\n" +
                " price decimal not null,\n" +
                " foreign key (pizzaIDFS) references pizza(pizzaID),\n" +
                " foreign key (orderIDFS) references order2(orderID))");
        PreparedStatement preparedStatement5 = dbConnection.prepareStatement(sql3);
        preparedStatement5.execute();

        Pizza pizza1 = new Pizza("Margherita", 16.50);
        Pizza pizza2 = new Pizza("Capres", 18.50);
        Pizza pizza3 = new Pizza("Prosciutto", 19.0);
        Pizza pizza4 = new Pizza("Biancaneve", 20.50);
        Pizza pizza5 = new Pizza("Hawaii",  21.0);
        Pizza pizza6 = new Pizza("Funghi", 22.50);
        Pizza pizza7 = new Pizza("Pesto", 23.0);

        pizzaDAO.savePizza(pizza1);
        pizzaDAO.savePizza(pizza2);
        pizzaDAO.savePizza(pizza3);
        pizzaDAO.savePizza(pizza4);
        pizzaDAO.savePizza(pizza5);
        pizzaDAO.savePizza(pizza6);
        pizzaDAO.savePizza(pizza7);

        Order order1 = new Order("Address 1", Arrays.asList(new PizzaOrder(3, pizza1)));
        Order order2 = new Order("Address 2", Arrays.asList(new PizzaOrder(2, pizza2)));
        Order order3 = new Order("Address 3", Arrays.asList(new PizzaOrder(1, pizza3)));
        Order order4 = new Order("Address 4", Arrays.asList(new PizzaOrder(3, pizza4)));
        Order order5 = new Order("Address 5", Arrays.asList(new PizzaOrder(2, pizza5)));
        Order order6 = new Order("Address 6", Arrays.asList(new PizzaOrder(1, pizza6)));
        Order order7 = new Order("Address 7", Arrays.asList(new PizzaOrder(3, pizza7)));

        orderDAO.saveOrder(order1);
        orderDAO.saveOrder(order2);
        orderDAO.saveOrder(order3);
        orderDAO.saveOrder(order4);
        orderDAO.saveOrder(order5);
        orderDAO.saveOrder(order6);
        orderDAO.saveOrder(order7);

    }
}
