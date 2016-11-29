package io.pivotal.ecosystem;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Configuration
public class Config implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        log.info("passthrough port: " + event.getEmbeddedServletContainer().getPort());
        try {
            log.info("passthrough address: " + InetAddress.getLocalHost().getHostAddress());
            log.info("passthrough name: " + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            log.error("oops: ", e);
        }
    }
}
