package co.com.store.shoppingCartAPI.service.product.datarepository;

import co.com.store.shoppingCartAPI.service.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, String> {
    boolean existsByName(String name);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE ProductEntity p SET p.stock = :stock WHERE p.id = :id")
    void updateStockById(@Param("id") String id, @Param("stock") Integer stock);

}
