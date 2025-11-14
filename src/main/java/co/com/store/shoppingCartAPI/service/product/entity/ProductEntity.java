package co.com.store.shoppingCartAPI.service.product.entity;

import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.service.order.entity.OrderItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    private String id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String description;
    private String category;
    private Double price;
    private Integer stock;

    @OneToMany(mappedBy = "product")
    private List<OrderItemEntity> orderItems;

    public static ProductEntity fromModel(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setCategory(product.getCategory());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        return entity;
    }

    public static Product toModel(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getCategory(),
                productEntity.getPrice(),
                productEntity.getStock()
        );
    }

    public static List<Product> toModelList(List<ProductEntity> products) {
        return products.stream()
                .map(ProductEntity::toModel)
                .toList();
    }
}
