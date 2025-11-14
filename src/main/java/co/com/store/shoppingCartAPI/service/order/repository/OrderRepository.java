package co.com.store.shoppingCartAPI.service.order.repository;

import co.com.store.shoppingCartAPI.model.Order;
import co.com.store.shoppingCartAPI.model.Product;
import co.com.store.shoppingCartAPI.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {

    /**
     * Crea un nuevo pedido para un usuario
     * @param userId ID del usuario
     * @return Pedido creado
     */
    Order createOrder(String userId);

    /**
     * Obtiene la lista de pedidos de un usuario
     * @param userId ID del usuario
     * @param status Estado del pedido (abierto, cerrado)
     * @return Lista de pedidos del usuario
     */
    List<Order> getOrders(String userId, String status);

    /**
     * Obtiene un pedido por su ID
     * @param userId ID del usuario
     * @param orderId ID del pedido
     * @return Pedido correspondiente al ID
     */
    Order getOrderById(String userId, String orderId);

    /**
     * Agrega un producto a un pedido
     * @param userId ID del usuario
     * @param orderId ID del pedido
     * @param productId ID del producto a agregar
     * @param quantity Cantidad del producto a agregar
     */
    void addProductToOrder(String userId, String orderId, String productId, Integer quantity);

    /**
     * Actualiza la cantidad de un producto en un pedido
     * @param userId ID del usuario
     * @param orderId ID del pedido
     * @param productId ID del producto a actualizar
     * @param quantity Nueva cantidad del producto
     */
    void updateProductQuantity(String userId, String orderId, String productId, Integer quantity);

    /**
     * Elimina un producto de un pedido
     * @param userId ID del usuario
     * @param orderId ID del pedido
     * @param productId ID del producto a eliminar
     */
    void removeProductFromOrder(String userId, String orderId, String productId);

    /**
     * Finaliza un pedido existente
     * @param userId ID del usuario
     * @param orderId ID del pedido
     * @return Pedido finalizado
     */
    Order checkoutOrder(String userId, String orderId);

    /**
     * Cancela un pedido existente
     * @param userId ID del usuario
     * @param orderId ID del pedido
     */
    void cancelOrder(String userId, String orderId);
}
