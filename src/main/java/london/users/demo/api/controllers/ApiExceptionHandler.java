package london.users.demo.api.controllers;

import london.users.demo.api.model.ApiResponse;
import london.users.demo.api.services.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleNotFoundApiException(
            ApiException ex) {
        ApiResponse response = ApiResponse.builder()
                .message("Something went wrong with the API")
                .endpoint(ex.getEndpoint()).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
