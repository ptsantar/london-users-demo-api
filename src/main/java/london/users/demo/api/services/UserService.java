package london.users.demo.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import london.users.demo.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    /**
     * The URL to the 3rd-party API
     */
    private final String apiUrl;

    /**
     * The REST template to consume the 3rd-party API
     */
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplate restTemplate, final @Value("${api_url}") String url) {
        this.restTemplate = restTemplate;
        this.apiUrl = url;
    }

    /**
     * Sends a GET request to /users/{id} endpoint to retrieve the user with the specified id
     * @return the user retrieved by the 3rd-party API
     */
    public User getUser(Integer id) {
        try {
            ResponseEntity<User> responseEntity = restTemplate.exchange(
                    apiUrl + "user/" + id,
                    HttpMethod.GET,
                    null,
                    User.class);
            return responseEntity.getBody();
        }catch (HttpClientErrorException e){
            throw new ApiException(e.getMessage(), "endpoint: user/{id}", e.getStatusCode());
        }
    }

    /**
     * Sends a GET request to /users/{id} endpoint to retrieve the all the users provided by the API
     * @return all the users retrieved by the 3rd-party API
     */
    public List<User> getUsers() {
        try {
            // sent the GET request
            ResponseEntity<User[]> responseEntity =
                    restTemplate.exchange(apiUrl+"users", HttpMethod.GET, null, User[].class);
            // check if the body is not null
            Optional<User[]> body = Optional.ofNullable(responseEntity.getBody());
            // if not parse the body and return the user list
            if(body.isPresent()){
                return Arrays.stream(responseEntity.getBody()).collect(Collectors.toList());
            }
            // else throw an exception
            throw new ApiException("Body was null", "endpoint: user/{id}", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(HttpStatusCodeException e) {
            throw new ApiException(e.getMessage(), "Endpoint /users", e.getStatusCode());
        }
    }
}
