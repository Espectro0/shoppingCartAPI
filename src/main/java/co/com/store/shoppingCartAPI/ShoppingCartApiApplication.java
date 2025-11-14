package co.com.store.shoppingCartAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class ShoppingCartApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartApiApplication.class);

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(ShoppingCartApiApplication.class, args).getEnvironment();

        String port = env.getProperty("local.server.port", "8080");

        if (logger.isDebugEnabled()) {
            logger.debug("Shopping Cart API started successfully.");
            logger.debug("Shopping Cart API is running at http://localhost:{}/v1/api/", port);
            logger.debug("Java Version: {}", System.getProperty("java.version"));
            logger.debug("Spring Boot Version: {}", SpringBootVersion.getVersion());
        }

    }
}
