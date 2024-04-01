package edu.fd.se.rjw.carpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CarPanelApplication {

    public static void main(String[] args) {
        checkRequiredEnvs();
        SpringApplication.run(CarPanelApplication.class, args);
    }

    public static void checkRequiredEnvs() {
        var requiredEnvs = List.of("CAR_MODEL");
        var errorMessages = List.of("CAR_MODEL未设置，请设置CAR_MODEL环境变量为x3或limo");
        for (int i = 0; i < requiredEnvs.size(); i++) {
            var env = requiredEnvs.get(i);
            var errorMessage = errorMessages.get(i);
            if (System.getenv(env) == null) {
                System.err.println(errorMessage);
                System.exit(1);
            }
        }
    }

}
