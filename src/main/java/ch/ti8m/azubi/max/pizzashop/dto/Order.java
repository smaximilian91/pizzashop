package ch.ti8m.azubi.max.pizzashop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
        this.total = pizzaOrders.stream().mapToDouble(pizzaOrder -> pizzaOrder.getAmount() * pizzaOrder.getPizza().getPrice()).sum();
    }

    @JsonIgnore
    public Date getLocalDate() {
        return Date.valueOf(LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(address, order.address) &&
                Objects.equals(phone, order.phone) &&
                Objects.equals(date, order.date) &&
                Objects.equals(total, order.total) &&
                Objects.equals(pizzaOrders, order.pizzaOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, phone, date, total, pizzaOrders);
    }
}
