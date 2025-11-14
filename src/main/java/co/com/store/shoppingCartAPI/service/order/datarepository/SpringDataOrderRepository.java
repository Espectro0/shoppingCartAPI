package co.com.store.shoppingCartAPI.service.order.datarepository;

import co.com.store.shoppingCartAPI.service.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {

}
