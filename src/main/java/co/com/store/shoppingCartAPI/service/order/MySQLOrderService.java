package co.com.store.shoppingCartAPI.service.order;

import co.com.store.shoppingCartAPI.controller.exception.BadRequestException;
import co.com.store.shoppingCartAPI.controller.exception.ConflictException;
import co.com.store.shoppingCartAPI.controller.exception.ResourceNotFoundException;
import co.com.store.shoppingCartAPI.model.Order;
import co.com.store.shoppingCartAPI.model.OrderItem;
import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.model.User;
import co.com.store.shoppingCartAPI.service.order.datarepository.SpringDataOrderRepository;
import co.com.store.shoppingCartAPI.service.order.entity.OrderEntity;
import co.com.store.shoppingCartAPI.service.order.entity.OrderItemEntity;
import co.com.store.shoppingCartAPI.service.order.repository.OrderRepository;
import co.com.store.shoppingCartAPI.service.product.entity.ProductEntity;
import co.com.store.shoppingCartAPI.service.product.repository.ProductRepository;
import co.com.store.shoppingCartAPI.service.user.entity.UserEntity;
import co.com.store.shoppingCartAPI.service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("MySQLOrderService")
public class MySQLOrderService implements OrderRepository {


    private final UserRepository userService;
    private final ProductRepository productService;
    private final SpringDataOrderRepository orderRepository;

    public MySQLOrderService(@Qualifier("mySQLUserService") UserRepository userService, @Qualifier("mySQLProductService") ProductRepository productService, SpringDataOrderRepository orderRepository) {
        this.userService = userService;
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(String userId) {

        userService.getUserById(userId);

        UserEntity userEntity =  UserEntity.fromModel(userService.getUserById(userId));

            Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .orderItems(new ArrayList<>())
                .total(0.0)
                .discount(0.0)
                .date("")
                .isCheckedOut(false)
                .build();

        orderRepository.save(OrderEntity.fromModel(order, userEntity));
        return order;
    }

    @Override
    public List<Order> getOrders(String userId, String status) {

        userService.getUserById(userId);

        List<OrderEntity> orders = orderRepository.findAll().stream()
                .filter(o -> o.getUser().getId().equals(userId))
                .toList();

        if (status == null) {
            return OrderEntity.toModelList(orders);
        } else {
            String normalizedStatus = status.toLowerCase();
            Boolean checkedOut;

            switch (normalizedStatus) {
                case "open", "abierto" -> checkedOut = false;
                case "closed", "cerrado" -> checkedOut = true;
                default -> throw new BadRequestException("Invalid status: " + status);
            }

            return OrderEntity.toModelList(orders.stream()
                    .filter(o -> o.getIsCheckedOut().equals(checkedOut))
                    .toList());
        }
    }

    @Override
    public Order getOrderById(String userId, String orderId) {

        userService.getUserById(userId);

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id "+ orderId + " not found for user "+ userId +" ."));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Order with id "+ orderId + " not found for user "+ userId +" .");
        }

        return OrderEntity.toModel(order);
    }

    @Transactional
    @Override
    public void addProductToOrder(String userId, String orderId, String productId, Integer quantity) {

        validateStock(productId, quantity);

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found."));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Order with id " + orderId + " not found for user " + userId + ".");
        }

        validateOrder(orderId);

        if (order.getOrderItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId))) {
            throw new ConflictException("Product with id " + productId + " already in order " + orderId + ".");
        }

        Product product = productService.findProductById(productId);
        productService.updateStock(productId, product.getStock() - quantity);

        OrderItemEntity newItem = new OrderItemEntity();
        newItem.setId(UUID.randomUUID().toString());
        newItem.setOrder(order);
        newItem.setProduct(ProductEntity.fromModel(productService.findProductById(productId)));
        newItem.setQuantity(quantity);
        newItem.setSubtotal(product.getPrice() * quantity);

        order.getOrderItems().add(newItem);

        double total = order.getOrderItems().stream()
                .mapToDouble(OrderItemEntity::getSubtotal)
                .sum();
        order.setTotal(total);
    }

    @Transactional
    @Override
    public void updateProductQuantity(String userId, String orderId, String productId, Integer quantity) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found."));
        validateOrder(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Order with id " + orderId + " not found for user " + userId + ".");
        }

        OrderItemEntity item = order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in order " + orderId + "."));

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

        double total = order.getOrderItems().stream()
                .mapToDouble(OrderItemEntity::getSubtotal)
                .sum();
        order.setTotal(total);
    }

    @Transactional
    @Override
    public void removeProductFromOrder(String userId, String orderId, String productId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found."));
        validateOrder(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Order with id " + orderId + " not found for user " + userId + ".");
        }

        OrderItemEntity item = order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in order " + orderId + "."));

        Product product = productService.findProductById(productId);

        productService.updateStock(productId, product.getStock() + item.getQuantity());
        order.getOrderItems().remove(item);

        double total = order.getOrderItems().stream()
                .mapToDouble(OrderItemEntity::getSubtotal)
                .sum();
        order.setTotal(total);
    }

    @Transactional
    @Override
    public Order checkoutOrder(String userId, String orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found."));

        validateOrder(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Order with id " + orderId + " not found for user " + userId + ".");
        }

        if (order.getOrderItems().isEmpty()) {
            throw new BadRequestException("Cannot checkout an empty order.");
        }

        double total = order.getTotal();
        double discount = (total > 100000.0) ? total * 0.05 : 0.0;

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        order.setDate(date);
        order.setDiscount(discount);
        order.setIsCheckedOut(true);

        return OrderEntity.toModel(order);
    }

    @Override
    public void cancelOrder(String userId, String orderId) {

        Order order = getOrderById(userId, orderId);
        validateOrder(orderId);

        for (OrderItem item : order.getOrderItems()) {
            productService.updateStock(item.getProduct().getId(), item.getProduct().getStock() + item.getQuantity());
        }

        orderRepository.deleteById(orderId);
    }

    private void validateOrder(String orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found."));

        if (orderEntity.getIsCheckedOut()) {
            throw new ConflictException("Order is already checked out.");
        }
    }

    private void validateStock(String productId, Integer quantity) {
        Product product = productService.findProductById(productId);

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0.");
        }

        if (product.getStock() < quantity) {
            throw new BadRequestException("Not enough stock for product " + product.getName() + ".");
        }
    }
}
