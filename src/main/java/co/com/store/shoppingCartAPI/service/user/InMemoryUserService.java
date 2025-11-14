package co.com.store.shoppingCartAPI.service.user;

import co.com.store.shoppingCartAPI.model.User;
import co.com.store.shoppingCartAPI.service.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("inMemoryUserService")
public class InMemoryUserService implements UserRepository {

    private List<User> users = new ArrayList<>();

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(String userId) {
        return users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found."));
    }

    @Override
    public User createUser(User user) {
        if (users.stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists.");
        }

        user.setId(UUID.randomUUID().toString());
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(String userId, User user) {
        User existingUser = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found."));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return existingUser;
    }

    @Override
    public void deleteUser(String userId) {
        User userToDelete = users.stream()
                .filter(u-> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found."));
        users.remove(userToDelete);
    }
}
