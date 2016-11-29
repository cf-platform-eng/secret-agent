package io.pivotal.ecosystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class Controller {

    private AgentRepository agentRepository;

    public Controller(AgentRepository agentRepository) {
        super();
        this.agentRepository = agentRepository;
    }

    @RequestMapping(value = "/hello")
    public ResponseEntity<Map<String, Object>> sayHello() {
        log.info("passing through...");
        return new ResponseEntity<>(agentRepository.sayHello(), HttpStatus.OK);
    }
}