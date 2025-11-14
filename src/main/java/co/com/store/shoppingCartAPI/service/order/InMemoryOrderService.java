package co.com.store.shoppingCartAPI.service.order;

import co.com.store.shoppingCartAPI.model.Order;
import co.com.store.shoppingCartAPI.model.OrderItem;
import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.model.User;
import co.com.store.shoppingCartAPI.service.order.repository.OrderRepository;
import co.com.store.shoppingCartAPI.service.product.repository.ProductRepository;
import co.com.store.shoppingCartAPI.service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("InMemoryOrderService")
public class InMemoryOrderService implements OrderRepository {

    private List<Order> orders = new ArrayList<>();
    private final UserRepository userService;
    private final ProductRepository productService;

    public InMemoryOrderService(@Qualifier("inMemoryUserService") UserRepository userService, @Qualifier("InMemoryProductService") ProductRepository productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public Order createOrder(String userId) {

        User user = validateUser(userId);

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .orderItems(new ArrayList<>())
                .build();

        orders.add(order);
        return order;
    }

    @Override
    public List<Order> getOrders(String userId, String status) {

        validateUser(userId);

        if (status == null) {
            return orders.stream()
                    .filter(order -> order.getUserId().equals(userId))
                    .toList();
        } else {
            String normalizedStatus = status.toLowerCase();
            Boolean checkedOut;


            switch (normalizedStatus) {
                case "open" -> checkedOut = false;
                case "closed" -> checkedOut = true;
                default -> throw new IllegalArgumentException("Invalid status: " + status);
            }

            return orders.stream()
                    .filter(order -> order.getUserId().equals(userId))
                    .filter(order -> order.getIsCheckedOut().equals(checkedOut))
                    .toList();
        }
    }

    @Override
    public Order getOrderById(String userId, String orderId) {

        validateUser(userId);

        return orders.stream().filter(
                        o -> o.getId().equals(orderId) && o.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order with id "+ orderId + " not found for user "+ userId +" ."));
    }

    @Override
    public void addProductToOrder(String userId, String orderId, String productId, Integer quantity) {

        Product product = productService.findProductById(productId);

        Order order = getOrderById(userId, orderId);

        validateOrder(order.getId());

        if (order.getOrderItems().stream().anyMatch(item -> item.getProduct().getId().equals(product.getId()))) {
            throw new IllegalArgumentException("Product with id " + product.getId() + " already in order " + orderId + ".");
        }

        validateStock(productId, quantity);
        productService.updateStock(productId, product.getStock() - quantity);

        OrderItem orderItem = new OrderItem(product, quantity, product.getPrice() * quantity);
        order.getOrderItems().add(orderItem);

        calculatedTotal(order);
    }

    @Override
    public void updateProductQuantity(String userId, String orderId, String productId, Integer quantity) {

        Order order = getOrderById(userId, orderId);

        validateOrder(order.getId());

        OrderItem item = order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " not found in order " + orderId + "."));

        Product product = productService.findProductById(productId);

        int newQuantity = item.getQuantity() + quantity;

        if (quantity > 0) {
            validateStock(productId, quantity);
        }

        productService.updateStock(productId, product.getStock() - quantity);

        if (newQuantity <= 0) {
            order.getOrderItems().remove(item);
        } else {
            item.setQuantity(newQuantity);
            item.setSubtotal(item.getProduct().getPrice() * newQuantity);
        }

        calculatedTotal(order);
    }

    @Override
    public void removeProductFromOrder(String userId, String orderId, String productId) {

        Order order = getOrderById(userId, orderId);

        validateOrder(order.getId());

        OrderItem item = order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " not found in order " + orderId + "."));

        Product product = productService.findProductById(productId);

        productService.updateStock(productId, product.getStock() + item.getQuantity());
        order.getOrderItems().remove(item);

        calculatedTotal(order);
    }

    @Override
    public Order checkoutOrder(String userId, String orderId) {

        Order order = getOrderById(userId, orderId);

        validateOrder(order.getId());

        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout an empty order.");
        }

        double total = order.getTotal();
        double discount = (total > 100000.0) ? total * 0.05 : 0.0;

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        order.setDate(date);
        order.setDiscount(discount);
        order.setIsCheckedOut(true);

        return order;
    }

    @Override
    public void cancelOrder(String userId, String orderId) {

        Order orderToCancel = getOrderById(userId, orderId);

        validateOrder(orderToCancel.getId());

        for (OrderItem item : orderToCancel.getOrderItems()) {
            productService.updateStock(item.getProduct().getId(), item.getProduct().getStock() + item.getQuantity());
        }

        orders.remove(orderToCancel);
    }

    public User validateUser(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User id cant be null or empty.");
        }
        return userService.getUserById(userId);
    }

    public void validateOrder(String orderId) {
        if (orders.stream().anyMatch(o -> o.getId().equals(orderId) &&  o.getIsCheckedOut().equals(true))) {
            throw new IllegalArgumentException("Order is already checked out.");
        }
    }

    public void validateStock(String productId, Integer quantity) {
        Product product = productService.findProductById(productId);
        if (quantity > 0 && product.getStock() - quantity < 0) {
            throw new IllegalArgumentException("Not enough stock for product " + product.getName() + ".");
        }
    }

    public void calculatedTotal(Order order) {
        double total = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setTotal(total);
    }
}
