package london.users.demo.api.services;

public class ApiException extends RuntimeException{
    private String endpoint;

    public ApiException(String message){
        this.endpoint = message;
    }

    public String getEndpoint(){
        return endpoint;
    }
}
