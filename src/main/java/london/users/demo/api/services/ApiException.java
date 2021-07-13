package london.users.demo.api.services;

import org.springframework.http.HttpStatus;

/**
 * Custom exception to handle errors during the requests
 */
public class ApiException extends RuntimeException{
    /**
     * The endpoint that caused the exception
     */
    private String endpoint;

    /**
     * The message to be displayed
     */
    private String message;

    /**
     * The status to return
     */
    private HttpStatus status;

    /**
     * Constructor for the class
     * @param message message to be displayed
     * @param endpoint endpoint that caused the exception
     * @param status the status to return
     */
    public ApiException(String message, String endpoint, HttpStatus status){
        this.endpoint = endpoint;
        this.message =  message;
        this.status = status;
    }

    /**
     * Getter function for the {@link ApiException#endpoint}
     * @return message regarding the endpoint
     */
    public String getEndpoint(){
        return endpoint;
    }

    /**
     * Getter function for the {@link ApiException#message}
     * @return message that describes the error
     */
    public String getMessage(){
        return message;
    }

    /**
     * Getter function for the {@link ApiException#status}
     * @return the status of the request
     */
    public HttpStatus getStatus() {
        return status;
    }
}
