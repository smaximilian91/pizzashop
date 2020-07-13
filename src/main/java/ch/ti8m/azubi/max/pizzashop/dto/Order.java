package ch.ti8m.azubi.max.pizzashop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("address")
    public String address;
    @JsonProperty("phone")
    public String phone;
    @JsonProperty("date")
    public Date date;
    @JsonProperty("total")
    public Double total;
    @JsonProperty("pizzaOrders")
    public List<PizzaOrder> pizzaOrders;

    public Order(Integer id, String address, String phone, List<PizzaOrder> pizzaOrders) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.date = getLocalDate();
        this.pizzaOrders = pizzaOrders;
        calculateTotalPrice();
    }

    public Order(Integer id, String address, String phone, Date date, List<PizzaOrder> pizzaOrders) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.date = date;
        this.pizzaOrders = pizzaOrders;
        calculateTotalPrice();
    }

    public Order(String address, String phone, List<PizzaOrder> pizzaOrders) {
        this.address = address;
        this.phone = phone;
        this.date = getLocalDate();
        this.pizzaOrders = pizzaOrders;
        calculateTotalPrice();
    }

    public Order(Integer pizzaID, String pizzaName, List<PizzaOrder> pizzaOrders) {
        this.id = pizzaID;
        this.address = pizzaName;
        this.pizzaOrders = pizzaOrders;
        this.date = getLocalDate();
        calculateTotalPrice();
    }

    public Order() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PizzaOrder> getPizzaOrders() {
        return pizzaOrders;
    }

    public void setPizzaOrders(List<PizzaOrder> pizzaOrders) {
        this.pizzaOrders = pizzaOrders;
    }

    @Override
    public String toString() {
        return "OrderID: #" + id + " Adress: \"" + address + "\" , Phone: " + phone + ", " + date + ", Order: " + pizzaOrders + " Total Price: " + total;
    }

    public void calculateTotalPrice() {
        double totalPrice = 0;
        for (int i = 0; i < pizzaOrders.size(); i++) {
            totalPrice += pizzaOrders.get(i).getAmount() * pizzaOrders.get(i).getPizza().getPrice();
        }
        this.total = totalPrice;
    }

    @JsonIgnore
    public Date getLocalDate() {
        return Date.valueOf(LocalDateTime.now().toLocalDate().toString());
    }
}
