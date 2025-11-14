package co.com.store.shoppingCartAPI.service.user.datarepository;

import co.com.store.shoppingCartAPI.service.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
