package co.com.store.shoppingCartAPI.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    private String id;
    private String userId;
    private List<OrderItem> orderItems;

    @Builder.Default
    private Double total = 0.0;

    @Builder.Default
    private Double discount = 0.0;

    @Builder.Default
    private String date = "";

    @Builder.Default
    private Boolean isCheckedOut = false;
}
