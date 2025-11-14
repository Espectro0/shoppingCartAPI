package co.com.store.shoppingCartAPI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {

    private String id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private Integer stock;
}
