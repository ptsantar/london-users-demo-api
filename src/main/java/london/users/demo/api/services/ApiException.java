package london.users.demo.api.services;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    private String endpoint;
    private String message;
    private HttpStatus status;

    public ApiException(String message, String endpoint, HttpStatus status){
        this.endpoint = endpoint;
        this.message =  message;
        this.status = status;
    }

    public String getEndpoint(){
        return endpoint;
    }

    public String getMessage(){
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
