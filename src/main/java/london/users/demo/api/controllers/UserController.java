package london.users.demo.api.controllers;

import london.users.demo.api.model.User;
import london.users.demo.api.services.ApiException;
import london.users.demo.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("london-users/api/v1/users")
public class UserController {

    /**
     * This service will implement the "business logic" behind users
     */
    private final UserService service;

    @Autowired
    public UserController(UserService userService){
        service = userService;
    }

    /**
     * This request retrieves the user with the specified id from the API
     * @param id the id of the user to retrieve
     * @return an instance of User
     */
    @GetMapping(value = "{id}", produces = { "application/json" })
    public User getUser(@PathVariable Integer id){
        return service.getUser(id);
    }

    /**
     * This request retrieves all the users with the specified id from the API
     * @return a list containing all the users retrieved by the API
     */
    @GetMapping()
    public List<User> getUsers(){
        return service.getUsers();
    }

    /**
     * This request retrieves all the users from the API that are registered in the specified city
     * @return a list containing the users retrieved by the API
     */
    @GetMapping(value = "/city/{cityName}")
    public List<User> getUsersByCity(@PathVariable String cityName){
        return service.getUsersByCity(cityName);
    }

    /**
     * This request finds all the users that live/are registered around the specified city. Optionally, the user
     * can specify the radius (default value: 50).
     * @return a list containing the users retrieved by the API
     */
    @GetMapping(value = {"/city-radius/{cityName}"})
    public List<User> getUsersByCityRadiusPar(
            @PathVariable String cityName,
            @RequestParam Optional<Integer> radius){
        // if a negative radius is provided throw an exception
        if(radius.isPresent() && radius.get()<0){
            throw new ApiException("Negative Radius provided. Value: "+ radius.get(), "Endpoint /city-radius/{cityName}", HttpStatus.BAD_REQUEST);
        }
        // convert radius from miles to meters
        double r = radius.orElse(50) * 1609.344;
        return service.getUsersByCityRadius(cityName, r);
    }
}
