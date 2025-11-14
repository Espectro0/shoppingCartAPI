package co.com.store.shoppingCartAPI.service.user;

import co.com.store.shoppingCartAPI.model.User;
import co.com.store.shoppingCartAPI.service.user.datarepository.SpringDataUserRepository;
import co.com.store.shoppingCartAPI.service.user.entity.UserEntity;
import co.com.store.shoppingCartAPI.service.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("mySQLUserService")
@AllArgsConstructor
public class MySQLUserService implements UserRepository {

    private final SpringDataUserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return UserEntity.toModelList(userRepository.findAll());
    }

    @Override
    public User getUserById(String userId) {
        return UserEntity.toModel(
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found.")));
    }

    @Override
    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());

        if (userRepository.existsById(user.getId()) || userRepository.existsByEmail(user.getEmail()) || userRepository.existsByName(user.getName())) {
            throw new IllegalArgumentException("This user already exists in the database.");
        }

        userRepository.save(UserEntity.fromModel(user));
        return user;
    }

    @Override
    public User updateUser(String userId, User user) {
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found."));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        UserEntity updatedUser = userRepository.save(existingUser);

        return UserEntity.toModel(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found."));

        userRepository.delete(userToDelete);
    }
}
