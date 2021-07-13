package london.users.demo.api.controllers;

import london.users.demo.api.model.User;
import london.users.demo.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
