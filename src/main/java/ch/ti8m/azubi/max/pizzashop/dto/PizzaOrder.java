package ch.ti8m.azubi.max.pizzashop.dto;

import java.util.Objects;

public class PizzaOrder {

    public int amount;
    public Pizza pizza;

    public PizzaOrder(int amount, Pizza pizza) {
        this.amount = amount;
        this.pizza = pizza;
    }

    public PizzaOrder() {}
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaOrder that = (PizzaOrder) o;
        return amount == that.amount &&
                Objects.equals(pizza, that.pizza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, pizza);
    }

    @Override
    public String toString() {
        return "Pizza: " + pizza.getName() + " Amount: " + amount;
    }
}
