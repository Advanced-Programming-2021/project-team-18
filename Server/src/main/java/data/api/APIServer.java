package data.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class APIServer {
    public static void main(String[] args) {
        SpringApplication.run(APIServer.class , args);
    }

}
