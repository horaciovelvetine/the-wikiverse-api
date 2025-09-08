package edu.velvet.Wikiverse.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class TheWikiverseApiApplication {

	/**
	 * Setup to track startup details: https://www.baeldung.com/spring-boot-actuator-startup
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TheWikiverseApiApplication.class);
		app.setApplicationStartup(new BufferingApplicationStartup(2048));
		app.run(args);
	}

}
