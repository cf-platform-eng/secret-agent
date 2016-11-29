package io.pivotal.ecosystem;

import feign.RequestLine;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AgentRepository {

    @RequestLine("GET /hello")
    public Map<String, Object> sayHello();

}