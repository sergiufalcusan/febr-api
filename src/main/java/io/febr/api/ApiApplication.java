package io.febr.api;

import io.febr.api.config.RsaKeyConfigProperties;
import io.febr.api.config.SendGridProperties;
import io.febr.api.controller.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyConfigProperties.class, SendGridProperties.class})
@Import(GlobalExceptionHandler.class)
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
