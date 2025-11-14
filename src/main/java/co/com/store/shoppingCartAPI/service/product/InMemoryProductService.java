package co.com.store.shoppingCartAPI.service.product;

import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.service.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("InMemoryProductService")
public class InMemoryProductService implements ProductRepository {

    private List<Product> products = new ArrayList<>();

    @Override
    public List<Product> getProducts(String category, Double minPrice, Double maxPrice) {
        return products.stream()
                .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .toList();
    }


    @Override
    public Product findProductById(String id) {
        return products.stream().filter(
                p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found."));
    }

    @Override
    public Product createProduct(Product product) {
        if (products.stream().anyMatch(p -> p.getName().equals(product.getName()))) {
            throw new IllegalArgumentException("This product already exists.");
        }

        if (product.getStock() < 0 || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Stock or price must be greater than zero.");
        }

        validateField(product.getName(), "name");
        validateField(product.getDescription(), "description");
        validateField(product.getCategory(), "category");

        product.setId(UUID.randomUUID().toString());
        products.add(product);
        return product;
    }

    @Override
    public void deleteProduct(String id) {
        Product productToDelete = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found."));
        products.remove(productToDelete);
    }

    @Override
    public void updateStock(String id, Integer quantity) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                if (quantity < 0) {
                    throw new IllegalArgumentException("Stock quantity can't be negative.");
                }
                product.setStock(quantity);
                return;
            }
        }
    }

    private void validateField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invalid " + fieldName + " value.");
        }
    }
}
