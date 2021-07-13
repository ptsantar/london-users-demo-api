package london.users.demo.api.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * It contains the code to return the instructions from the API
 */
@Service
public class InstructionService {

    /**
     * The URL to the 3rd-party API
     */
    private final String apiUrl;

    /**
     * The REST template to consume the 3rd-party API
     */
    private final RestTemplate restTemplate;

    @Autowired
    public InstructionService(RestTemplate restTemplate, final @Value("${api_url}") String url) {
        this.restTemplate = restTemplate;
        this.apiUrl = url;
    }

    /**
     * Sends a GET request to /instructions endpoint to retrieve the instructions
     * @return the response of the 3rd-party API
     */
    public String getInstructions(){
        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(apiUrl+"instructions", HttpMethod.GET, null, String.class);
            return responseEntity.getBody();
        } catch(HttpStatusCodeException e) {
            throw new ApiException("Endpoint /instructions");
        }
    }
}
