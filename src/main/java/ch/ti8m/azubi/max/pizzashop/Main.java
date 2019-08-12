package ch.ti8m.azubi.max.pizzashop;

import ch.ti8m.azubi.max.pizzashop.dto.Order;
import ch.ti8m.azubi.max.pizzashop.dto.Pizza;
import ch.ti8m.azubi.max.pizzashop.dto.PizzaOrder;
import ch.ti8m.azubi.max.pizzashop.persistence.OrderDAO;
import ch.ti8m.azubi.max.pizzashop.persistence.PizzaDAO;
import ch.ti8m.azubi.max.pizzashop.service.ConnectionUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {


        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionUtil.createDBConnection());
        Order order = new Order();
        order.setId(1);
        order.setDate(Date.valueOf(LocalDate.now()));
        order.setAddress("hello 123");
        order.setPhone("123 456 78 90");
        PizzaOrder pizzaOrder = new PizzaOrder(3, pizzaDAO.getPizzaByID(1));
        PizzaOrder pizzaOrder2 = new PizzaOrder(3, pizzaDAO.getPizzaByID(2));
        order.setPizzaOrders(Arrays.asList(pizzaOrder, pizzaOrder2));
        order.calculateTotalPrice();
       // String json = ObjectMapperFactory.objectMapper().writeValueAsString(order);

       // System.out.println(json);

        //Order restoredOrder = ObjectMapperFactory.objectMapper().readValue(json, Order.class);



    }

    public String lineSeperator() {
        return "________________________________________________________";
    }

    public void setup() throws Exception {
        Connection dbConnection = ConnectionUtil.createDBConnection();
        PizzaDAO pizzaDAO = new PizzaDAO(dbConnection);
        OrderDAO orderDAO = new OrderDAO(dbConnection);

        PreparedStatement preparedStatement = dbConnection.prepareStatement("drop table if exists pizza_order");
        preparedStatement.execute();

        PreparedStatement preparedStatement1 = dbConnection.prepareStatement("drop table if exists order2");
        preparedStatement1.execute();


        PreparedStatement preparedStatement2 = dbConnection.prepareStatement("drop table if exists pizza");
        preparedStatement2.execute();


        String sql = String.format("create table order2 (\n" +
                " id int not null auto_increment,\n" +
                " address VARCHAR(255) not null,\n" +
                " phone varcharacter(255) not null,\n" +
                " date datetime default current_timestamp not null,\n" +
                " totalPrice double not null,\n" +
                " primary key (id))");
        PreparedStatement preparedStatement3 = dbConnection.prepareStatement(sql);
        preparedStatement3.execute();

        String sql2 = String.format("create table pizza (\n" +
                " id int not null auto_increment,\n" +
                " name VARCHAR(255) not null,\n" +
                " price double not null,\n" +
                " primary key (id))");
        PreparedStatement preparedStatement4 = dbConnection.prepareStatement(sql2);
        preparedStatement4.execute();

        String sql3 = String.format("create table pizza_order (\n" +
                " orderIDFS int not null,\n" +
                " pizzaIDFS int not null,\n" +
                " amount int not null,\n" +
                " foreign key (pizzaIDFS) references pizza(id),\n" +
                " foreign key (orderIDFS) references order2(id))");
        PreparedStatement preparedStatement5 = dbConnection.prepareStatement(sql3);
        preparedStatement5.execute();

        Pizza pizza1 = new Pizza("Margherita", 16.50);
        Pizza pizza2 = new Pizza("Caprese", 18.50);
        Pizza pizza3 = new Pizza("Prosciutto", 19.0);
        Pizza pizza4 = new Pizza("Biancaneve", 20.50);
        Pizza pizza5 = new Pizza("Hawaii", 21.0);
        Pizza pizza6 = new Pizza("Funghi", 22.50);
        Pizza pizza7 = new Pizza("Pesto", 24.0);

        pizzaDAO.savePizza(pizza1);
        pizzaDAO.savePizza(pizza2);
        pizzaDAO.savePizza(pizza3);
        pizzaDAO.savePizza(pizza4);
        pizzaDAO.savePizza(pizza5);
        pizzaDAO.savePizza(pizza6);
        pizzaDAO.savePizza(pizza7);
/*
        PizzaOrder pizzaOrder1 = new PizzaOrder(3, pizza1);
        PizzaOrder pizzaOrder2 = new PizzaOrder(2, pizza2);
        PizzaOrder pizzaOrder3 = new PizzaOrder(1, pizza3);
        PizzaOrder pizzaOrder4 = new PizzaOrder(3, pizza4);
        PizzaOrder pizzaOrder5 = new PizzaOrder(2, pizza5);
        PizzaOrder pizzaOrder6 = new PizzaOrder(1, pizza6);
        PizzaOrder pizzaOrder7 = new PizzaOrder(3, pizza7);

        Order order1 = new Order("Address 1", "+11 111 11 11", Arrays.asList(pizzaOrder1));
        Order order2 = new Order("Address 2", "+22 222 22 22", Arrays.asList(pizzaOrder2));
        Order order3 = new Order("Address 3", "+33 333 33 33", Arrays.asList(pizzaOrder3));
        Order order4 = new Order("Address 4", "+44 444 44 44", Arrays.asList(pizzaOrder4));
        Order order5 = new Order("Address 5", "+55 555 55 55", Arrays.asList(pizzaOrder5));
        Order order6 = new Order("Address 6", "+66 666 66 66", Arrays.asList(pizzaOrder6));
        Order order7 = new Order("Address 7", "+77 777 77 77", Arrays.asList(pizzaOrder7));

        orderDAO.saveOrder(order1);
        orderDAO.saveOrder(order2);
        orderDAO.saveOrder(order3);
        orderDAO.saveOrder(order4);
        orderDAO.saveOrder(order5);
        orderDAO.saveOrder(order6);
        orderDAO.saveOrder(order7);
        */

    }
}
