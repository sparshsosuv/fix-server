package com.flowlinx.fix.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan("com.flowlinx.fix.server")
public class AppServer {

    public static void main(String[] args) {
		SpringApplication.run(AppServer.class, args);
	}

}
