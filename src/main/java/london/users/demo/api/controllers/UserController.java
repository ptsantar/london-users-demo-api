package london.users.demo.api.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import london.users.demo.api.model.User;
import london.users.demo.api.services.ApiException;
import london.users.demo.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This controller handles the requests related to the {@link User} class
 */
@RestController
@RequestMapping("london-users/api/v1/users")
public class UserController {

    /**
     * This service will implement the "business logic" behind users
     */
    private final UserService service;

    /**
     * Controller constructor; it initializes the user service. Parameter is autowired
     * @param userService service that implements the business logic
     */
    @Autowired
    public UserController(UserService userService){
        service = userService;
    }

    /**
     * This request retrieves the user with the specified id from the API
     * @param id the id of the user to retrieve
     * @return an instance of User
     */
    @ApiOperation(value = "This request retrieves the user with the specified id from the API",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
        }
    )
    @GetMapping(value = "{id}", produces = { "application/json" })
    public User getUser(@PathVariable Integer id){
        return service.getUser(id);
    }

    /**
     * This request retrieves all the users available from the API
     * @return a list containing all the users retrieved by the API
     */
    @ApiOperation(value = "This request retrieves all the users available from the API",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
        }
    )
    @GetMapping()
    public List<User> getUsers(){
        return service.getUsers();
    }

    /**
     * This request retrieves all the users from the API that are registered in the specified city
     * @param cityName the name of the city
     * @return a list containing the users retrieved by the API
     */
    @ApiOperation(value = "This request retrieves all the users from the API that are registered in the specified city",
            response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
        }
    )
    @GetMapping(value = "/city/{cityName}")
    public List<User> getUsersByCity(@PathVariable String cityName){
        return service.getUsersByCity(cityName);
    }

    /**
     * This request finds all the users that live/are registered around the specified city. Optionally, the user
     * can specify the radius (default value: 50).
     * @param cityName the name of the city
     * @param radius optional parameter, the search radius in miles
     * @return a list containing the users retrieved by the API
     */
    @ApiOperation(value = "This request finds all the users that live/are registered around the specified city. " +
            "Optionally, the user can specify the radius (default value: 50)",
            response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Negative radius value provided."),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
        }
    )
    @GetMapping(value = {"/city-radius/{cityName}"})
    public List<User> getUsersByCityRadiusPar(
            @PathVariable String cityName,
            @RequestParam(required = false) Optional<Integer> radius){
        // if a negative radius is provided throw an exception
        if(radius.isPresent() && radius.get()<0){
            throw new ApiException("Negative Radius provided. Value: "+ radius.get(), "Endpoint /city-radius/{cityName}", HttpStatus.BAD_REQUEST);
        }
        // convert radius from miles to meters
        double r = radius.orElse(50) * 1609.344;
        return service.getUsersByCityRadius(cityName, r);
    }
}
