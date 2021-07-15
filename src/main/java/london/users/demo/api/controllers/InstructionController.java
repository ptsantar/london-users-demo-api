package london.users.demo.api.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import london.users.demo.api.services.InstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles the request (only 1 for now) regarding the assignment instructions
 */
@RestController
@RequestMapping("london-users/api/v1/instructions")
public class InstructionController {

    /**
     * The service that will implement the "business logic"
     */
    private InstructionService service;

    @Autowired
    public InstructionController(InstructionService instructionService){
        service = instructionService;
    }

    /**
     * This request utilizes the /instructions endpoint
     * @return a JSON string with the instructions
     */
    @ApiOperation(value = "This request utilizes the `/instructions` endpoint to retrieve the instructions of the assignment",
            response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
        }
    )
    @GetMapping(produces = { "application/json" })
    public ResponseEntity<String> getInstructions(){
        return ResponseEntity.ok(service.getInstructions());
    }
}
