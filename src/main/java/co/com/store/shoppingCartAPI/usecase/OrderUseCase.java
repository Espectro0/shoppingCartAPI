package co.com.store.shoppingCartAPI.usecase;

import co.com.store.shoppingCartAPI.model.Order;
import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.service.order.repository.OrderRepository;
import co.com.store.shoppingCartAPI.service.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderUseCase {

    private final OrderRepository orderRepository;

    public OrderUseCase(@Qualifier("MySQLOrderService") OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public Order createOrder(String userId) {
        return orderRepository.createOrder(userId);
    }

    public List<Order> getOrders(String userId, String status) {
        return orderRepository.getOrders(userId, status);
    }

    public Order getOrderById(String userId, String orderId) {
        return orderRepository.getOrderById(userId, orderId);
    }

    public void addProductToOrder(String userId, String orderId, String productId, Integer quantity) {
        orderRepository.addProductToOrder(userId, orderId, productId, quantity);
    }

    public void updateProductQuantity(String userId, String orderId, String productId, Integer quantity) {
        orderRepository.updateProductQuantity(userId, orderId, productId, quantity);
    }

    public void removeProductFromOrder(String userId, String orderId, String productId) {
        orderRepository.removeProductFromOrder(userId, orderId, productId);
    }

    public Order checkoutOrder(String userId, String orderId) {
        return orderRepository.checkoutOrder(userId, orderId);
    }

    public void cancelOrder(String userId, String orderId) {
        orderRepository.cancelOrder(userId, orderId);
    }
}
