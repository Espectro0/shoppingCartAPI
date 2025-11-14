package co.com.store.shoppingCartAPI.controller.dto;

import co.com.store.shoppingCartAPI.model.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDTO {

    @NotNull(message = "Product is required.")
    @Valid
    private ProductDTO product;

    @NotNull(message = "Quantity is required.")
    private Integer quantity;

    @NotNull(message = "Subtotal is required.")
    private Double subtotal;

    public static OrderItem toModel(OrderItemDTO orderItemDTO) {
        return new OrderItem(
                orderItemDTO.getProduct() != null ? ProductDTO.toModel(orderItemDTO.getProduct()) : null,
                orderItemDTO.getQuantity(),
                orderItemDTO.getSubtotal()
        );
    }

    public static OrderItemDTO fromModel(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getProduct() != null ? ProductDTO.fromModel(orderItem.getProduct()) : null,
                orderItem.getQuantity(),
                orderItem.getSubtotal()
        );
    }
}
