package co.com.store.shoppingCartAPI.controller;

import co.com.store.shoppingCartAPI.controller.dto.UserDTO;
import co.com.store.shoppingCartAPI.usecase.UserUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = UserDTO.fromModelList(userUseCase.getUsers());

        return ResponseEntity
                .ok()
                .body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        UserDTO user = UserDTO.fromModel(userUseCase.getUserById(userId));

        return ResponseEntity
                .ok()
                .body(user);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = UserDTO.fromModel(userUseCase.createUser(UserDTO.toModel(userDTO)));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO) {
        userUseCase.updateUser(userId, UserDTO.toModel(userDTO));

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userUseCase.deleteUser(userId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
