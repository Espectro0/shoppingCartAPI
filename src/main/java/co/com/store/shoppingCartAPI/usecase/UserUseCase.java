package co.com.store.shoppingCartAPI.usecase;

import co.com.store.shoppingCartAPI.model.User;
import co.com.store.shoppingCartAPI.service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserUseCase {

    private final UserRepository userRepository;

    public UserUseCase(@Qualifier("mySQLUserService") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public void updateUser(String userId, User user) {
        userRepository.updateUser(userId, user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
    }
}
