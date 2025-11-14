package co.com.store.shoppingCartAPI.controller.exception;

import co.com.store.shoppingCartAPI.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de validación
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        if (message.isEmpty()) {
            message = "Arguments Validation Error.";
        }
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Maneja las excepciones de IllegalArgumenException
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Maneja cuando un parámetro obligatorio del request falta.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = String.format("Missing required parameter: %s", ex.getParameterName());
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Maneja cuando un parámetro no se puede convertir (por tipo).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "Parameter '%s' must be of type %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Maneja excepciones de NullPointerException
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "An invalid or missing value was encountered. Please verify your request.");
    }


    /**
     * Maneja cualquier otra excepción no capturada especificamente
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "An internal error has occurred. Please contact an administrator.";
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }

    /**
     * Contryye una respuesta de error con la fecha, el status, error y mensaje.
     */

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        ErrorResponse error = ErrorResponse.builder()
                .date(timestamp)
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(error);
    }

}
