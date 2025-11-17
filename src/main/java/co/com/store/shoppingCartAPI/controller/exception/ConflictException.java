package co.com.store.shoppingCartAPI.controller.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
