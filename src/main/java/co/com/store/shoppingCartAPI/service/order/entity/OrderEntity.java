package co.com.store.shoppingCartAPI.service.order.entity;

import co.com.store.shoppingCartAPI.model.Order;
import co.com.store.shoppingCartAPI.service.product.entity.ProductEntity;
import co.com.store.shoppingCartAPI.service.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems;

    private Double total;
    private Double discount;
    private String date;
    private Boolean isCheckedOut;

    public static OrderEntity fromModel(Order order, UserEntity user) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUser(user);

        if (order.getOrderItems() != null) {
            List<OrderItemEntity> items = order.getOrderItems().stream()
                    .map(item -> {
                        OrderItemEntity itemEntity = new OrderItemEntity();
                        itemEntity.setId(UUID.randomUUID().toString());
                        itemEntity.setOrder(entity);
                        itemEntity.setQuantity(item.getQuantity());
                        itemEntity.setSubtotal(item.getSubtotal());

                        ProductEntity ref = new ProductEntity();

                        ref.setId(item.getProduct().getId());
                        itemEntity.setProduct(ref);

                        return itemEntity;
                    })
                    .toList();
            entity.setOrderItems(items);
        }

        entity.setTotal(order.getTotal());
        entity.setDiscount(order.getDiscount());
        entity.setDate(order.getDate());
        entity.setIsCheckedOut(order.getIsCheckedOut());
        return entity;
    }

    public static Order toModel(OrderEntity orderEntity) {
        return new Order(
                orderEntity.getId(),
                orderEntity.getUser().getId(),
                orderEntity.getOrderItems() != null ?
                        orderEntity.getOrderItems().stream()
                                .map(OrderItemEntity::toModel)
                                .toList() : null,
                orderEntity.getTotal(),
                orderEntity.getDiscount(),
                orderEntity.getDate(),
                orderEntity.getIsCheckedOut()
        );
    }

    public static List<Order> toModelList(List<OrderEntity> orders) {
        return orders.stream()
                .map(OrderEntity::toModel)
                .toList();
    }

}
