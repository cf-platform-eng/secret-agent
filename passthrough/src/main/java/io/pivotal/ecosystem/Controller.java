package io.pivotal.ecosystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class Controller {

    @RequestMapping(value = "/hello")
    public ResponseEntity<String> sayHello() {
        log.info("passing through...");
        return new ResponseEntity<>("hello on " + new Date().toString(), HttpStatus.OK);
    }
}