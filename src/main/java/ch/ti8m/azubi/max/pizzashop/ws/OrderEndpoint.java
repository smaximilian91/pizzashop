package ch.ti8m.azubi.max.pizzashop.ws;

import ch.ti8m.azubi.max.pizzashop.dto.Order;
import ch.ti8m.azubi.max.pizzashop.service.OrderService;
import ch.ti8m.azubi.max.pizzashop.service.ServiceRegistry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/orders")
public class OrderEndpoint {

    private OrderService orderService = ServiceRegistry.getInstance().get(OrderService.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> listOrders() throws Exception {
        return orderService.list();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrder(@PathParam("id") int id) throws Exception {
        return orderService.get(id);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Order createOrder(Order order) throws Exception {
        order.calculateTotalPrice();
        return orderService.create(order);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateOrder(@PathParam("id") int id, Order order) throws Exception {
        order.setId(id);
        orderService.update(order);
    }

    @DELETE
    @Path("/{id}")
    public void deleteOrder(@PathParam("id") int id) throws Exception {
        orderService.remove(id);
    }
}
