package io.pivotal.ecosystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class Controller {

    @RequestMapping(value = "/hello")

    public ResponseEntity<Map<String, Object>> sayHello() throws UnknownHostException {
        log.info("someone is out there!");

        Map m = new HashMap<String, Object>();
        m.put("hello from " + InetAddress.getLocalHost().getHostAddress(), new Date());
        log.info("am saying: " + m);

        return new ResponseEntity<>(m, HttpStatus.OK);
    }
}