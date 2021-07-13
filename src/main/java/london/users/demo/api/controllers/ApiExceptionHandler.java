package london.users.demo.api.controllers;

import london.users.demo.api.model.ApiResponse;
import london.users.demo.api.services.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class handles the application behavior when an {@link ApiException} is thrown.
 */
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleNotFoundApiException(
            ApiException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .endpoint(ex.getEndpoint()).build();
        return new ResponseEntity<>(response, ex.getStatus());
    }
}
