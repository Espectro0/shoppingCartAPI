package co.com.store.shoppingCartAPI.controller;

import co.com.store.shoppingCartAPI.controller.dto.OrderDTO;
import co.com.store.shoppingCartAPI.usecase.OrderUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/carts")
@AllArgsConstructor
public class OrderController {
    private final OrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@PathVariable String userId) {
        OrderDTO createdOrder = OrderDTO.fromModel(orderUseCase.createOrder(userId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable String userId, @RequestParam(required = false) String status) {
        List<OrderDTO> orders = OrderDTO.fromModelList(orderUseCase.getOrders(userId, status));

        return ResponseEntity
                .ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String userId, @PathVariable String orderId) {
        OrderDTO order = OrderDTO.fromModel(orderUseCase.getOrderById(userId, orderId));

        return ResponseEntity
                .ok(order);
    }

    @PostMapping("/{orderId}/item/{productId}")
    public ResponseEntity<Void> addProductToOrder(@PathVariable String userId, @PathVariable String orderId, @PathVariable String productId, @RequestParam Integer quantity) {

        orderUseCase.addProductToOrder(userId, orderId, productId, quantity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{orderId}/item/update/{productId}")
    public ResponseEntity<Void> updateProductQuantity(@PathVariable String userId, @PathVariable String orderId, @PathVariable String productId, @RequestParam Integer quantity) {

        orderUseCase.updateProductQuantity(userId, orderId, productId, quantity);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{orderId}/item")
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable String userId, @PathVariable String orderId, @RequestParam String productId) {

        orderUseCase.removeProductFromOrder(userId, orderId, productId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{orderId}/checkout")
    public ResponseEntity<OrderDTO> checkoutOrder(@PathVariable String userId, @PathVariable String orderId) {

        OrderDTO checkedOut = OrderDTO.fromModel(orderUseCase.checkoutOrder(userId, orderId));

        return ResponseEntity
                .ok(checkedOut);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable String userId, @PathVariable String orderId) {

        orderUseCase.cancelOrder(userId, orderId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
