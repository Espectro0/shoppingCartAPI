package co.com.store.shoppingCartAPI.controller.dto;


import co.com.store.shoppingCartAPI.model.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {

    private String id;

    @NotNull(message = "Name is required.")
    private String name;

    @NotNull(message = "Description is required.")
    private String description;

    @NotNull(message = "Category is required.")
    private String category;

    @NotNull(message = "Price is required.")
    private Double price;

    @NotNull(message = "Stock is required.")
    private Integer stock;

    public static Product toModel(ProductDTO productDTO) {
        return new Product(
                productDTO.getId(),
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getCategory(),
                productDTO.getPrice(),
                productDTO.getStock()
        );
    }

    public static ProductDTO fromModel(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getStock()
        );
    }

    public static List<ProductDTO> fromModelList(List<Product> products) {
        return products.stream()
                .map(ProductDTO::fromModel)
                .toList();
    }
}
