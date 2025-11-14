package co.com.store.shoppingCartAPI.service.order.entity;

import co.com.store.shoppingCartAPI.model.OrderItem;
import co.com.store.shoppingCartAPI.service.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    private Integer quantity;
    private Double subtotal;

    public static OrderItemEntity fromModel(OrderItem orderItem, OrderEntity order, ProductEntity product) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setOrder(order);
        entity.setProduct(product);
        entity.setQuantity(orderItem.getQuantity());
        entity.setSubtotal(orderItem.getSubtotal());
        return entity;
    }

    public static OrderItem toModel(OrderItemEntity orderItemEntity) {
        return new OrderItem(
                orderItemEntity.getProduct() != null ?
                        ProductEntity.toModel(orderItemEntity.getProduct()) : null,
                orderItemEntity.getQuantity(),
                orderItemEntity.getSubtotal()
        );
    }
}