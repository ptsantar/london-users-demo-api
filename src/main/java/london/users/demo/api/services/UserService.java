package london.users.demo.api.services;

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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements the "business logic"  (related to the users)
 */
@Service
public class UserService {

    /**
     * An internal class to maintain a few city coordinates to test the code
     */
    private static class CityCoordinates{
        Map<String, Double[]> coordinates = new HashMap<>();

        public Double[] getCoordinates(String city){
            return coordinates.get(city);
        }

        public boolean containsCity(String city){
            return coordinates.containsKey(city);
        }

        public void add(String city, Double[] cooord){
            coordinates.put(city, cooord);
        }
    }

    /**
     * Maintains city coordinates
     */
    private final static CityCoordinates coordinates = new CityCoordinates();
    // populate the coordinates with a few cities
    static {
        coordinates.add("London", new Double[]{51.5074, -0.1272}); //51.5074° N, 0.1272° W
        coordinates.add("Paris", new Double[]{48.8565, 2.3064}); //48.8565° N, 2.3064° E
        coordinates.add("Athens", new Double[]{37.9787, 23.7514}); //37.9787° N, 23.7514° E
    }


    /**
     * The URL to the 3rd-party API
     */
    private final String apiUrl;

    /**
     * The REST template to consume the 3rd-party API
     */
    private final RestTemplate restTemplate;

    /**
     * Service constructor; the parameters are autowired
     * @param restTemplate the template that consumes the 3rd-party API
     * @param url the url of the 3rd-party API
     */
    @Autowired
    public UserService(RestTemplate restTemplate, final @Value("${api_url}") String url) {
        this.restTemplate = restTemplate;
        this.apiUrl = url;
    }

    /**
     * Sends a GET request to /users/{id} endpoint to retrieve the user with the specified id
     * @param id the id of the user
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
            ResponseEntity<User[]> responseEntity = restTemplate.exchange(
                    apiUrl+"users",
                    HttpMethod.GET,
                    null,
                    User[].class);
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

    /**
     * Sends a GET request to /city/{city}/users endpoint to retrieve the all the users that live in the specified
     * city provided by the API
     * @param cityName the name of the city
     * @return all the users retrieved by the 3rd-party API
     */
    public List<User> getUsersByCity(String cityName) {
        try {
            // sent the GET request
            ResponseEntity<User[]> responseEntity =
                    restTemplate.exchange(
                            apiUrl+"/city/"+ URLEncoder.encode(cityName, StandardCharsets.UTF_8) + "/users",
                            HttpMethod.GET,
                            null,
                            User[].class);
            // check if the body is not null
            Optional<User[]> body = Optional.ofNullable(responseEntity.getBody());
            // if not parse the body and return the user list
            if(body.isPresent()){
                return Arrays.stream(responseEntity.getBody()).collect(Collectors.toList());
            }
            // else throw an exception
            throw new ApiException("Body was null", "endpoint: /city/{city}/users", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(HttpStatusCodeException e) {
            throw new ApiException(e.getMessage(), "Endpoint /city/{city}/users", e.getStatusCode());
        }
    }

    /**
     * Given a city name and a radius (in meters) finds all the users that are registered in the city or/and
     * their coordinates are inside the radius. The distance is calculated using the city coordinates as they
     * are provided by Google.
     *
     * Note: only a couple of cities are currently supported. If a city is not supported an exception is thrown
     *
     * @param cityName the name of the city
     * @param radius the radius in meters
     * @return list of users that are registered in the city or their distance (based on their coordinates) is inside the specified radius
     */
    public List<User> getUsersByCityRadius(String cityName, double radius) {
        List<User> londoners = getUsersByCity(cityName);
        List<User> usersByRadius = findUsersInRadius(cityName, radius);
        return Stream.of(londoners, usersByRadius)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all the users provided by the 3rd-Party API and then finds the users that their distance from the
     * city center is inside the specified radius. If the city is not supported, throws an exception with a
     * corresponding message
     * @param cityName the name of the city
     * @param radius the radius in meters
     * @return a list of users that are inside the specified radius of the city
     */
    private List<User> findUsersInRadius(String cityName, double radius) {
        // check if city is supported
        if(coordinates.containsCity(cityName) ) {
            // get the city coordinates
            Double[] cityCoordinates = coordinates.getCoordinates(cityName);
            // filter the users based on their distance from the city center
            List<User> filteredUsers = getUsers().stream().filter(
                user -> distance(
                    cityCoordinates[0],
                    user.getLatitude(),
                    cityCoordinates[1],
                    user.getLongitude(),
                    0,0
                ) <= radius
            ).collect(Collectors.toList());
            return filteredUsers;
        }
        // if the city is not supported yet throw an exception
        throw new ApiException(
                "City "+cityName+ " is not supported yet.",
                "Endpoint city-radius/{cityName}",
                HttpStatus.NOT_IMPLEMENTED);
    }


    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * Source: https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     *
     * @param lat1 Start point latitude
     * @param lat2 End point latitude
     * @param lon1 Start point longitude
     * @param lon2 End point longitude
     * @param el1 Start altitude in meters
     * @param el2 End altitude in meters
     * @return Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
