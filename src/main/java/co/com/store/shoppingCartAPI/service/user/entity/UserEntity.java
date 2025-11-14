package co.com.store.shoppingCartAPI.service.user.entity;

import co.com.store.shoppingCartAPI.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"name"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    private String id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String email;

    public static UserEntity fromModel(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        return entity;
    }

    public static User toModel(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }

    public static List<User> toModelList(List<UserEntity> users) {
        return users.stream()
                .map(UserEntity::toModel)
                .toList();
    }

}
