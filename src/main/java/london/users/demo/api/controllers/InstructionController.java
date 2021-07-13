package london.users.demo.api.controllers;

import london.users.demo.api.services.InstructionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping(produces = { "application/json" })
    public String getInstructions(){
        return service.getInstructions();
    }
}
