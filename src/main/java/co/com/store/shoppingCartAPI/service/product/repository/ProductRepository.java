package co.com.store.shoppingCartAPI.service.product.repository;

import co.com.store.shoppingCartAPI.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {

    /**
     * Obtiene la lista de productos, filtrando por categoria y rango de precios si se proporcionan
     * @param category Categoria por la que se quiere filtrar (opcional)
     * @param minPrice Precio minimo del rango (opcional)
     * @param maxPrice Precio maximo del rango (opcional)
     * @return Lista de productos que cumplen con los filtros
     */
    List<Product> getProducts(String category, Double minPrice, Double maxPrice);

    /**
     * Busca el id de un producto, y si lo encuentra devuelve el producto
     * @param id Corresponde al id del producto que se quiere buscar
     * @return Producto encontrado en el sistema
     */
    Product findProductById(String id);

    /**
     * Crea un nuevo producto en el sistema
     * @param product Datos del producto a crear
     * @return Producto creado
     */
    Product createProduct(Product product);

    /**
     * Elimina un producto del sistema
     * @param id Id del producto que se quiere eliminar
     */
    void deleteProduct(String id);

    /**
     * Busca el producto por su id y actualiza su stock
     * @param id El id del producto que se quiere modificar
     * @param quantity La nueva cantidad que se quiere asignar al producto
     */
    void updateStock(String id, Integer quantity);
}
