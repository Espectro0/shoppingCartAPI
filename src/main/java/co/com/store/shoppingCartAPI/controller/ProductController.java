package co.com.store.shoppingCartAPI.controller;

import co.com.store.shoppingCartAPI.controller.dto.ProductDTO;
import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.usecase.ProductUseCase;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductUseCase productUseCase;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<ProductDTO> products = ProductDTO.fromModelList(productUseCase.getProducts(category, minPrice, maxPrice));

        return ResponseEntity
                .ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable String id) {
        ProductDTO product = ProductDTO.fromModel(productUseCase.findProductById(id));

        return ResponseEntity
                .ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = ProductDTO.fromModel(productUseCase.createProduct(ProductDTO.toModel(productDTO)));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStock(@PathVariable String id, @RequestParam @NotNull Integer quantity) {
        productUseCase.updateStock(id, quantity);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productUseCase.deleteProduct(id);

        return ResponseEntity
                .noContent()
                .build();
    }

}
