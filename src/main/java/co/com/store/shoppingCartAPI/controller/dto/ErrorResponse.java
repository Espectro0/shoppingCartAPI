package co.com.store.shoppingCartAPI.controller.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String date;
    private Integer status;
    private String error;
    private String message;
}
