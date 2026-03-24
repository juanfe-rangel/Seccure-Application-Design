package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Collections;

@SpringBootApplication
public class Secureweb{
    public static void main(String[] args) {
        //secureURl();

        SpringApplication app = new SpringApplication(Secureweb.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "35000"));
        app.run(args);
    }
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }



}
