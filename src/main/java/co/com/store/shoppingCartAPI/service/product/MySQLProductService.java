package co.com.store.shoppingCartAPI.service.product;

import co.com.store.shoppingCartAPI.controller.exception.BadRequestException;
import co.com.store.shoppingCartAPI.controller.exception.ConflictException;
import co.com.store.shoppingCartAPI.controller.exception.ResourceNotFoundException;
import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.service.product.datarepository.SpringDataProductRepository;
import co.com.store.shoppingCartAPI.service.product.entity.ProductEntity;
import co.com.store.shoppingCartAPI.service.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("mySQLProductService")
@AllArgsConstructor
public class MySQLProductService implements ProductRepository {

    private final SpringDataProductRepository productRepository;

    @Override
    public List<Product> getProducts(String category, Double minPrice, Double maxPrice) {
        return ProductEntity.toModelList(productRepository.findAll().stream()
                .filter(p -> category == null || p.getCategory().equals(category))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .toList()
        );
    }

    @Override
    public Product findProductById(String id) {
        return ProductEntity.toModel(
                productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found.")
        ));
    }

    @Override
    public Product createProduct(Product product) {
        product.setId(UUID.randomUUID().toString());

        if (productRepository.existsById(product.getId()) || productRepository.existsByName(product.getName())) {
            throw new ConflictException("A product with the same name already exists.");
        }

        productRepository.save(ProductEntity.fromModel(product));

        return product;
    }

    @Override
    public void deleteProduct(String id) {
        ProductEntity productToDelete = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));

        productRepository.delete(productToDelete);
    }

    @Override
    public void updateStock(String id, Integer quantity) {
        if (quantity < 0) {
            throw new BadRequestException("Stock quantity can't be negative.");
        }

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with id " + id + " not found.");
        }

        productRepository.updateStockById(id, quantity);
    }
}
