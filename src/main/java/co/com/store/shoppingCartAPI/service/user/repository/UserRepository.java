package co.com.store.shoppingCartAPI.service.user.repository;

import co.com.store.shoppingCartAPI.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {

    /**
     * Obtiene la lista de todos los usuarios
     * @return Lista de usuarios
     */
    List<User> getUsers();

    /**
     * Obtiene un usuario por su ID
     * @param userId ID del usuario
     * @return Usuario correspondiente al ID
     */
    User getUserById(String userId);

    /**
     * Crea un nuevo usuario
     * @param user Datos del usuario a crear
     * @return Usuario creado
     */
    User createUser(User user);

    /**
     * Actualiza un usuario existente
     * @param userId ID del usuario a actualizar
     * @param user Nuevos datos del usuario
     * @return Usuario actualizado
     */
    User updateUser(String userId, User user);

    /**
     * Elimina un usuario por su ID
     * @param userId ID del usuario a eliminar
     */
    void deleteUser(String userId);

}
