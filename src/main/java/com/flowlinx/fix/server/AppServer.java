package com.flowlinx.fix.server;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan("com.flowlinx.fix.server")
public class AppServer {

    public static void main(String[] args) {
		new SpringApplicationBuilder()
				.listeners(new ApplicationPidFileWriter())
				.bannerMode(Banner.Mode.OFF)
				.sources(AppServer.class)
				.run(args);
	}

}
