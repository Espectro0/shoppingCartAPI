package co.com.store.shoppingCartAPI.usecase;

import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.service.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductUseCase {

    private final ProductRepository productRepository;

    public ProductUseCase(@Qualifier("mySQLProductService") ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(String category, Double minPrice, Double maxPrice) {
        return productRepository.getProducts(category, minPrice, maxPrice);
    }

    public Product findProductById(String id) {
        return  productRepository.findProductById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.createProduct(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteProduct(id);
    }

    public void updateStock(String id, Integer quantity) {
        productRepository.updateStock(id, quantity);
    }
}
