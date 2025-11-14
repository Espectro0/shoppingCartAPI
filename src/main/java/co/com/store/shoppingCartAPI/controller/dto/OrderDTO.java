package co.com.store.shoppingCartAPI.controller.dto;

import co.com.store.shoppingCartAPI.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderDTO {

    private String id;

    @NotNull(message = "User id is required.")
    private String userId;

    @NotNull(message = "Order items are required.")
    private List<OrderItemDTO> orderItems;

    private Double total;
    private Double discount;
    private String date;
    private Boolean isCheckedOut;

    public static Order toModel(OrderDTO orderDTO) {
        return new Order(
                orderDTO.getId(),
                orderDTO.getUserId(),
                orderDTO.getOrderItems() != null
                        ? orderDTO.getOrderItems().stream()
                        .map(OrderItemDTO::toModel)
                        .toList()
                        : null,
                orderDTO.getTotal() != null ? orderDTO.getTotal() : 0.0,
                orderDTO.getDiscount() != null ? orderDTO.getDiscount() : 0.0,
                orderDTO.getDate() != null ? orderDTO.getDate() : "",
                orderDTO.getIsCheckedOut() != null ? orderDTO.getIsCheckedOut() : false
        );
    }

    public static OrderDTO fromModel(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getOrderItems() != null
                        ? order.getOrderItems().stream()
                        .map(OrderItemDTO::fromModel)
                        .toList()
                        : null,
                order.getTotal(),
                order.getDiscount(),
                order.getDate(),
                order.getIsCheckedOut()
        );
    }

    public static List<OrderDTO> fromModelList(List<Order> orders) {
        return orders.stream()
                .map(OrderDTO::fromModel)
                .toList();
    }
}
