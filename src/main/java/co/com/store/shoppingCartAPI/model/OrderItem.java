package co.com.store.shoppingCartAPI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItem {
    private Product product;
    private Integer quantity;
    private Double subtotal;
}
