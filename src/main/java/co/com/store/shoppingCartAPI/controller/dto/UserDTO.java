package co.com.store.shoppingCartAPI.controller.dto;

import co.com.store.shoppingCartAPI.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private String id;

    @NotNull(message = "Name is required.")
    private String name;

    @NotNull(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    public static User toModel(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail()
        );
    }

    public static UserDTO fromModel(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static List<UserDTO> fromModelList(List<User> users) {
        return users.stream()
                .map(UserDTO::fromModel)
                .toList();
    }
}
